package com.zsoft.service.mapper.extension;

import com.zsoft.domain.User;
import com.zsoft.domain.extension.Doctor;
import com.zsoft.domain.extension.Gender;
import com.zsoft.service.dto.extension.DoctorDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    default DoctorDTO toDto(Doctor doctor){
        DoctorDTO dto = new DoctorDTO();
        dto.setId(doctor.getId());
        dto.setPhone(doctor.getPhone());
        dto.setAddress(doctor.getAddress());
        dto.setSpeciality(doctor.getSpeciality());
        if( doctor.getGender() != null ) {
            dto.setGender(doctor.getGender().toString());
        }
        if( doctor.getUser() != null )
            dto.setUserId(doctor.getUser().getId());
        return dto;
    }

    List<DoctorDTO> doctorsToDoctorDTOs(List<Doctor> users);

    default Doctor toEntity(DoctorDTO dto){
        Doctor doctor = new Doctor();
        doctor.setId(dto.getId());
        // set Profile information
        doctor.setPhone(dto.getPhone());
        doctor.setAddress(dto.getAddress());
        doctor.setGender(Gender.valueOf(dto.getGender()));
        doctor.setSpeciality(dto.getSpeciality());
        // set profile user
        User user = new User();
        user.setId(dto.getUserId());
        doctor.setUser(user);
        return doctor;
    }

    List<Doctor> DoctorDTOsToDoctors(List<DoctorDTO> doctorDTOS);

    default Doctor doctorFromId(Long id){
        if (id == null) {
            return null;
        }
        Doctor doctor = new Doctor();
        doctor.setId(id);
        return doctor;
    }

}
