package com.zsoft.repository.extension;

import com.zsoft.domain.extension.Appointment;
import com.zsoft.domain.extension.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Page<Appointment> findAppointmentsByDoctor_User_Id(Pageable pageable, Long doctor_user_id);

    Page<Appointment> findAppointmentsByPatient_Id(Pageable pageable, Long patient_id);

    Stream<Appointment> findAppointmentsByDoctor_IdAndDateAndStatusNot(Long doctor_id, Date date, AppointmentStatus status);

    Stream<Appointment> findAppointmentsByDoctor_IdAndDateAndStatus(Long doctor_id, Date date, AppointmentStatus status);

    Optional<Appointment> findAppointmentsById(Long appointment_id);

    Stream<Appointment> findAppointmentsByDateBeforeAndStatus(Date date, AppointmentStatus status);

    Stream<Appointment> findAppointmentsByDateAndStatus(Date date, AppointmentStatus status);
}
