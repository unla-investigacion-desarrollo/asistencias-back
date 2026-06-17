package com.unla.eventos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.unla.eventos.componentes.CertificadoScheduler;
import com.unla.eventos.services.ICertificadoService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/certificados")
public class CertificadoController {

    
    @Autowired
    private CertificadoScheduler certificadoScheduler;

    @Autowired
    private ICertificadoService certificadoService;

    @GetMapping("/enviar")
    public String enviarCertificadosManual() throws MessagingException {
        certificadoScheduler.runBatch();
        return "Certificados enviados (si correspondía)";
    }

    @GetMapping("/generar")
    public ResponseEntity<byte[]> generarCertificado(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String dni
    ) throws Exception {

        byte[] pdfBytes = certificadoService.generarCertificadoPdf(nombre, apellido, dni);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=certificado_" + nombre + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
