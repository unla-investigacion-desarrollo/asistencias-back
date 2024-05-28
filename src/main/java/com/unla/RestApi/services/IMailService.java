package com.unla.RestApi.services;

public interface IMailService {
    public void sendMail(String from, String to, String subject, String body);    
}
