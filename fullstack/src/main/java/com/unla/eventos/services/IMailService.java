package com.unla.eventos.services;

import jakarta.mail.MessagingException;

import java.time.LocalDateTime;
import java.util.Map;

public interface IMailService {
	void prepareAndSendEmail(String qRCode, String name, String lastName, String eventName,
    		LocalDateTime eventStartDate, LocalDateTime eventEndDate, String mailContact, String assistanceResponseEmail) throws Exception;
	
    void sendEmail(String toUser, String subject, Map<String, Object> message, byte[] qrCodeBytes)
            throws MessagingException;
}