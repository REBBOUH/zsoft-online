package com.zsoft.service.extension;

import com.zsoft.config.extension.ExtenstionConstant;
import com.zsoft.domain.User;
import com.zsoft.domain.extension.Appointment;
import com.zsoft.domain.extension.AppointmentStatus;
import com.zsoft.domain.extension.PersistentConfiguration;
import com.zsoft.repository.extension.AppointmentRepository;
import com.zsoft.service.dto.extension.AppointmentDTO;
import com.zsoft.service.mapper.extension.AppointmentMapper;
import com.zsoft.service.util.AppointmentUtil;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class AppointmentService {

    private final Logger log = LoggerFactory.getLogger(DoctorService.class);

    private final AppointmentRepository appointmentRepository;

    private final AppointmentMailService mailService;

    private final PersistentConfigurationService configurationService;

    private AppointmentMapper mapper = Mappers.getMapper(AppointmentMapper.class);

    public AppointmentService(
        AppointmentRepository appointmentRepository,
        AppointmentMailService mailService,
        PersistentConfigurationService configurationService
    ) {
        this.appointmentRepository = appointmentRepository;
        this.mailService = mailService;
        this.configurationService = configurationService;
    }

    public AppointmentDTO takeAppointment(AppointmentDTO appointmentDTO) {
        return appointmentRepository
            .findById(appointmentDTO.getId())
            .filter(ap -> ap.getStatus() == AppointmentStatus.FREE)
            .map(appointment -> {
                User patient = new User();
                patient.setId(appointmentDTO.getPatientId());
                appointment.setPatient(patient);
                appointment.setStatus(AppointmentStatus.PENDING);
                log.debug("Update Information for Appointment : {}", appointment);
                return appointmentRepository.saveAndFlush(appointment);
            })
            .map(mapper::toDto)
            .orElseThrow(() -> new IllegalArgumentException("Illegal Arguments, Appointment not found !"));
    }

    public AppointmentDTO updateAppointment(AppointmentDTO appointmentDTO, Long old_appointment_id) {
        this.cancelAppointment(old_appointment_id);
        return this.takeAppointment(appointmentDTO);
    }

    public void cancelAppointment(Long id) {
        appointmentRepository.findById(id)
            .map(appointment -> {
                // Change status to CANCELED
                appointment.setStatus(AppointmentStatus.CANCELED);
                // Create a Copy with Status = FREE AND Patient = null
                Appointment copyAppointment = new Appointment(
                    appointment.getDoctor(),
                    null,
                    AppointmentStatus.FREE,
                    appointment.getDate(),
                    appointment.getTimeStart(),
                    appointment.getTimeEnd()
                );
                appointmentRepository.save(copyAppointment);
                log.debug("Cancel an Appointment: {}", appointment);
                return appointment;
            })
            .orElseThrow(() -> new IllegalArgumentException("Illegal Arguments, Appointment not found !"));
    }

    @Transactional(readOnly = true)
    public Page<Appointment> getAllAppointment(Pageable pageable) {
        return appointmentRepository.findAppointmentsByStatusNot(pageable, AppointmentStatus.FREE);
    }

    @Transactional(readOnly = true)
    public Page<Appointment> getAllAppointmentOfDoctor(Pageable pageable, Long doctor_user_id) {
        return appointmentRepository.findAppointmentsByDoctor_User_IdAndStatusNot(pageable, doctor_user_id, AppointmentStatus.FREE);
    }

    @Transactional(readOnly = true)
    public Page<Appointment> getAllAppointmentOfPatient(Pageable pageable, Long patient_id) {
        return appointmentRepository.findAppointmentsByPatient_IdAndStatusNot(pageable, patient_id, AppointmentStatus.FREE);
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsOfDoctorByDate(Long doctor_id, Date date) {
        return appointmentRepository
            .findAppointmentsByDoctor_IdAndDateAndStatusNot(doctor_id, date, AppointmentStatus.FREE);
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAvailableAppointmentsOfDoctorByDate(Long doctor_id, Date date) {
        return appointmentRepository
            .findAppointmentsByDoctor_IdAndDateAndStatus(doctor_id, date, AppointmentStatus.FREE);
    }

    @Transactional(readOnly = true)
    public Optional<Appointment> find(Long appointment_id) {
        return appointmentRepository.findAppointmentsById(appointment_id);
    }

    @Transactional(readOnly = true)
    public void resetNextAppointments(Long doctor_id){
        appointmentRepository.deleteNextAppointmentsByDoctor(doctor_id);
        createNewAppointments(doctor_id);
    }

    /**
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void appointmentScheduled(){
        System.out.println("START SCHEDULER");
        changeAppointmentsStatus();
        createNewAppointments(null);
        sendReminderAppointmentsEmails();
    }

    /**
     * Change Old Appointments with Status 'Pending' to 'Passed'
     * Delete Old Appointment with Status 'FREE'
     */
    private void changeAppointmentsStatus(){
        final Date today = Date.valueOf(LocalDate.now());
        appointmentRepository
            .findAppointmentsByDateBeforeAndStatus(today, AppointmentStatus.PENDING)
            .forEach(
                appointment -> {
                    appointment.setStatus(AppointmentStatus.PASSED);
                    log.debug("Passed an Appointment: {}", appointment);
                    appointmentRepository.saveAndFlush(appointment);
                });
        appointmentRepository.deleteAppointmentsPassed();
    }

    /**
     * Send Reminder Email to Patient on the day of Appointment
     */
    private void sendReminderAppointmentsEmails(){
        Date today = new Date(Calendar.getInstance().getTime().getTime());
        appointmentRepository
            .findAppointmentsByDateAndStatus(today, AppointmentStatus.PENDING)
            .forEach(mailService::sendReminderMail);
    }

    /**
     * Create free appointments for all doctors (doctor_id = null) or specific doctor
     * For NEXT_APPOINTMENT_DAYS (default : 30 days)
     * And TIME SLOT LENGTH (default : 30 min)
     */
    private void createNewAppointments(Long doctor_id) {
        Stream<PersistentConfiguration> configurationStream = configurationService
            .getByEntityAndKey("DOCTOR", "@TIME_SLOT");

        if( doctor_id != null ){
            configurationStream = configurationStream.filter(conf -> conf.getEntityId().equals(doctor_id));
        }
        configurationStream
            .forEach(conf -> {
                Long currentDoctorId = conf.getEntityId();
                Map<String, String> ts = configurationService
                    .getByEntityAndEntityId(conf.getKey(), Long.parseLong(conf.getValue()))
                    .collect(Collectors.toMap( PersistentConfiguration::getKey, PersistentConfiguration::getValue ));
                int dayOfWeek = Integer.parseInt(ts.get("DAY_OF_WEEK"));
                if( dayOfWeek == 0 ) dayOfWeek = 7;
                AppointmentUtil.getDatesForNextDays(ExtenstionConstant.NEXT_APPOINTMENT_DAYS)
                    .get(dayOfWeek)
                    .forEach(day -> {
                        Date date = Date.valueOf(day);
                        int apCounter = appointmentRepository
                            .findAppointmentsByDoctor_IdAndDateAndStatus(currentDoctorId, date, AppointmentStatus.FREE)
                            .size();
                        if( apCounter == 0 ){
                            Time timeStart = Time.valueOf(ts.get("TIME_START"));
                            Time timeEnd = Time.valueOf(ts.get("TIME_END"));
                            AppointmentUtil
                                .createAppointments(currentDoctorId, date, timeStart, timeEnd)
                                .forEach(appointmentRepository::save);
                        }
                    });
        });
    }
}
