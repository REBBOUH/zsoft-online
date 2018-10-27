package com.zsoft.service.dto.extension;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Size;
import java.sql.Date;

public class AppointmentDTO {
    private Long id;

    private Long doctorId;

    private Long patientId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private Date timeStart;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private Date timeEnd;

    @Size(max = 15)
    private String status;

    public AppointmentDTO() {
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

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
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
