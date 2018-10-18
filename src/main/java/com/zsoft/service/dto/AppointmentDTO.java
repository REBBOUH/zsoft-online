package com.zsoft.service.dto;

import com.zsoft.domain.Appointment;

import javax.validation.constraints.Size;
import java.sql.Date;
import java.sql.Time;

public class AppointmentDTO {
    private Long id;

    private Long doctorId;

    private Long patientId;

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
        this.timeStart = appointment.getTimeStart().toString();
        this.timeEnd = appointment.getTimeEnd().toString();
        this.status = appointment.getStatus();
        this.doctorId = appointment.getDoctor().getId();
        this.patientId = appointment.getPatient().getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
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
            ", doctorId=" + doctorId +
            ", patientId=" + patientId +
            ", date=" + date +
            ", timeStart=" + timeStart +
            ", timeEnd=" + timeEnd +
            ", status='" + status + '\'' +
            '}';
    }
}
