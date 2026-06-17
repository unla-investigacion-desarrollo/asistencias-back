package com.unla.eventos.services;

import java.io.IOException;

import jakarta.mail.MessagingException;

public interface ICertificadoService {
    public int enviarCertificados(int eventId, int maxAEnviar) throws MessagingException;
     public byte[] generarCertificadoPdf(String nombre, String apellido, String dni)
            throws IOException;
}
