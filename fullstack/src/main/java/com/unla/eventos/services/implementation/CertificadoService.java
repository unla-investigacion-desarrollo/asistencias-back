package com.unla.eventos.services.implementation;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Color;
import java.awt.RenderingHints;

import com.unla.eventos.entities.AssistanceDays;
import com.unla.eventos.entities.AssistanceResponse;
import com.unla.eventos.helpers.FunctionsHelper;
import com.unla.eventos.services.IAssistanceDaysService;
import com.unla.eventos.services.IAssistanceResponseService;
import com.unla.eventos.services.ICertificadoService;
import com.unla.eventos.services.IMailService;

import jakarta.mail.MessagingException;

@Service
public class CertificadoService implements ICertificadoService{

    @Autowired
    private IAssistanceDaysService assistanceDaysService;

    @Autowired
    private IAssistanceResponseService assistanceResponseService;

    @Autowired
	private IMailService mailService;

    @Override
    public int enviarCertificados(int eventId, int maxAEnviar) throws MessagingException {
        int enviados = 0;
        List<AssistanceResponse> assistanceResponse = assistanceResponseService.findByEventIdWithEvent(eventId);

        for (AssistanceResponse asistente : assistanceResponse) {
            if (enviados >= maxAEnviar) break;
            List<AssistanceDays> dias = assistanceDaysService.findByAssistanceResponseId(asistente.getId());

            boolean asistioAlMenosUnDia = dias.stream().anyMatch(AssistanceDays::isPresent);

            if (!asistioAlMenosUnDia | asistente.isAssistanceCertifySent()) {
                continue; // No asistió ningún día o el certificado ya se envio, no se envía certificado
            }
            
            try {
                byte[] certificado = generarCertificado(asistente);
            
                Map<String, Object> variables = new HashMap<>();
                variables.put("nombre", asistente.getName() + " " + asistente.getLastName());
                variables.put("fecha", FunctionsHelper.formatLocalDateToARGTime(asistente.getEvent().getStartDate()));
                variables.put("caracter", asistente.getRolPrincipal());

                mailService.sendCertificate(asistente.getEmail(), "Certificado de Asistencia", variables, certificado);
                asistente.setAssistanceCertifySent(true);
                assistanceResponseService.save(asistente);
                enviados++;

            } catch (IOException e) {
                // Loguear error y continuar
                System.err.println("Error generando o enviando certificado a " + asistente.getEmail());
                e.printStackTrace();
            }
        }
        return enviados;
    }

    private byte[] generarCertificado(AssistanceResponse assistanceResponse) throws IOException {
        BufferedImage plantilla = ImageIO.read(new ClassPathResource("static/images/certificado.png").getInputStream());
        Graphics2D g2d = plantilla.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Times New Roman", Font.PLAIN, 102));
        g2d.drawString(assistanceResponse.getName() + " " + assistanceResponse.getLastName(), 320, 740);

        g2d.setFont(new Font("Times New Roman", Font.PLAIN, 28));
        g2d.drawString(assistanceResponse.getEvent().getStartDate().toLocalDate().toString(), 1460, 880);
        g2d.drawString(assistanceResponse.getRolPrincipal(), 805, 930);

        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(plantilla, "png", baos);
        return baos.toByteArray();
    }
    
}
