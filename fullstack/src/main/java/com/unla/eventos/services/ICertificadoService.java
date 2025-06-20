package com.unla.eventos.services;

import jakarta.mail.MessagingException;

public interface ICertificadoService {
    public int enviarCertificados(int eventId, int maxAEnviar) throws MessagingException;
}
