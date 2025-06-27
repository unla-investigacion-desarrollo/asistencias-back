package com.unla.eventos.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Color;
import java.awt.RenderingHints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.unla.eventos.componentes.CertificadoScheduler;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/certificados")
public class CertificadoController {

    
    @Autowired
    private CertificadoScheduler certificadoScheduler;

    @GetMapping("/enviar")
    public String enviarCertificadosManual() throws MessagingException {
        certificadoScheduler.runBatch();
        return "Certificados enviados (si correspondía)";
    }

    @GetMapping("/generar")
    public ResponseEntity<byte[]> generarCertificado(
            @RequestParam String nombre,
            @RequestParam String fecha,
            @RequestParam String caracter
    ) throws IOException {
        // Cargar plantilla desde resources
        BufferedImage plantilla = ImageIO.read(new ClassPathResource("static/images/certificado.png").getInputStream());

        Graphics2D g2d = plantilla.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Configurar fuente
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Times New Roman", Font.PLAIN, 102));

        // Ajustar coordenadas según tu plantilla
        g2d.drawString(nombre, 320, 740); // debajo de "SE DEJA CONSTANCIA DE QUE"
        g2d.setFont(new Font("Times New Roman", Font.PLAIN, 28));
        g2d.drawString(fecha, 1460, 880);  // después de "realizada el día:"
        g2d.drawString(caracter, 805, 930); // después de "en carácter de:"

        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(plantilla, "png", baos);
        byte[] imagenBytes = baos.toByteArray();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=certificado_" + nombre + ".png")
                .contentType(MediaType.IMAGE_PNG)
                .body(imagenBytes);
    }
}
