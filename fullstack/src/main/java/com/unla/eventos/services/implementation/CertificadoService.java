package com.unla.eventos.services.implementation;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.ByteArrayOutputStream;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.unla.eventos.entities.AssistanceDays;
import com.unla.eventos.entities.AssistanceResponse;
import com.unla.eventos.helpers.FunctionsHelper;
import com.unla.eventos.services.IAssistanceDaysService;
import com.unla.eventos.services.IAssistanceResponseService;
import com.unla.eventos.services.ICertificadoService;
import com.unla.eventos.services.IMailService;

import jakarta.mail.MessagingException;

@Service
public class CertificadoService implements ICertificadoService {

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
            if (enviados >= maxAEnviar)
                break;

            List<AssistanceDays> dias = assistanceDaysService.findByAssistanceResponseId(asistente.getId());
            boolean asistioAlMenosUnDia = dias.stream().anyMatch(AssistanceDays::isPresent);

            if (!asistioAlMenosUnDia || asistente.isAssistanceCertifySent())
                continue;

            try {
                // Generar certificado PDF con nombre, apellido y DNI
                byte[] certificado = generarCertificadoPdf(
                        asistente.getName(),
                        asistente.getLastName(),
                        asistente.getDocumentNumber());

                // Variables para el mail
                Map<String, Object> variables = new HashMap<>();
                variables.put("nombre",
                        asistente.getName() + " " + asistente.getLastName() + " DNI " + asistente.getDocumentNumber());
                variables.put("fecha", FunctionsHelper.formatLocalDateToARGTime(asistente.getEvent().getStartDate()));
                variables.put("caracter", asistente.getRolPrincipal());

                // Enviar mail
                mailService.sendCertificate(
                        asistente.getEmail(),
                        "Certificado de Asistencia",
                        variables,
                        certificado);

                // Marcar como enviado
                asistente.setAssistanceCertifySent(true);
                assistanceResponseService.save(asistente);
                enviados++;

            } catch (IOException e) {
                System.err.println("Error generando o enviando certificado a " + asistente.getEmail());
                e.printStackTrace();
            }
        }

        return enviados;
    }

    public byte[] generarCertificadoPdf(String nombre, String apellido, String dni)
            throws IOException {

        InputStream is = new ClassPathResource("static/plantillas/certificado.pdf").getInputStream();
        byte[] pdfBytes = is.readAllBytes();
        PDDocument document = Loader.loadPDF(pdfBytes);
        PDPage page = document.getPage(0);

        PDType0Font font = PDType0Font.load(document,
                new ClassPathResource("static/fonts/FranklinGothicBook.ttf").getInputStream());
        int fontSize = 30;
        float pageWidth = page.getMediaBox().getWidth();

        String nombreCompleto = nombre + " " + apellido + " DNI " + dni;
        float textWidth = (font.getStringWidth(nombreCompleto) / 1000) * fontSize;

        PDPageContentStream contentStream = new PDPageContentStream(document, page,
                PDPageContentStream.AppendMode.APPEND, true);
        contentStream.setFont(font, fontSize);

        if (textWidth < pageWidth - 200) {
            // Cabe en una línea
            float startX = (pageWidth - textWidth) / 2;
            float y = 275;
            contentStream.beginText();
            contentStream.newLineAtOffset(startX, y);
            contentStream.showText(nombreCompleto);
            contentStream.endText();
        } else {
            // Dividido en 2 líneas (nombre y apellido)
            float nombreWidth = (font.getStringWidth(nombre) / 1000) * fontSize;
            float startXNombre = (pageWidth - nombreWidth) / 2;
            float yNombre = 285;

            contentStream.beginText();
            contentStream.newLineAtOffset(startXNombre, yNombre);
            contentStream.showText(nombre);
            contentStream.endText();

            // Segunda línea: apellido + DNI
            String apellidoConDni = apellido + " DNI " + dni;
            float apellidoWidth = (font.getStringWidth(apellidoConDni) / 1000) * fontSize;
            float startXApellido = (pageWidth - apellidoWidth) / 2;
            float yApellido = 255;

            contentStream.beginText();
            contentStream.newLineAtOffset(startXApellido, yApellido);
            contentStream.showText(apellidoConDni);
            contentStream.endText();
        }

        contentStream.close();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.save(baos);
        document.close();

        return baos.toByteArray();
    }

}
