package com.zsoft.service.extension;

import com.zsoft.domain.User;
import com.zsoft.domain.extension.Appointment;
import com.zsoft.domain.extension.AppointmentStatus;
import com.zsoft.domain.extension.Doctor;
import com.zsoft.repository.UserRepository;
import com.zsoft.repository.extension.AppointmentRepository;
import com.zsoft.repository.extension.DoctorRepository;
import com.zsoft.service.MailService;
import com.zsoft.service.dto.extension.AppointmentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class AppointmentService {

    private final Logger log = LoggerFactory.getLogger(DoctorService.class);

    private final DoctorRepository doctorRepository;

    private final UserRepository userRepository;

    private final AppointmentRepository appointmentRepository;

    private final MailService mailService;

    public AppointmentService(
        DoctorRepository doctorRepository,
        UserRepository userRepository,
        AppointmentRepository appointmentRepository,
        MailService mailService
    ) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.mailService = mailService;
    }

    public Optional<Appointment> takeAppointment(AppointmentDTO appointmentDTO) {
        return Optional.of(appointmentRepository.findById(appointmentDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(appointment -> {
                appointment.setPatient(appointmentDTO.getPatient());
                appointment.setStatus(AppointmentStatus.PENDING);
                log.debug("Update Information for Appointment : {}", appointment);
                appointmentRepository.saveAndFlush(appointment);
                return appointment;
            });
    }

    public Optional<Appointment> updateAppointment(AppointmentDTO appointmentDTO, Long old_appointment_id) {
        this.cancelAppointment(old_appointment_id);
        return this.takeAppointment(appointmentDTO);
    }

    public void cancelAppointment(Long id) {
        Optional.of(appointmentRepository.findById(id))
            .filter(Optional::isPresent)
            .map(Optional::get)
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
            });
    }

    @Transactional(readOnly = true)
    public Page<AppointmentDTO> getAllAppointment(Pageable pageable) {
        return appointmentRepository.findAll(pageable).map(AppointmentDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<AppointmentDTO> getAllAppointmentOfDoctor(Pageable pageable, Long doctor_user_id) {
        return appointmentRepository.findAppointmentsByDoctor_User_Id(pageable, doctor_user_id).map(AppointmentDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<AppointmentDTO> getAllAppointmentOfPatient(Pageable pageable, Long patient_id) {
        return appointmentRepository.findAppointmentsByPatient_Id(pageable, patient_id).map(AppointmentDTO::new);
    }

    @Transactional(readOnly = true)
    public Stream<AppointmentDTO> getAppointmentsOfDoctorByDate(Long doctor_id, Date date) {
        return appointmentRepository
            .findAppointmentsByDoctor_IdAndDateAndStatusNot(doctor_id, date, AppointmentStatus.FREE)
            .map(AppointmentDTO::new);
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAvailableAppointmentsOfDoctorByDate(Long doctor_id, Date date) {
        return appointmentRepository
            .findAppointmentsByDoctor_IdAndDateAndStatus(doctor_id, date, AppointmentStatus.FREE)
            .map(AppointmentDTO::new)
            .collect(Collectors.toList());
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

    // create free appointments for each doctor
    private void createNewAppointments() {

    }
}
