package com.zsoft.service;

import com.zsoft.config.Constants;
import com.zsoft.domain.Doctor;
import com.zsoft.domain.Timeslot;
import com.zsoft.domain.User;
import com.zsoft.repository.DoctorRepository;
import com.zsoft.repository.TimeslotRepository;
import com.zsoft.repository.UserRepository;
import com.zsoft.service.dto.DoctorDTO;
import com.zsoft.service.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class DoctorService {

    private final Logger log = LoggerFactory.getLogger(DoctorService.class);

    private final DoctorRepository doctorRepository;

    private final UserRepository userRepository;

    private final TimeslotRepository timeslotRepository;

    public DoctorService(DoctorRepository doctorRepository, UserRepository userRepository, TimeslotRepository timeslotRepository) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.timeslotRepository = timeslotRepository;
    }

    /**
     * Create a new doctor profile, and return the created doctor profile.
     *
     * @param doctorDTO doctor to update
     * @return created doctor
     */
    public DoctorDTO createDoctorProfile(DoctorDTO doctorDTO) {
        System.out.println(doctorDTO);
        doctorDTO.getTimeslots().forEach(System.out::println);
        Doctor doctor = new Doctor();
        // set Profile informations
        doctor.setPhone(doctorDTO.getPhone());
        doctor.setAddress(doctorDTO.getAddress());
        doctor.setGender(doctorDTO.getGender());
        // set profile user
        User user = userRepository.findById(doctorDTO.getUserId()).get();
        doctor.setUser(user);
        // set time slots
        doctor.setTimeslots(doctorDTO.getTimeslots());
        // save the new profile
        doctorRepository.saveAndFlush(doctor);
        log.debug("Created Information for Doctor Profile : {}", doctor);
        return new DoctorDTO(doctor);
    }

    /**
     * Update all information for a specific doctor, and return the modified doctor.
     *
     * @param doctorDTO doctor to update
     * @return updated doctor
     */
    public Optional<DoctorDTO> updateDoctor(DoctorDTO doctorDTO) {
        return Optional.of(doctorRepository
            .findById(doctorDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(doctor -> {
                // set Profile informations
                doctor.setPhone(doctorDTO.getPhone());
                doctor.setAddress(doctorDTO.getAddress());
                doctor.setGender(doctorDTO.getGender());
                // delete old time slots
                doctor.getTimeslots()
                    .stream()
                    .map(Timeslot::getId)
                    .forEach(timeslotRepository::deleteById);
                // create new time slots
                doctor.setTimeslots(doctorDTO.getTimeslots());

                // save the profile
                log.debug("Changed Information for Doctor Profile: {}", doctor);
                return doctor;
            })
            .map(DoctorDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<DoctorDTO> getAllDoctors(Pageable pageable) {
        return doctorRepository.findAll(pageable).map(DoctorDTO::new);
    }

    public Optional<Doctor> find(Long doctor_id) {
        return doctorRepository.findById(doctor_id);
    }
}
