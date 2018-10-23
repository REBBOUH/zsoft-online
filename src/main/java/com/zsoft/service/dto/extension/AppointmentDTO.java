package com.zsoft.service.dto.extension;

import com.zsoft.domain.User;
import com.zsoft.domain.extension.Appointment;
import com.zsoft.domain.extension.AppointmentStatus;
import com.zsoft.domain.extension.Doctor;

import javax.validation.constraints.Size;
import java.sql.Date;
import java.sql.Time;

public class AppointmentDTO {
    private Long id;

    private Doctor doctor;

    private User patient;

    private Date date;

    private Time timeStart;

    private Time timeEnd;

    @Size(max = 15)
    private String status;

    public AppointmentDTO() {
    }

    public AppointmentDTO(Long id, Doctor doctor, User patient, Date date, Time timeStart, Time timeEnd, @Size(max = 15) String status) {
        this.id = id;
        this.doctor = doctor;
        this.patient = patient;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.status = status;
    }

    public AppointmentDTO(Appointment appointment) {
        this.id = appointment.getId();
        this.doctor = appointment.getDoctor();
        this.patient = appointment.getPatient();
        this.date = appointment.getDate();
        this.timeStart = appointment.getTimeStart();
        this.timeEnd = appointment.getTimeEnd();
        this.status = appointment.getStatus().toString();
    }

    public Appointment toAppointment() {
        Appointment ap = new Appointment();
        ap.setId(this.id);
        ap.setDoctor(this.doctor);
        ap.setPatient(this.patient);
        ap.setDate(this.date);
        ap.setTimeStart(this.timeStart);
        ap.setTimeEnd(this.timeEnd);
        ap.setStatus(AppointmentStatus.valueOf(this.status));
        return ap;
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
