package com.zsoft.web.rest.extension;

import com.codahale.metrics.annotation.Timed;
import com.zsoft.security.AuthoritiesConstants;
import com.zsoft.service.dto.extension.AppointmentDTO;
import com.zsoft.service.extension.AppointmentService;
import com.zsoft.service.mapper.extension.AppointmentMapper;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing appointments.
 */
@RestController
@RequestMapping("/api")
public class AppointmentResource {
    private final Logger log = LoggerFactory.getLogger(AppointmentResource.class);

    private final AppointmentService appointmentService;

    private AppointmentMapper mapper = Mappers.getMapper(AppointmentMapper.class);

    public AppointmentResource(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * POST  /appointments  : Create a new Appointment.
     * <p>
     * Creates a new appointment if the user has ROLE_PATIENT
     *
     * @param appointmentDTO the appointment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new appointment, or with status 400 (Bad Request) if the appointment is already in use
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws BadRequestAlertException 400 (Bad Request) if the appointment is already in use
     */
    @PostMapping("/appointments")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<AppointmentDTO> takeAppointment(@Valid @RequestBody AppointmentDTO appointmentDTO) throws URISyntaxException {
        log.debug("REST request to save Appointment : {}", appointmentDTO);
        AppointmentDTO newAppointment = appointmentService.takeAppointment(appointmentDTO);
        return ResponseEntity.created(new URI("/api/appointments"))
            .headers(HeaderUtil.createAlert("appointment.messages.created", newAppointment.getDate().toString()))
            .body(newAppointment);
    }

    /**
     * PUT /users : Update an existing Appointment.
     *
     * @param appointmentDTO the appointment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated appointment
     */
    @PutMapping("/appointments/{old_appointment_id}")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<AppointmentDTO> updateAppointment(@Valid @RequestBody AppointmentDTO appointmentDTO, @PathVariable Long old_appointment_id) throws URISyntaxException  {
        log.debug("REST request to update Appointment : {}", appointmentDTO);
        AppointmentDTO updatedAppointment = appointmentService.updateAppointment(appointmentDTO, old_appointment_id);
        return ResponseEntity.created(new URI("/api/appointments"))
            .headers(HeaderUtil.createAlert("appointment.messages.updated", old_appointment_id.toString()))
            .body(updatedAppointment);
    }

    /**
     * DELETE /appointments/:appointment_id : cancel an Appointment.
     *
     * @param appointment_id the id of the appointment to cancel
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/appointments/{appointment_id}")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long appointment_id) {
        log.debug("REST request to cancel an Appointment: {}", appointment_id);
        appointmentService.cancelAppointment(appointment_id);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert( "appointment.messages.canceled", appointment_id.toString()))
            .build();
    }

    /**
     * GET /appointments : get all appointments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all appointments
     */
    @GetMapping("/appointments")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments(Pageable pageable) {
        final Page<AppointmentDTO> page = appointmentService
            .getAllAppointment(pageable)
            .map(mapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/appointments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /appointments/:appointment_id : get an Appointment by ID.
     *
     * @param appointment_id the ID of the Appointment to find
     * @return the ResponseEntity with status 200 (OK) and with body the "id" doctor profile, or with status 404 (Not Found)
     */
    @GetMapping("/appointments/{appointment_id}")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<AppointmentDTO> findAppointment(@PathVariable Long appointment_id) {
        log.debug("REST request to get Appointment : {}", appointment_id);
        return ResponseUtil.wrapOrNotFound(
            appointmentService.find(appointment_id)
                .map(mapper::toDto)
        );
    }

    /**
     * GET /appointments : get appointments of doctor.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all appointments
     */
    @GetMapping("/appointments/doctor/{doctor_user_id}")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.DOCTOR + "\")")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsOfDoctor(Pageable pageable, @PathVariable Long doctor_user_id) {
        final Page<AppointmentDTO> page = appointmentService
            .getAllAppointmentOfDoctor(pageable, doctor_user_id)
            .map(mapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/appointments/doctor/"+doctor_user_id);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /appointments : get appointments of doctor.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all appointments
     */
    @GetMapping("/appointments/patient/{patient_id}")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsOfPatient(Pageable pageable, @PathVariable Long patient_id) {
        final Page<AppointmentDTO> page = appointmentService
            .getAllAppointmentOfPatient(pageable, patient_id)
            .map(mapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/appointments/doctor/"+patient_id);
        return new ResponseEntity<>(
            page.getContent(),
            headers,
            HttpStatus.OK
        );
    }

    /**
     * GET /appointments : get appointments of doctor between two dates.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all appointments
     */
    @GetMapping("/appointments/doctor/{doctor_id}/{date}")
    @Timed
    public List<AppointmentDTO> getAppointmentsOfDoctorByDate(@PathVariable Long doctor_id, @PathVariable Date date) {
        return appointmentService
            .getAppointmentsOfDoctorByDate(doctor_id, date)
            .stream()
            .map(mapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * GET /appointments : get appointments of doctor between two dates.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all appointments
     */
    @GetMapping("/appointments/available/{doctor_id}/{date}")
    @Timed
    public List<AppointmentDTO> getAvailableAppointmentsOfDoctorByDate(@PathVariable Long doctor_id,@Valid @PathVariable Date date) {
        return appointmentService
            .getAvailableAppointmentsOfDoctorByDate(doctor_id, date)
            .stream()
            //.filter(appointment -> appointment.getDate().getTime() > System.currentTimeMillis() )
            .map(mapper::toDto)
            .collect(Collectors.toList());
    }
}
