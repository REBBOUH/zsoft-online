package com.zsoft.service.dto.extension;

import com.zsoft.domain.User;
import com.zsoft.domain.extension.Appointment;
import com.zsoft.domain.extension.Doctor;

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

    private User userDTO;

    private Set<AppointmentDTO> appointments;

    public DoctorDTO() {
    }

    public DoctorDTO(Doctor doctor) {
        this.id = doctor.getId();
        this.phone = doctor.getPhone();
        this.address = doctor.getAddress();
        if( doctor.getGender() != null )
            this.gender = doctor.getGender().toString();
        this.speciality = doctor.getSpeciality();
        if( doctor.getUser() != null )
            this.userId = doctor.getUser().getId();
        this.userDTO = doctor.getUser();
        this.appointments = new HashSet<>();
        if( doctor.getAppointments() != null )
            for (Appointment appointment: doctor.getAppointments()) {
                this.appointments.add(new AppointmentDTO(appointment));
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

    public User getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(User userDTO) {
        this.userDTO = userDTO;
    }

    public Set<AppointmentDTO> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<AppointmentDTO> appointments) {
        this.appointments = appointments;
    }

    @Override
    public String toString() {
        return "DoctorDTO{" +
            "id=" + id +
            ", phone='" + phone + '\'' +
            ", address='" + address + '\'' +
            ", gender='" + gender + '\'' +
            ", speciality='" + speciality + '\'' +
            ", userId=" + userId +
            ", userDTO=" + userDTO +
            '}';
    }
}