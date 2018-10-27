package com.zsoft.domain.extension;

import com.zsoft.domain.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "appointment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Appointment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "patient_id")
    private User patient;

    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private AppointmentStatus status;

    @Column(name = "date")
    private Date date;

    @Column(name = "time_start")
    private Time timeStart;

    @Column(name = "time_end")
    private Time timeEnd;

    public Appointment() {
    }

    public Appointment(Doctor doctor, User patient, AppointmentStatus status, Date date, Time timeStart, Time timeEnd) {
        this.doctor = doctor;
        this.patient = patient;
        this.status = status;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
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

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "Appointment{" +
            "id=" + id +
            ", doctor=" + doctor  +
            ", patient=" + patient +
            ", status=" + status +
            ", date=" + date +
            ", timeStart=" + timeStart +
            ", timeEnd=" + timeEnd +
            '}';
    }
}
