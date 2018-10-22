package com.zsoft.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.zsoft.config.Constants;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;

@Entity
@Table(name = "jhi_timeslot")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Timeslot implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="day_week", length = 2)
    private Integer dayOfWeek;

    @Column(name = "time_start")
    private Time timeStart;

    @Column(name = "time_end")
    private Time timeEnd;

    @Column(name = "status", columnDefinition = "tinyint", length = 1)
    private boolean status;

    @Column(name = "doctor_id")
    private Long doctorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Time getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Time timeStart) {
        this.timeStart = timeStart;
    }

    public Time getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Time timeEnd) {
        this.timeEnd = timeEnd;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Timeslot() {
    }

    public Timeslot(Integer dayOfWeek, Time timeStart, Time timeEnd, boolean status, Long doctorId) {
        this.dayOfWeek = dayOfWeek;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.status = status;
        this.doctorId = doctorId;
    }

    public ArrayList<Appointment> toAppointments(ArrayList<Appointment> appointments){
        long fullTime = Duration.between(timeStart.toLocalTime(), timeEnd.toLocalTime()).toMinutes();
        int nbrOfSlots = (int)(fullTime/ Constants.SLOT_LENGTH);
        LocalTime time = timeStart.toLocalTime();
        for (int i = 0; i < nbrOfSlots; i++ ){
            Appointment appointment = new Appointment();
            appointment.setTimeStart(Time.valueOf(time));
            appointment.setTimeEnd(Time.valueOf(time.plusMinutes(Constants.SLOT_LENGTH)));
            appointment.setStatus("Available");
            appointments.add(appointment);
            time = time.plusMinutes(Constants.SLOT_LENGTH);
        }
        return appointments;
    }

    @Override
    public String toString() {
        return "Timeslot{" +
            "id=" + id +
            ", dayOfWeek=" + dayOfWeek +
            ", timeStart=" + timeStart +
            ", timeEnd=" + timeEnd +
            ", status=" + status +
            ", doctorId=" + doctorId +
            '}';
    }
}
