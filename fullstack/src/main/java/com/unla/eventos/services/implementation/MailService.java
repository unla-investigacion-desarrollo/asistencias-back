package com.unla.eventos.services.implementation;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.unla.eventos.helpers.FunctionsHelper;
import com.unla.eventos.helpers.MailConfigHelper;
import com.unla.eventos.services.IMailService;

@Service("emailService")
@Slf4j
public class MailService implements IMailService {

	@Autowired
    private QRCodeService qrCodeService;
	
    private final JavaMailSender mailSender;

    private final SpringTemplateEngine templateEngine;

    public MailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void prepareAndSendEmail(String qRCode, String name, String lastName, String eventName,
    		LocalDateTime eventStartDate, LocalDateTime eventEndDate, String mailContact, String assistanceResponseEmail) throws Exception{
    	byte[] qrCodeBytes = qrCodeService.generateQRCodeBytes(qRCode, 300, 300);
        Map<String, Object> message = new HashMap<>();
        message.put("name", name);
        message.put("lastName", lastName);
        message.put("eventName", eventName);
        message.put("eventStartDate", FunctionsHelper.formatLocalDateToARGTime(eventStartDate));
        message.put("eventEndDate", FunctionsHelper.formatLocalDateToARGTime(eventEndDate));
        message.put("mailContact", mailContact);
        this.sendEmail(assistanceResponseEmail, "Confirmación de registro a evento (UNLa)", message, qrCodeBytes);
    }
    
    @Override
    public void sendEmail(String toUser, String subject, Map<String, Object> message, byte[] qrCodeBytes)
            throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try{
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(toUser);
            mimeMessageHelper.setSubject(subject);

            Context context = new Context();
            context.setVariables(message);

            String htmlContent = templateEngine.process(MailConfigHelper.TEMPLATE_REGISTER_USER, context);
            mimeMessageHelper.setText(htmlContent,true);

            ClassPathResource resource = new ClassPathResource("/static/images/logo.png");
            mimeMessageHelper.addInline("logoImage", resource);

            // Adjuntar el QR Code
            ByteArrayResource qrCodeResource = new ByteArrayResource(qrCodeBytes);
            mimeMessageHelper.addAttachment("qrcode.png", qrCodeResource);
            
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("ERROR en el envio de mail");
            throw new MessagingException(e.getMessage());
        }
    }

    @Override
    public void sendCertificate(String toUser, String subject, Map<String, Object> message, byte[] qrCodeBytes)
            throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try{
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(toUser);
            mimeMessageHelper.setSubject(subject);

            Context context = new Context();
            context.setVariables(message);

            String htmlContent = templateEngine.process(MailConfigHelper.TEMPLATE_CERTIFICADO, context);
            mimeMessageHelper.setText(htmlContent,true);

            ClassPathResource resource = new ClassPathResource("/static/images/logo.png");
            mimeMessageHelper.addInline("logoImage", resource);

            // Adjuntar el Certificado
            ByteArrayResource qrCodeResource = new ByteArrayResource(qrCodeBytes);
            mimeMessageHelper.addAttachment("certificate.png", qrCodeResource);
            
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("ERROR en el envio de mail");
            throw new MessagingException(e.getMessage());
        }
    }
}