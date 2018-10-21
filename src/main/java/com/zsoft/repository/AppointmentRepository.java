package com.zsoft.repository;

import com.zsoft.domain.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Page<Appointment> findAppointmentsByDoctor_Id(Pageable pageable, Long doctor_id);

    Page<Appointment> findAppointmentsByDoctor_User_Id(Pageable pageable, Long doctor_user_id);

    Page<Appointment> findAppointmentsByPatient_Id(Pageable pageable, Long patient_id);

    List<Appointment> findAppointmentsByDoctor_IdAndDate(Long doctor_id, Date date);

    List<Appointment> findAppointmentsByDoctor_IdAndDateAndStatusNot(Long doctor_id, Date date, String status);

    Optional<Appointment> findAppointmentsById(Long appointment_id);

    List<Appointment> findAppointmentsByDateBeforeAndStatus(Date date, String status);

    List<Appointment> findAppointmentsByDateAndStatus(Date date, String status);
}
