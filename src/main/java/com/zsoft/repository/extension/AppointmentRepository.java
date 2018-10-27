package com.zsoft.repository.extension;

import com.zsoft.domain.extension.Appointment;
import com.zsoft.domain.extension.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Page<Appointment> findAppointmentsByStatusNot(Pageable pageable, AppointmentStatus status);

    Page<Appointment> findAppointmentsByDoctor_User_IdAndStatusNot(Pageable pageable, Long doctor_user_id, AppointmentStatus status);

    Page<Appointment> findAppointmentsByPatient_IdAndStatusNot(Pageable pageable, Long patient_id, AppointmentStatus status);

    List<Appointment> findAppointmentsByDoctor_IdAndDateAndStatusNot(Long doctor_id, Date date, AppointmentStatus status);

    List<Appointment> findAppointmentsByDoctor_IdAndDateAndStatus(Long doctor_id, Date date, AppointmentStatus status);

    Optional<Appointment> findAppointmentsById(Long appointment_id);

    Stream<Appointment> findAppointmentsByDateBeforeAndStatus(Date date, AppointmentStatus status);

    Stream<Appointment> findAppointmentsByDateAndStatus(Date date, AppointmentStatus status);

    @Modifying
    @Transactional
    @Query(value = "delete from appointment ap where ap.doctor_id = :doctor_id and ap.date >= CURRENT_DATE ", nativeQuery = true)
    void deleteNextAppointmentsByDoctor(@Param("doctor_id") Long doctor_id);


    @Modifying
    @Transactional
    @Query(value = "delete from appointment ap where  ap.status = 'FREE' and (ap.date < CURRENT_DATE or (ap.date = CURRENT_DATE and ap.time_end < CURRENT_TIME) )", nativeQuery = true)
    void deleteAppointmentsPassed();
}
