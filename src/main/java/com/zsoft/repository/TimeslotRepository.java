package com.zsoft.repository;

import com.zsoft.domain.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {

    List<Timeslot> findTimeslotsByDoctorIdAndDayOfWeek(Long doctor_id, int dayofweek);

    void deleteTimeslotByDoctorId(Long doctor_id);

}
