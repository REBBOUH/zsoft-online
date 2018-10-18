package com.zsoft.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;

@Entity
@Table(name = "jhi_timeslot")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Timeslot implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="day_week")
    private int DayOfWeek;

    @Column(name = "time_start")
    private Time timeStart;

    @Column(name = "time_end")
    private Time timeEnd;

    @Column(name = "status", columnDefinition="tinyint(1) default 1")
    private Boolean Status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDayOfWeek() {
        return DayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        DayOfWeek = dayOfWeek;
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

    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean status) {
        Status = status;
    }


    public Timeslot() {
    }

    public Timeslot(int dayOfWeek, Time timeStart, Time timeEnd, Boolean status) {
        DayOfWeek = dayOfWeek;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        Status = status;
    }

    @Override
    public String toString() {
        return "Timeslot{" +
            "id=" + id +
            ", DayOfWeek=" + DayOfWeek +
            ", timeStart=" + timeStart +
            ", timeEnd=" + timeEnd +
            ", Status=" + Status +
            '}';
    }
}
