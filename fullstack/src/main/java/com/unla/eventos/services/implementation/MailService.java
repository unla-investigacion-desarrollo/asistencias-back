package com.unla.eventos.services.implementation;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.unla.eventos.helpers.ViewRouteHelper;
import com.unla.eventos.services.IMailService;

@Service("emailService")
@Slf4j
public class MailService implements IMailService {

    private final JavaMailSender mailSender;

    private final SpringTemplateEngine templateEngine;

    public MailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendEmail(String toUser, String subject, Map<String, Object> message, byte[] qrCodeBytes)
            throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try{
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(ViewRouteHelper.EMAIL_SENDER);
            mimeMessageHelper.setTo(toUser);
            mimeMessageHelper.setSubject(subject);

            Context context = new Context();
            context.setVariables(message);

            String htmlContent = templateEngine.process(ViewRouteHelper.TEMPLATE_REGISTER_USER, context);
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
}