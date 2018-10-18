package com.zsoft;

import com.zsoft.config.ApplicationProperties;
import com.zsoft.config.DefaultProfileUtil;
import com.zsoft.domain.*;
import com.zsoft.repository.AppointmentRepository;
import com.zsoft.repository.AuthorityRepository;
import com.zsoft.repository.DoctorRepository;
import com.zsoft.repository.UserRepository;
import com.zsoft.security.AuthoritiesConstants;
import io.github.jhipster.config.JHipsterConstants;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Date;
import java.sql.Time;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EnableConfigurationProperties({LiquibaseProperties.class, ApplicationProperties.class})
public class ZonlineApp {

    private static final Logger log = LoggerFactory.getLogger(ZonlineApp.class);

    private final Environment env;

    public ZonlineApp(Environment env) {
        this.env = env;
    }

    /**
     * Initializes zonline.
     * <p>
     * Spring profiles can be configured with a program argument --spring.profiles.active=your-active-profile
     * <p>
     * You can find more information on how profiles work with JHipster on <a href="https://www.jhipster.tech/profiles/">https://www.jhipster.tech/profiles/</a>.
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
            log.error("You have misconfigured your application! It should not run " +
                "with both the 'dev' and 'prod' profiles at the same time.");
        }
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)) {
            log.error("You have misconfigured your application! It should not " +
                "run with both the 'dev' and 'cloud' profiles at the same time.");
        }
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ZonlineApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info("\n----------------------------------------------------------\n\t" +
                "Application '{}' is running! Access URLs:\n\t" +
                "Local: \t\t{}://localhost:{}{}\n\t" +
                "External: \t{}://{}:{}{}\n\t" +
                "Profile(s): \t{}\n----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.getActiveProfiles());
    }

    /*
     */
    //@Bean
    ApplicationRunner init(DoctorRepository doctorRepository, UserRepository userRepository, AuthorityRepository authorityRepository){
        return args -> {
            System.out.println("\n\n##################################\n\n");
            System.out.println("DOCTORS : ");
            doctorRepository.findAll().forEach(System.out::println);
            System.out.println("\n\n##################################\n\n");

            if( !userRepository.findOneByLogin("doctor").isPresent() ){
                System.out.println("Doctor User Not Exist");
                User userDoctor = new User();
                userDoctor.setEmail("doctor@localhost");
                userDoctor.setLogin("doctor");
                userDoctor.setActivated(true);
                userDoctor.setPassword(RandomStringUtils.random(60));
                userDoctor.setFirstName("john");
                userDoctor.setLastName("doe");
                userDoctor.setImageUrl("http://placehold.it/50x50");
                userDoctor.setLangKey("en");
                Set<Authority> authorities = new HashSet<>();
                authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
                authorityRepository.findById(AuthoritiesConstants.DOCTOR).ifPresent(authorities::add);
                userDoctor.setAuthorities(authorities);
                userRepository.save(userDoctor);
            }

            User userDoctor = userRepository.findOneByLogin("doctor").get();
            System.out.println(userDoctor);
            if( doctorRepository.findDoctorByUser_Id(userDoctor.getId()).isPresent() ){
                System.out.println("# Doctor Profile is already exsit :");
                doctorRepository.deleteById(doctorRepository.findDoctorByUser_Id(userDoctor.getId()).get().getId());
                System.out.println("# Delete Doctor !");
            }
            if( !doctorRepository.findDoctorByUser_Id(userDoctor.getId()).isPresent() ){

                Set<Timeslot> timeslots = new HashSet<>();
                timeslots.add(new Timeslot(0, Time.valueOf("08:00:00"), Time.valueOf("12:00:00"), true));
                timeslots.add(new Timeslot(1, Time.valueOf("08:00:00"), Time.valueOf("11:30:00"), true));
                timeslots.add(new Timeslot(2, Time.valueOf("08:00:00"), Time.valueOf("12:00:00"), true));
                timeslots.add(new Timeslot(3, Time.valueOf("08:00:00"), Time.valueOf("12:00:00"), true));
                timeslots.add(new Timeslot(4, Time.valueOf("08:00:00"), Time.valueOf("12:00:00"), true));
                timeslots.add(new Timeslot(5, Time.valueOf("08:00:00"), Time.valueOf("12:00:00"), true));
                System.out.println("TIME SLOTS : ");
                timeslots.forEach(System.out::println);

                Doctor doctor = new Doctor();
                doctor.setPhone("001 131 213 266");
                doctor.setAddress("NY , USA");
                doctor.setGender("MALE");
                doctor.setSpeciality("Médcin Générale");
                doctor.setUser(userDoctor);
                doctor.setTimeslots(timeslots);
                System.out.println("# Created Doctor :");
                System.out.println(doctor);
                doctorRepository.saveAndFlush(doctor);
            }

            System.out.println("\n\n##################################\n\n");
            System.out.println("DOCTORS : ");
            doctorRepository.findAll().forEach(System.out::println);
            System.out.println("\n\n##################################\n\n");
        };
    }

    //@Bean
    ApplicationRunner init(DoctorRepository doctorRepository, UserRepository userRepository, AuthorityRepository authorityRepository, AppointmentRepository appointmentRepository){
        return args -> {
            Doctor doctor = doctorRepository.findDoctorByUser_Id(userRepository.findOneByLogin("doctor").get().getId()).get();
            User patient = userRepository.findOneByLogin("user").get();
            Appointment appointment = new Appointment();
            appointment.setDoctor(doctor);
            appointment.setPatient(patient);
            appointment.setDate(Date.valueOf("2018-10-17"));
            appointment.setTimeStart(Time.valueOf("09:30:00"));
            appointment.setTimeEnd(Time.valueOf("10:00:00"));
            appointment.setStatus("FINISHED");
            appointmentRepository.saveAndFlush(appointment);
            System.out.println("\n\n##################################\n\n");
            System.out.println("APPOINTMENTS : ");
            appointmentRepository.findAll().forEach(System.out::println);
            System.out.println("\n\n##################################\n\n");
        };
    }
}
