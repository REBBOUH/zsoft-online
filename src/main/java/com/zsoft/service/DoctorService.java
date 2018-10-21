package com.zsoft.service;

import com.zsoft.domain.Doctor;
import com.zsoft.domain.Timeslot;
import com.zsoft.domain.User;
import com.zsoft.repository.DoctorRepository;
import com.zsoft.repository.TimeslotRepository;
import com.zsoft.repository.UserRepository;
import com.zsoft.service.dto.DoctorDTO;
import com.zsoft.service.dto.TimeslotDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
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
     * @param doctorDTO doctor profile to update
     * @return created doctor profile
     */
    public DoctorDTO createDoctorProfile(DoctorDTO doctorDTO) {
        Doctor doctor = new Doctor();
        // set Profile informations
        doctor.setPhone(doctorDTO.getPhone());
        doctor.setAddress(doctorDTO.getAddress());
        doctor.setGender(doctorDTO.getGender());
        doctor.setSpeciality(doctorDTO.getSpeciality());
        // set profile user
        User user = userRepository.findById(doctorDTO.getUserId()).get();
        doctor.setUser(user);
        // set time slots
        Set<Timeslot> timeslots = new HashSet<>();
        for (TimeslotDTO timeslot: doctorDTO.getTimeslots()) {
            timeslots.add(timeslot.toTimeslot());
        }
        doctor.setTimeslots(timeslots);
        // save the new profile
        doctorRepository.saveAndFlush(doctor);
        log.debug("Created Information for Doctor Profile : {}", doctor);
        return new DoctorDTO(doctor);
    }

    /**
     * Update all information for a specific doctor profile, and return the modified doctor profile.
     *
     * @param doctorDTO doctor to update
     * @return updated doctor profile
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
                doctor.setSpeciality(doctorDTO.getSpeciality());
                // delete old time slots
                timeslotRepository.deleteTimeslotByDoctorId(doctorDTO.getId());
                // create new time slots
                Set<Timeslot> timeslots = new HashSet<>();
                for (TimeslotDTO timeslot: doctorDTO.getTimeslots()) {
                    timeslots.add(timeslot.toTimeslot());
                }
                doctor.setTimeslots(timeslots);
                // save the profile
                log.debug("Changed Information for Doctor Profile: {}", doctor);
                return doctor;
            })
            .map(DoctorDTO::new);
    }


    /**
     * get all doctors profile.
     *
     * @return doctors profile
     */
    @Transactional(readOnly = true)
    public Page<DoctorDTO> getAllDoctors(Pageable pageable) {
        return doctorRepository.findAll(pageable).map(DoctorDTO::new);
    }


    /**
     * find a specific doctor profile by doctor_id.
     *
     * @param doctor_id doctor to fetch
     * @return doctor profile
     */
    public Optional<Doctor> find(Long doctor_id) {
        return doctorRepository.findById(doctor_id);
    }


    /**
     * find a specific doctor profile by user_id.
     *
     * @param user_id doctor to fetch
     * @return doctor profile
     */
    public Optional<Doctor> findByUserId(Long user_id) {
        return doctorRepository.findDoctorByUser_Id(user_id);
    }
}
