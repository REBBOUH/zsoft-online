package com.zsoft.service.extension;

import com.zsoft.config.extension.ExtenstionConstant;
import com.zsoft.domain.User;
import com.zsoft.domain.extension.Appointment;
import com.zsoft.domain.extension.AppointmentStatus;
import com.zsoft.domain.extension.PersistentConfiguration;
import com.zsoft.repository.UserRepository;
import com.zsoft.repository.extension.AppointmentRepository;
import com.zsoft.service.dto.extension.AppointmentDTO;
import com.zsoft.service.dto.extension.UserMinDTO;
import com.zsoft.service.mapper.extension.AppointmentMapper;
import com.zsoft.service.mapper.extension.UserMinMapper;
import com.zsoft.service.util.AppointmentUtil;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class UserMinService {

    private final Logger log = LoggerFactory.getLogger(DoctorService.class);

    private final UserRepository userRepository;

    private UserMinMapper mapper = Mappers.getMapper(UserMinMapper.class);

    public UserMinService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Optional<UserMinDTO> find(Long user_id) {
        return userRepository.findById(user_id).map(mapper::toDto);
    }
}
