package com.unla.eventos.services.implementation;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.ByteArrayOutputStream;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDVariableText;
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


    public byte[] generarCertificadoPdf(String nombre, String apellido, String dni) throws IOException {
        InputStream is = new ClassPathResource("static/plantillas/certificado.pdf").getInputStream();
        byte[] pdfBytes = is.readAllBytes();
        PDDocument document = Loader.loadPDF(pdfBytes);

        PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();

        if (acroForm != null) {
            PDField campo = acroForm.getField("Text1");
            if (campo == null && !acroForm.getFields().isEmpty()) {
                campo = acroForm.getFields().get(0);
            }

            if (campo != null) {
                String nombreCompleto = nombre + " " + apellido + " DNI " + dni;

                PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

                PDRectangle rect = campo.getWidgets().get(0).getRectangle();
                float fieldWidth = rect.getWidth() - 10;
                float fontSize = 30f;
                float textWidth = (font.getStringWidth(nombreCompleto) / 1000) * fontSize;

                while (textWidth > fieldWidth && fontSize > 8) {
                    fontSize -= 0.5f;
                    textWidth = (font.getStringWidth(nombreCompleto) / 1000) * fontSize;
                }

                PDResources dr = acroForm.getDefaultResources();
                if (dr == null) {
                    dr = new PDResources();
                    acroForm.setDefaultResources(dr);
                }

                COSName fontName = dr.add(font);

                String daString = "/" + fontName.getName() + " " + fontSize + " Tf 0 g";
                ((PDVariableText) campo).setDefaultAppearance(daString);

                campo.setValue(nombreCompleto);
                acroForm.flatten();
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.save(baos);
        document.close();

        return baos.toByteArray();
    }

}
