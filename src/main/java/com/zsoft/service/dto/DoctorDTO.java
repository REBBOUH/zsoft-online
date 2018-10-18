package com.zsoft.service.dto;

import com.zsoft.domain.Doctor;
import com.zsoft.domain.Timeslot;

import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

public class DoctorDTO {
    private Long id;

    @Size(max = 50)
    private String phone;

    @Size(max = 254)
    private String address;

    @Size(max = 10)
    private String gender;

    @Size(max = 50)
    private String speciality;

    private Long userId;

    private Set<Timeslot> timeslots;

    public DoctorDTO() {
    }

    public DoctorDTO(Doctor doctor) {
        this.id = doctor.getId();
        this.phone = doctor.getPhone();
        this.address = doctor.getAddress();
        this.gender = doctor.getGender().toString();
        this.speciality = doctor.getSpeciality();
        this.userId = doctor.getUser().getId();
        this.timeslots = new HashSet<>();
        for (Timeslot timeslot: doctor.getTimeslots()) {
            this.timeslots.add(timeslot);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<Timeslot> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(Set<Timeslot> timeslots) {
        this.timeslots = timeslots;
    }

    @Override
    public String toString() {
        return "DoctorDTO{" +
            "id=" + id +
            ", phone='" + phone + '\'' +
            ", address='" + address + '\'' +
            ", gender='" + gender + '\'' +
            ", speciality='" + speciality + '\'' +
            ", userDTO=" + userId +
            '}';
    }
}
