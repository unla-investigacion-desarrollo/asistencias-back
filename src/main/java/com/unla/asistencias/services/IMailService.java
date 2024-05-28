package com.unla.asistencias.services;

public interface IMailService {
    public void sendMail(String from, String to, String subject, String body);    
}
