package com.zsoft.service.mapper.extension;

import com.zsoft.domain.User;
import com.zsoft.domain.extension.Appointment;
import com.zsoft.domain.extension.AppointmentStatus;
import com.zsoft.domain.extension.Doctor;
import com.zsoft.service.dto.extension.AppointmentDTO;
import org.mapstruct.Mapper;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    default AppointmentDTO toDto(Appointment appointment){
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        if( appointment.getDoctor() != null )
            dto.setDoctorId(appointment.getDoctor().getId());
        if( appointment.getPatient() != null)
            dto.setPatientId(appointment.getPatient().getId());
        if( appointment.getDate() != null)
            dto.setDate(appointment.getDate());
        if( appointment.getTimeStart() != null)
            dto.setTimeStart(new Date(appointment.getTimeStart().getTime()));
        if( appointment.getTimeEnd() != null)
            dto.setTimeEnd(new Date(appointment.getTimeEnd().getTime()));
        if( appointment.getStatus() != null)
            dto.setStatus(appointment.getStatus().toString());
        return dto;
    }

    List<AppointmentDTO> appointmentsToApointmentDTOs(List<Appointment> appointments);

    default Appointment toEntity(AppointmentDTO dto){
        Appointment ap = new Appointment();
        ap.setId(dto.getId());
        Doctor doctor = new Doctor();
            doctor.setId(dto.getDoctorId());
            ap.setDoctor(doctor);
        User patient = new User();
            patient.setId(dto.getPatientId());
            ap.setPatient(patient);
        if( dto.getDate() != null)
            ap.setDate(dto.getDate());
        if( dto.getTimeStart() != null)
            ap.setTimeStart(new Time(dto.getTimeStart().getTime()));
        if( dto.getTimeEnd() != null)
            ap.setTimeEnd(new Time(dto.getTimeEnd().getTime()));
        if( dto.getStatus() != null)
            ap.setStatus(AppointmentStatus.valueOf(dto.getStatus()));
        return ap;
    }

    List<Appointment> apointmentDTOsAppointments(List<AppointmentDTO> appointmentDTOS);

    default Appointment appointmentFromId(Long id){
        if (id == null) {
            return null;
        }
        Appointment appointment = new Appointment();
        appointment.setId(id);
        return appointment;
    }
}
