package com.unla.eventos.services;

import jakarta.mail.MessagingException;
import java.util.Map;

public interface IMailService {
    void sendEmail(String toUser, String subject, Map<String, Object> message, byte[] qrCodeBytes)
            throws MessagingException;
}