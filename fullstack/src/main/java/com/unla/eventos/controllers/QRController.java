package com.unla.eventos.controllers;

import com.unla.eventos.entities.AssistanceResponse;
import com.unla.eventos.services.IAssistanceResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QRController {

    @Autowired
    private IAssistanceResponseService assistanceResponseService;

    @GetMapping("/qr/{qrCode}")
    public String procesarQRCode(@PathVariable String qrCode, Model model) {
        AssistanceResponse assistanceResponse = assistanceResponseService.findByQRCode(qrCode);
        if (assistanceResponse != null) {
            assistanceResponse.setPresent(true);
            assistanceResponseService.save(assistanceResponse);
            model.addAttribute("mensaje", "Presencia marcada exitosamente.");
        } else {
            model.addAttribute("mensaje", "Código QR inválido.");
        }
        return "qrResultado";
    }
}
