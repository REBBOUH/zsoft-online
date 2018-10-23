package com.zsoft.service.extension;

import com.zsoft.domain.extension.Appointment;
import com.zsoft.domain.extension.AppointmentStatus;
import com.zsoft.repository.extension.AppointmentRepository;
import com.zsoft.service.dto.extension.AppointmentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Calendar;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Transactional
public class AppointmentService {

    private final Logger log = LoggerFactory.getLogger(DoctorService.class);

    private final AppointmentRepository appointmentRepository;

    private final AppointmentMailService mailService;

    public AppointmentService(
        AppointmentRepository appointmentRepository,
        AppointmentMailService mailService
    ) {
        this.appointmentRepository = appointmentRepository;
        this.mailService = mailService;
    }

    public Appointment takeAppointment(AppointmentDTO appointmentDTO) {
        return appointmentRepository
            .findById(appointmentDTO.getId())
            .map(appointment -> {
                appointment.setPatient(appointmentDTO.getPatient());
                appointment.setStatus(AppointmentStatus.PENDING);
                log.debug("Update Information for Appointment : {}", appointment);
                return appointmentRepository.saveAndFlush(appointment);
            })
            .orElseThrow(() -> new IllegalArgumentException("Illegal Arguments, Appointment not found !"));
    }

    public Appointment updateAppointment(AppointmentDTO appointmentDTO, Long old_appointment_id) {
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
        return appointmentRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Appointment> getAllAppointmentOfDoctor(Pageable pageable, Long doctor_user_id) {
        return appointmentRepository.findAppointmentsByDoctor_User_Id(pageable, doctor_user_id);
    }

    @Transactional(readOnly = true)
    public Page<Appointment> getAllAppointmentOfPatient(Pageable pageable, Long patient_id) {
        return appointmentRepository.findAppointmentsByPatient_Id(pageable, patient_id);
    }

    @Transactional(readOnly = true)
    public Stream<Appointment> getAppointmentsOfDoctorByDate(Long doctor_id, Date date) {
        return appointmentRepository
            .findAppointmentsByDoctor_IdAndDateAndStatusNot(doctor_id, date, AppointmentStatus.FREE);
    }

    @Transactional(readOnly = true)
    public Stream<Appointment> getAvailableAppointmentsOfDoctorByDate(Long doctor_id, Date date) {
        return appointmentRepository
            .findAppointmentsByDoctor_IdAndDateAndStatus(doctor_id, date, AppointmentStatus.FREE);
    }

    public Optional<Appointment> find(Long appointment_id) {
        return appointmentRepository.findAppointmentsById(appointment_id);
    }

    /**
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void appointmentScheduled(){
        changeAppointmentsStatus();
        createNewAppointments();
        sendReminderAppointmentsEmails();
    }

    private void changeAppointmentsStatus(){
        // Load Old Appointments with Status 'Panding'
        // Update Status of Appointments to 'Passed'
        Date today = new Date(Calendar.getInstance().getTime().getTime());
        appointmentRepository
            .findAppointmentsByDateBeforeAndStatus(today, AppointmentStatus.PENDING)
            .forEach(
                appointment -> {
                    appointment.setStatus(AppointmentStatus.PASSED);
                    log.debug("Passed an Appointment: {}", appointment);
                    appointmentRepository.saveAndFlush(appointment);
                });
    }

    private void sendReminderAppointmentsEmails(){
        Date today = new Date(Calendar.getInstance().getTime().getTime());
        appointmentRepository
            .findAppointmentsByDateAndStatus(today, AppointmentStatus.PENDING)
            .forEach(mailService::sendReminderMail);
    }

    // create free appointments for each doctor
    private void createNewAppointments() {

    }
}
