package com.zsoft.service;

import com.zsoft.domain.Appointment;
import com.zsoft.domain.Doctor;
import com.zsoft.domain.User;
import com.zsoft.repository.AppointmentRepository;
import com.zsoft.repository.DoctorRepository;
import com.zsoft.repository.UserRepository;
import com.zsoft.service.dto.AppointmentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppointmentService {

    private final Logger log = LoggerFactory.getLogger(DoctorService.class);

    private final DoctorRepository doctorRepository;

    private final UserRepository userRepository;

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(DoctorRepository doctorRepository, UserRepository userRepository, AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }



    public Appointment createAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = new Appointment();
        appointment.setDate(appointmentDTO.getDate());
        appointment.setTimeStart(Time.valueOf(appointmentDTO.getTimeStart()));
        appointment.setTimeEnd(Time.valueOf(appointmentDTO.getTimeEnd()));
        appointment.setStatus(appointmentDTO.getStatus());
        Doctor doctor = doctorRepository.findById(appointmentDTO.getDoctorId()).get();
        User patient = userRepository.findById(appointmentDTO.getPatientId()).get();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        log.debug("Created Information for Appointment : {}", appointment);
        appointmentRepository.saveAndFlush(appointment);
        return appointment;
    }


    /**
     * Update all information for a specific appointment, and return the modified appointment.
     *
     * @param appointmentDTO appointment to update
     * @return updated appointment
     */
    public Optional<AppointmentDTO> updateAppointment(AppointmentDTO appointmentDTO) {
        return Optional.of(appointmentRepository.findById(appointmentDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(appointment -> {
                appointment.setDate(appointmentDTO.getDate());
                appointment.setTimeStart(Time.valueOf(appointmentDTO.getTimeStart()));
                appointment.setTimeEnd(Time.valueOf(appointmentDTO.getTimeEnd()));
                appointment.setStatus(appointmentDTO.getStatus());
                log.debug("Changed Information for Appointment: {}", appointment);
                return appointment;
            })
            .map(AppointmentDTO::new);
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.findById(id).ifPresent(appointment -> {
            appointmentRepository.delete(appointment);
            log.debug("Deleted Appointment: {}", appointment);
        });
    }

    @Transactional(readOnly = true)
    public Page<AppointmentDTO> getAllAppointment(Pageable pageable) {
        return appointmentRepository.findAll(pageable).map(AppointmentDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<AppointmentDTO> getAllAppointmentOfDoctor(Pageable pageable, Long doctor_id) {
        return appointmentRepository.findAppointmentsByDoctor_Id(pageable, doctor_id).map(AppointmentDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<AppointmentDTO> getAllAppointmentOfPatient(Pageable pageable, Long patient_id) {
        return appointmentRepository.findAppointmentsByPatient_Id(pageable, patient_id).map(AppointmentDTO::new);
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsOfDoctorByDate(Long doctor_id, Date date) {
        List<Appointment> appointments = appointmentRepository.findAppointmentsByDoctor_IdAndDate(doctor_id, date);
        return appointments.stream().map(AppointmentDTO::new).collect(Collectors.toList());
    }
}
