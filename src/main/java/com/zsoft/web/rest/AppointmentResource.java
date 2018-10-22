package com.zsoft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.zsoft.domain.Appointment;
import com.zsoft.security.AuthoritiesConstants;
import com.zsoft.service.AppointmentService;
import com.zsoft.service.dto.AppointmentDTO;
import com.zsoft.web.rest.errors.BadRequestAlertException;
import com.zsoft.web.rest.util.HeaderUtil;
import com.zsoft.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
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
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * REST controller for managing appointments.
 */
@RestController
@RequestMapping("/api")
public class AppointmentResource {
    private final Logger log = LoggerFactory.getLogger(AppointmentResource.class);

    private final AppointmentService appointmentService;

    public AppointmentResource(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * POST  /appointments  : Creates a new Appointment.
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
    public ResponseEntity<Appointment> takeAppointment(@Valid @RequestBody AppointmentDTO appointmentDTO) throws URISyntaxException {
        log.debug("REST request to save Appointment : {}", appointmentDTO);
        Appointment newAppointment = appointmentService.createAppointment(appointmentDTO);
        return ResponseEntity.created(new URI("/api/appointments"))
            .headers(HeaderUtil.createAlert("appointment.messages.created", newAppointment.getDate().toString()))
            .body(newAppointment);
    }

    /**
     * PUT /users : Updates an existing Appointment.
     *
     * @param appointmentDTO the appointment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated appointment
     */
    @PutMapping("/appointments")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<AppointmentDTO> updateAppointment(@Valid @RequestBody AppointmentDTO appointmentDTO) {
        log.debug("REST request to update Appointment : {}", appointmentDTO);
        Optional<AppointmentDTO> updatedAppointment = appointmentService.updateAppointment(appointmentDTO);

        return ResponseUtil.wrapOrNotFound(updatedAppointment,
            HeaderUtil.createAlert("appointment.messages.updated", appointmentDTO.getId().toString()));
    }

    /**
     * DELETE /appointments/:appointment_id : delete an Appointment.
     *
     * @param appointment_id the id of the appointment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/appointments/{appointment_id}")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long appointment_id) {
        log.debug("REST request to cancel an Appointment: {}", appointment_id);
        appointmentService.cancelAppointment(appointment_id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "appointment.messages.canceled", appointment_id.toString())).build();
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
        final Page<AppointmentDTO> page = appointmentService.getAllAppointment(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/appointments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET /appointments/:appointment_id : get the Appointment by ID.
     *
     * @param appointment_id the ID of the Appointment to find
     * @return the ResponseEntity with status 200 (OK) and with body the "id" doctor profile, or with status 404 (Not Found)
     */
    @GetMapping("/appointments/{appointment_id}")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<AppointmentDTO> findAppointment(@PathVariable Long appointment_id) {
        log.debug("REST request to get Appointment : {}", appointment_id);
        Optional<Appointment> oap = appointmentService.find(appointment_id);
        System.out.println(oap.get());
        return ResponseUtil.wrapOrNotFound(oap.map(AppointmentDTO::new));
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
        final Page<AppointmentDTO> page = appointmentService.getAllAppointmentOfDoctor(pageable, doctor_user_id);
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
        final Page<AppointmentDTO> page = appointmentService.getAllAppointmentOfPatient(pageable, patient_id);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/appointments/doctor/"+patient_id);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
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
            .collect(Collectors.toList());
    }

    /**
     * GET /appointments : get appointments of doctor between two dates.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all appointments
     */
    @GetMapping("/appointments/available/{doctor_id}/{date}")
    @Timed
    public List<AppointmentDTO> getAvailableAppointmentsOfDoctorByDate(@PathVariable Long doctor_id, @PathVariable Date date) {
        final List<AppointmentDTO> appointments = appointmentService.getAvailableAppointmentsOfDoctorByDate(doctor_id, date);
        return appointments;
    }
}
