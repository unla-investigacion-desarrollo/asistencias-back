package com.unla.asistencias.services.implementation;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.unla.asistencias.services.IMailService;

@Service("emailService")
public class MailService implements IMailService {

    private final JavaMailSender javaMailSender;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String from, String to, String subject, String body) {

        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setFrom(from);
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(body);

        javaMailSender.send(mail);
    }
}