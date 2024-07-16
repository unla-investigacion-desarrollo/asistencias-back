package com.unla.eventos.controllers;

import com.unla.eventos.entities.AssistanceResponse;
import com.unla.eventos.helpers.FunctionsHelper;
import com.unla.eventos.helpers.ViewRouteHelper;
import com.unla.eventos.services.IAssistanceResponseService;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
    	Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if(user == "anonymousUser") return ViewRouteHelper.QR_ASSISTANCE;
        
    	AssistanceResponse assistanceResponse = assistanceResponseService.findByQRCode(qrCode);
        if (assistanceResponse != null) {
        	if (LocalDateTime.now().isBefore(assistanceResponse.getEvent().getStartDate())) {
        		model.addAttribute("mensaje",
        				"Evento no iniciado. Vuelva a leer el QR después del: " +
        				FunctionsHelper.formatLocalDateToARGTime(assistanceResponse.getEvent().getStartDate()));
        	} else {
        		if (!assistanceResponse.isPresent()) {
            		assistanceResponse.setPresent(true);
            		assistanceResponseService.save(assistanceResponse);
            		model.addAttribute("mensaje", "Presencia marcada exitosamente. Disfrute del evento.");
            	} else {
                    model.addAttribute("mensaje", "Asistencia ya registrada.");
                }
        	}
        } else {
            model.addAttribute("mensaje", "Código QR inválido.");
        }
        return ViewRouteHelper.QR_RESULT;
    }
}
