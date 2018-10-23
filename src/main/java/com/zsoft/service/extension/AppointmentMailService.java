package com.zsoft.service.extension;

import com.zsoft.domain.User;
import com.zsoft.domain.extension.Appointment;
import com.zsoft.service.MailService;
import io.github.jhipster.config.JHipsterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.text.SimpleDateFormat;
import java.util.Locale;

@Service
public class AppointmentMailService extends MailService{
    private final Logger log = LoggerFactory.getLogger(AppointmentMailService.class);

    private final JHipsterProperties jHipsterProperties;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    public AppointmentMailService(
        JHipsterProperties jHipsterProperties,
        JavaMailSender javaMailSender,
        MessageSource messageSource,
        SpringTemplateEngine templateEngine
    ) {
        super(jHipsterProperties, javaMailSender, messageSource, templateEngine);
        this.jHipsterProperties = jHipsterProperties;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendReminderMail(Appointment appointment) {
        log.debug("Sending an appointment reminder email to '{}'", appointment.getPatient().getEmail());
        User patient = appointment.getPatient();
        String templateName = "mail/reminderEmail";
        String title = "email.reminder.title";
        Locale locale = Locale.forLanguageTag(patient.getLangKey());
        Context context = new Context(locale);
        context.setVariable("patient", patient);
        context.setVariable("doctor", appointment.getDoctor());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        context.setVariable("date", sdf.format(appointment.getDate()));
        sdf = new SimpleDateFormat("HH:mm");
        context.setVariable("timeStart", sdf.format(appointment.getTimeStart()));
        context.setVariable("timeEnd", sdf.format(appointment.getTimeEnd()));
        context.setVariable("baseUrl", jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(title, null, locale);
        sendEmail(patient.getEmail(), subject, content, false, true);
    }
}
