package com.zsoft.service.extension;

import com.zsoft.domain.extension.Doctor;
import com.zsoft.domain.extension.PersistentConfiguration;
import com.zsoft.repository.UserRepository;
import com.zsoft.repository.extension.DoctorRepository;
import com.zsoft.service.dto.extension.DoctorDTO;
import com.zsoft.service.mapper.extension.DoctorMapper;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class DoctorService {

    private final Logger log = LoggerFactory.getLogger(DoctorService.class);

    private final DoctorRepository doctorRepository;

    private final UserRepository userRepository;

    private final PersistentConfigurationService configurationService;

    private final AppointmentService appointmentService;

    private final DoctorMapper mapper = Mappers.getMapper(DoctorMapper.class);

    public DoctorService(
        DoctorRepository doctorRepository,
        PersistentConfigurationService configurationService,
        AppointmentService appointmentService,
        UserRepository userRepository
    ) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.configurationService = configurationService;
        this.appointmentService = appointmentService;
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
        return doctorRepository.saveAndFlush(mapper.toEntity(doctorDTO));
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
            .map(currentDoctor -> {
                log.debug("Changed Information for Doctor Profile: {}", doctorDTO);
                Doctor doctor = mapper.toEntity(doctorDTO);
                doctor.setAppointments(currentDoctor.getAppointments());
                doctor.setId(currentDoctor.getId());
                return doctorRepository.saveAndFlush(doctor);
            })
            .map(mapper::toDto);
    }


    /**
     * get all doctors profile.
     *
     * @return doctors profile
     */
    @Transactional(readOnly = true)
    public Page<DoctorDTO> getAllDoctors(Pageable pageable) {
        return doctorRepository
            .findAll(pageable)
            .map(mapper::toDto);
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


    /**
     * Update all configurations for a specific doctor profile.
     */
    public void updateConfigurations(Long doctor_id, List<PersistentConfiguration> configurations) {
        log.debug("Changed configurations for Doctor Profile: {}", doctor_id);
        // RESET CONFIGURATIONS
        configurationService.deleteByEntityAndEntityId("DOCTOR", doctor_id);
        configurations.forEach(configurationService::add);
        // RESET DOCTOR APPOINTMENTS
        appointmentService.resetNextAppointments(doctor_id);
    }

    public List<PersistentConfiguration> getDoctorConfigurations(Long doctor_id) {
        List<PersistentConfiguration> result = new ArrayList<>();
        Stream<PersistentConfiguration> configurations = configurationService.getByEntityAndEntityId("DOCTOR", doctor_id);
        configurations.forEach(conf -> {
            result.add(conf);
            configurationService
                .getByEntityAndEntityId(conf.getKey(), Long.parseLong(conf.getValue()))
                .forEach(result::add);
        });
        return result;
    }
}
