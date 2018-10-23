package com.zsoft.service.extension;

import com.zsoft.domain.extension.Doctor;
import com.zsoft.repository.UserRepository;
import com.zsoft.repository.extension.DoctorRepository;
import com.zsoft.service.dto.extension.DoctorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class DoctorService {

    private final Logger log = LoggerFactory.getLogger(DoctorService.class);

    private final DoctorRepository doctorRepository;

    private final UserRepository userRepository;

    public DoctorService(
        DoctorRepository doctorRepository,
        UserRepository userRepository
    ) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
    }

    /**
     * Create a new doctor profile, and return the created doctor profile.
     *
     * @param doctorDTO doctor profile to update
     * @return created doctor profile
     */
    public Doctor createDoctorProfile(DoctorDTO doctorDTO) {
        if( !userRepository.findById(doctorDTO.getUserId()).isPresent() ){
            throw new IllegalArgumentException("Illegal Arguments, User Not Found !");
        }
        log.debug("Created Information for Doctor Profile : {}", doctorDTO);
        return doctorRepository.saveAndFlush(doctorDTO.toDoctor());
    }

    /**
     * Update all information for a specific doctor profile, and return the modified doctor profile.
     *
     * @param doctorDTO doctor to update
     * @return updated doctor profile
     */
    public Optional<DoctorDTO> updateDoctor(DoctorDTO doctorDTO) {
        if( !userRepository.findById(doctorDTO.getUserId()).isPresent() ){
            throw new IllegalArgumentException("Illegal Arguments, User Not Found !");
        }
        return doctorRepository.findById(doctorDTO.getId())
            .map(doctor -> {
                log.debug("Changed Information for Doctor Profile: {}", doctorDTO);
                return doctorRepository.saveAndFlush(doctorDTO.toDoctor(doctor));
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
