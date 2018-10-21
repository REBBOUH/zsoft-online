package com.zsoft.service.dto;

import com.zsoft.domain.Timeslot;

import java.sql.Time;

public class TimeslotDTO{
    private Long id;

    private Integer dayOfWeek;

    private String timeStart;

    private String timeEnd;

    private Boolean status;

    private Long doctorId;

    public TimeslotDTO() {
    }

    public TimeslotDTO(Timeslot timeslot) {
        this.id = timeslot.getId();
        this.dayOfWeek = timeslot.getDayOfWeek();
        if( timeslot.getTimeStart() != null )
            this.timeStart = timeslot.getTimeStart().toString();
        if( timeslot.getTimeEnd() != null )
            this.timeEnd = timeslot.getTimeEnd().toString();
        this.status = timeslot.getStatus();
        this.doctorId = timeslot.getDoctorId();
    }

    public Timeslot toTimeslot(){
        return new Timeslot(this.dayOfWeek, Time.valueOf(this.timeStart), Time.valueOf(this.timeEnd), this.status, this.doctorId);
    }

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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public String toString() {
        return "TimeslotDTO{" +
            "id=" + id +
            ", dayOfWeek=" + dayOfWeek +
            ", timeStart='" + timeStart + '\'' +
            ", timeEnd='" + timeEnd + '\'' +
            ", status=" + status +
            ", doctorId=" + doctorId +
            '}';
    }
}
