package com.zsoft.service.util;

import com.zsoft.config.extension.ExtenstionConstant;
import com.zsoft.domain.extension.Appointment;
import com.zsoft.domain.extension.AppointmentStatus;
import com.zsoft.service.mapper.extension.DoctorMapper;
import org.mapstruct.factory.Mappers;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AppointmentUtil {
    private static final DoctorMapper doctorMapper = Mappers.getMapper(DoctorMapper.class);

    /*
     * HELPER FUNCTION : CREATE APPOINTMENTS LIST
     * @Params: doctor id, date, start time & end time
     */
    public static List<Appointment> createAppointments(Long doctor_id, Date date, Time timeStart, Time timeEnd){
        long fullTime = Duration.between(timeStart.toLocalTime(), timeEnd.toLocalTime()).toMinutes();
        int nbrOfSlots = (int)(fullTime/ ExtenstionConstant.SLOT_LENGTH);
        LocalTime time = timeStart.toLocalTime();
        List<Appointment> appointments = new ArrayList<>();
        for (int i = 0; i < nbrOfSlots; i++ ){
            Appointment appointment = new Appointment(
                doctorMapper.doctorFromId(doctor_id),
                null,
                AppointmentStatus.FREE,
                date,
                Time.valueOf(time),
                Time.valueOf(time.plusMinutes(ExtenstionConstant.SLOT_LENGTH))
            );
            appointments.add(appointment);
            time = time.plusMinutes(ExtenstionConstant.SLOT_LENGTH);
        }
        return appointments;
    }

    /*
     * Helper function: Mapped next X days with day of week
     */
    public static Map<Integer, List<LocalDate>> getDatesForNextDays(int days){
        LocalDate today = LocalDate.now();
        Map<Integer, List<LocalDate>> map = new HashMap<>();
        for (int i = 0; i < days; i++) {
            LocalDate day = today.plusDays(i);
            int currentDayOfWeek = day.getDayOfWeek().getValue();
            map.putIfAbsent(currentDayOfWeek, new ArrayList<>());
            map.get(currentDayOfWeek).add(day);
        }
        return map;
    }
}
