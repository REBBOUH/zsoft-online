package com.zsoft.repository;

import com.zsoft.domain.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Page<Appointment> findAppointmentsByDoctor_Id(Pageable pageable, Long doctor_id);

    Page<Appointment> findAppointmentsByPatient_Id(Pageable pageable, Long patient_id);

    List<Appointment> findAppointmentsByDoctor_IdAndDate(Long patient_id, Date date);

}
