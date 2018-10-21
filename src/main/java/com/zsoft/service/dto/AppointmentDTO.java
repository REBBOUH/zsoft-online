package com.zsoft.service.dto;

import com.zsoft.domain.Appointment;
import com.zsoft.domain.Doctor;
import com.zsoft.domain.User;

import javax.validation.constraints.Size;
import java.sql.Date;
import java.sql.Time;

public class AppointmentDTO {
    private Long id;

    private Doctor doctor;

    private User patient;

    private Date date;

    private String timeStart;

    private String timeEnd;

    @Size(max = 15)
    private String status;

    public AppointmentDTO() {
    }

    public AppointmentDTO(Appointment appointment) {
        this.id = appointment.getId();
        this.date = appointment.getDate();
        if( appointment.getTimeStart() != null )
            this.timeStart = appointment.getTimeStart().toString();
        if( appointment.getTimeEnd() != null )
            this.timeEnd = appointment.getTimeEnd().toString();
        this.status = appointment.getStatus();
        this.doctor = appointment.getDoctor();
        this.patient = appointment.getPatient();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AppointmentDTO{" +
            "id=" + id +
            ", doctor=" + doctor +
            ", patient=" + patient +
            ", date=" + date +
            ", timeStart=" + timeStart +
            ", timeEnd=" + timeEnd +
            ", status='" + status + '\'' +
            '}';
    }
}
