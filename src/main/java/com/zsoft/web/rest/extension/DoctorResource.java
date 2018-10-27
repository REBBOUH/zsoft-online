package com.zsoft.web.rest.extension;

import com.codahale.metrics.annotation.Timed;
import com.zsoft.domain.extension.Doctor;
import com.zsoft.domain.extension.PersistentConfiguration;
import com.zsoft.repository.extension.DoctorRepository;
import com.zsoft.service.dto.extension.DoctorDTO;
import com.zsoft.service.extension.DoctorService;
import com.zsoft.service.mapper.extension.DoctorMapper;
import com.zsoft.web.rest.errors.BadRequestAlertException;
import com.zsoft.web.rest.util.HeaderUtil;
import com.zsoft.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing doctors.
 */
@RestController
@RequestMapping("/api")
public class DoctorResource {

    private final Logger log = LoggerFactory.getLogger(DoctorResource.class);

    private final DoctorService doctorService;

    private final DoctorRepository doctorRepository;

    private final DoctorMapper mapper = Mappers.getMapper(DoctorMapper.class);

    public DoctorResource(DoctorService doctorService, DoctorRepository doctorRepository) {
        this.doctorService = doctorService;
        this.doctorRepository = doctorRepository;
    }

    /**
     * POST  /doctors  : Create a new Doctor Profile.
     * <p>
     * Creates a new doctor Profile if the user has ROLE_DOCTOR and Profile Not already exist
     * Create multi time slots linked to the doctor profile
     *
     * @param doctorDTO the doctor profile to create
     * @return the ResponseEntity with status 201 (Created) and with body the new doctor profile, or with status 400 (Bad Request) if the doctor profile is already in use
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws BadRequestAlertException 400 (Bad Request) if the doctor profile is already in use
     */
    @PostMapping("/doctors")
    @Timed
    public ResponseEntity<DoctorDTO> createDoctorProfile(@Valid @RequestBody DoctorDTO doctorDTO) throws URISyntaxException {
        log.debug("REST request to create a new Doctor Profile : {}", doctorDTO);
        if (doctorDTO.getId() != null || doctorRepository.findDoctorByUser_Id(doctorDTO.getUserId()).isPresent()) {
            throw new BadRequestAlertException("Doctor profile Already exist !", "doctor.messages", "idexists");
        } else {
            Doctor newDoctor = doctorService.createDoctorProfile(doctorDTO);
            return ResponseEntity.created(new URI("/api/doctors/profile"))
                .headers(HeaderUtil.createAlert("doctor.messages.created", newDoctor.getId().toString()))
                .body(mapper.toDto(newDoctor));
        }
    }


    /**
     * PUT /doctors : Update an existing Doctor Profile.
     *
     * @param doctorDTO the Doctor Profile to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated doctor profile
     */
    @PutMapping("/doctors")
    @Timed
    public ResponseEntity<DoctorDTO> updateDoctorProfile(@Valid @RequestBody DoctorDTO doctorDTO) {
        log.debug("REST request to update Doctor Profile : {}", doctorDTO);
        if ( doctorDTO.getId() == null || !doctorRepository.findById(doctorDTO.getId()).isPresent()) {
            throw new BadRequestAlertException("Doctor profile not found", "doctor.messages", "notexist");
        } else {
            Optional<DoctorDTO> updatedDoctor = doctorService.updateDoctor(doctorDTO);
            return ResponseUtil.wrapOrNotFound(updatedDoctor,
                HeaderUtil.createAlert("doctor.messages.updated", doctorDTO.getId().toString()));
        }
    }

    /**
     * GET /doctors : get all doctors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all doctors
     */
    @GetMapping("/doctors")
    @Timed
    public ResponseEntity<List<DoctorDTO>> getAllDoctorsProfile(Pageable pageable) {
        final Page<DoctorDTO> page = doctorService.getAllDoctors(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/doctors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /doctors/:doctor_id : get the "id" doctor profile.
     *
     * @param doctor_id the identify of the doctor profile to find
     * @return the ResponseEntity with status 200 (OK) and with body the "id" doctor profile, or with status 404 (Not Found)
     */
    @GetMapping("/doctors/{doctor_id}")
    @Timed
    public ResponseEntity<DoctorDTO> findDoctorProfile(@PathVariable Long doctor_id) {
        log.debug("REST request to get Doctor Profile : {}", doctor_id);
        return ResponseUtil.wrapOrNotFound(
            doctorService
                .find(doctor_id)
                .map(mapper::toDto)
        );
    }

    /**
     * GET /doctors/user/:user_id : get the "user_id" doctor profile.
     *
     * @param user_id the identify of the doctor profile to find
     * @return the ResponseEntity with status 200 (OK) and with body the "id" doctor profile, or with status 404 (Not Found)
     */
    @GetMapping("/doctors/user/{user_id}")
    @Timed
    public ResponseEntity<DoctorDTO> findDoctorProfileByUserId(@PathVariable Long user_id) {
        log.debug("REST request to get Doctor Profile : {}", user_id);
        return ResponseUtil.wrapOrNotFound(
            doctorService
                .findByUserId(user_id)
                .map(mapper::toDto)
        );
    }


    /**
     * PUT /doctors : Update a Doctor Configuration.
     *
     * @param doctor_id the Doctor Profile ID
     * @param configurations the list of configuration to update
     * @return the ResponseEntity with status 200 (OK)
     */
    @PutMapping("/doctors/configuration/{doctor_id}")
    @Timed
    public ResponseEntity<Void> updateDoctorConfigurations(@PathVariable Long doctor_id, @Valid @RequestBody List<PersistentConfiguration> configurations) {
        log.debug("REST request to update Doctor Configuration : {}", doctor_id);
        if ( doctor_id == null || !doctorRepository.findById(doctor_id).isPresent()) {
            throw new BadRequestAlertException("Doctor profile not found", "doctor.messages", "notexist");
        } else {
            // delete old and create new configurations
            doctorService.updateConfigurations(doctor_id, configurations);
            return ResponseEntity.ok().headers(HeaderUtil.createAlert( "doctor.messages.updateConfigurations", doctor_id.toString())).build();
        }
    }

    /**
     * GET /doctors/configuration/:doctor_id : get the "id" doctor profile.
     *
     * @param doctor_id the doctor profile id
     * @return the ResponseEntity with status 200 (OK) and with body all doctor configurations
     */
    @GetMapping("/doctors/configuration/{doctor_id}")
    @Timed
    public List<PersistentConfiguration> getDoctorConfiguration(@PathVariable Long doctor_id) {
        log.debug("REST request to get Doctor Profile : {}", doctor_id);
        return doctorService.getDoctorConfigurations(doctor_id);
    }
}
