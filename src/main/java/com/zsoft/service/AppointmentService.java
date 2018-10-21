package com.zsoft.service;

import com.zsoft.config.Constants;
import com.zsoft.domain.Appointment;
import com.zsoft.domain.Doctor;
import com.zsoft.domain.Timeslot;
import com.zsoft.domain.User;
import com.zsoft.repository.AppointmentRepository;
import com.zsoft.repository.DoctorRepository;
import com.zsoft.repository.TimeslotRepository;
import com.zsoft.repository.UserRepository;
import com.zsoft.service.dto.AppointmentDTO;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppointmentService {

    private final Logger log = LoggerFactory.getLogger(DoctorService.class);

    private final DoctorRepository doctorRepository;

    private final UserRepository userRepository;

    private final AppointmentRepository appointmentRepository;

    private final TimeslotRepository timeslotRepository;

    private final MailService mailService;

    public AppointmentService(
        DoctorRepository doctorRepository,
        UserRepository userRepository,
        AppointmentRepository appointmentRepository,
        TimeslotRepository timeslotRepository,
        MailService mailService
    ) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.timeslotRepository = timeslotRepository;
        this.mailService = mailService;
    }

    public Appointment createAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = new Appointment();
        appointment.setDate(appointmentDTO.getDate());
        appointment.setTimeStart(Time.valueOf(appointmentDTO.getTimeStart()));
        appointment.setTimeEnd(Time.valueOf(appointmentDTO.getTimeEnd()));
        appointment.setStatus(appointmentDTO.getStatus());

        Doctor doctor = doctorRepository.findById(appointmentDTO.getDoctor().getId()).get();
        appointment.setDoctor(doctor);
        User patient = userRepository.findById(appointmentDTO.getPatient().getId()).get();
        appointment.setPatient(patient);

        log.debug("Created Information for Appointment : {}", appointment);
        appointmentRepository.saveAndFlush(appointment);
        return appointment;
    }

    public Optional<AppointmentDTO> updateAppointment(AppointmentDTO appointmentDTO) {
        return Optional.of(appointmentRepository.findById(appointmentDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(appointment -> {
                appointment.setDate(appointmentDTO.getDate());
                appointment.setTimeStart(Time.valueOf(appointmentDTO.getTimeStart()));
                appointment.setTimeEnd(Time.valueOf(appointmentDTO.getTimeEnd()));

                Doctor doctor = doctorRepository.findById(appointmentDTO.getDoctor().getId()).get();
                appointment.setDoctor(doctor);
                User patient = userRepository.findById(appointmentDTO.getPatient().getId()).get();
                appointment.setPatient(patient);

                appointment.setStatus(appointmentDTO.getStatus());
                log.debug("Changed Information for Appointment: {}", appointment);
                return appointment;
            })
            .map(AppointmentDTO::new);
    }

    public void cancelAppointment(Long id) {
        Optional.of(appointmentRepository.findById(id))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(appointment -> {
                appointment.setStatus("Canceled");
                log.debug("Cancel an Appointment: {}", appointment);
                return appointment;
            })
            .map(AppointmentDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<AppointmentDTO> getAllAppointment(Pageable pageable) {
        return appointmentRepository.findAll(pageable).map(AppointmentDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<AppointmentDTO> getAllAppointmentOfDoctor(Pageable pageable, Long doctor_user_id) {
        return appointmentRepository.findAppointmentsByDoctor_User_Id(pageable, doctor_user_id).map(AppointmentDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<AppointmentDTO> getAllAppointmentOfPatient(Pageable pageable, Long patient_id) {
        return appointmentRepository.findAppointmentsByPatient_Id(pageable, patient_id).map(AppointmentDTO::new);
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAppointmentsOfDoctorByDate(Long doctor_id, Date date) {
        List<Appointment> appointments = appointmentRepository.findAppointmentsByDoctor_IdAndDate(doctor_id, date);
        return appointments.stream().map(AppointmentDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentDTO> getAvailableAppointmentsOfDoctorByDate(Long doctor_id, Date date) {
        // Get Day of week for this date
        int dayOfWeek = date.toLocalDate().getDayOfWeek().getValue();
        if( dayOfWeek == 7 ) dayOfWeek = 0;
        // Get time slots of this date and this doctor
        List<Timeslot> timeslots = timeslotRepository.findTimeslotsByDoctorIdAndDayOfWeek(doctor_id, dayOfWeek);
        // Get appointments of this date and this doctor
        List<Appointment> appointments = appointmentRepository.findAppointmentsByDoctor_IdAndDateAndStatusNot(doctor_id, date, "Canceled");
        List<Appointment> appointmentsAvailable = new ArrayList<>();
        timeslots.forEach(
            timeslot -> {
                long fullTime = Duration.between(timeslot.getTimeStart().toLocalTime(), timeslot.getTimeEnd().toLocalTime()).toMinutes();
                int nbrOfSlots = (int)(fullTime/ Constants.SLOT_LENGTH);
                LocalTime time = timeslot.getTimeStart().toLocalTime();
                for (int i = 0; i < nbrOfSlots; i++ ){
                    Appointment appointment = new Appointment();
                    appointment.setTimeStart(Time.valueOf(time));
                    appointment.setTimeEnd(Time.valueOf(time.plusMinutes(Constants.SLOT_LENGTH)));
                    for( Appointment ap: appointments) {
                        if( !appointment.isBetween(ap.getTimeStart(), ap.getTimeEnd()) ){
                            appointment.setStatus("Available");
                        }else{
                            appointment.setStatus("Occuped");
                            break;
                        }
                    }
                    appointmentsAvailable.add(appointment);
                    time = time.plusMinutes(Constants.SLOT_LENGTH);
                }
            }
        );

        // Fill the gaps (Set times between time slot)
        // Sort TimeSlot List By time start asc
        timeslots.sort((t1, t2) -> t1.getTimeStart().before(t2.getTimeStart())?-1:1);
        Iterator<Timeslot> timeslotIterator = timeslots.iterator();
        if( timeslotIterator.hasNext() ){
            Timeslot ts = timeslotIterator.next();
            Timeslot nts;
            while( timeslotIterator.hasNext() ){
                nts = timeslotIterator.next();
                if( nts.getTimeStart().after(ts.getTimeEnd()) ) {
                    appointmentsAvailable.add(new Appointment(null, null, null, ts.getTimeEnd(), nts.getTimeStart(), "Occuped"));
                }
                ts = nts;
            }
            // Sort Available Appointments By time start asc
            appointmentsAvailable.sort((a1, a2) -> a1.getTimeStart().before(a2.getTimeStart())?-1:1);
        }

        return appointmentsAvailable.stream().map(AppointmentDTO::new).collect(Collectors.toList());
    }

    public Optional<Appointment> find(Long appointment_id) {
        return appointmentRepository.findAppointmentsById(appointment_id);
    }

    /**
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void appointmentScheduled(){
        changeAppointmentsStatus();
        sendReminderAppointmentsEmails();
    }

    private void changeAppointmentsStatus(){
        // Load Old Appointments with Status 'Panding'
        // Update Status of Appointments to 'Passed'
        Date today = new Date(Calendar.getInstance().getTime().getTime());
        appointmentRepository
            .findAppointmentsByDateBeforeAndStatus(today, "Panding")
            .forEach(
                appointment -> {
                    appointment.setStatus("Passed");
                    log.debug("Passed an Appointment: {}", appointment);
                    appointmentRepository.saveAndFlush(appointment);
                });
    }

    private void sendReminderAppointmentsEmails(){
        Date today = new Date(Calendar.getInstance().getTime().getTime());
        appointmentRepository
            .findAppointmentsByDateAndStatus(today, "Panding")
            .forEach(mailService::sendReminderMail);
    }
}
