package com.unla.eventos.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.unla.eventos.entities.AssistanceResponse;
import com.unla.eventos.entities.Event;
import com.unla.eventos.helpers.ViewRouteHelper;
import com.unla.eventos.services.IAssistanceResponseService;
import com.unla.eventos.services.IMailService;
import com.unla.eventos.services.implementation.QRCodeService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/registro")
public class RegistroController {

	@Autowired
	private IMailService mailService;
	
    @Autowired
    private IAssistanceResponseService assistanceResponseService;
    
    @Autowired
    private QRCodeService qrCodeService;

    RegistroController(IMailService mailService) {
        this.mailService = mailService;
    }
    
    @GetMapping("exito")
    public String exito() {
        return ViewRouteHelper.REGISTRO_EXITO;
    }
    
    @GetMapping("notfound")
    public String notfound() {
        return ViewRouteHelper.REGISTRO_NOTFOUND;
    }
    
    @GetMapping("/{publicFormLink}")
    public String mostrarFormularioRegistro(@PathVariable("publicFormLink") String publicFormLink, Model model) {
    	Optional<Event> eventOp = assistanceResponseService.findByPublicFormLink(publicFormLink);
    	if(eventOp.isPresent()) {
    		Event event = eventOp.get();
    		model.addAttribute("eventName", event.getName());
    		model.addAttribute("eventStartDate", event.getStartDate());
    		model.addAttribute("eventEndDate", event.getEndDate());
        	model.addAttribute("publicFormLink", publicFormLink);
            model.addAttribute("assistanceResponse", new AssistanceResponse());
            return ViewRouteHelper.REGISTRO_INDEX;	
    	}else {
    		return  "redirect:/" + ViewRouteHelper.REGISTRO_NOTFOUND;
    	}
    }

    @PostMapping("/submit")
    public String procesarRegistro(@RequestParam("publicFormLink") String publicFormLink,
                                   @ModelAttribute AssistanceResponse assistanceResponse,
                                   @ModelAttribute String eventName,
                                   @ModelAttribute String eventStartDate,
    							   @ModelAttribute String eventEndDate,
                                   HttpServletResponse response) {
    	Optional<Event> eventOp = assistanceResponseService.findByPublicFormLink(publicFormLink);
    	if(eventOp.isPresent()) {
    		Event event = eventOp.get();
    		assistanceResponse.setPresent(false);
            assistanceResponse.setAssistanceCertifySent(false);
            
            int code = 123;
            String qrCode = generateQRCode(code);
            assistanceResponse.setQRCode(qrCode);
            
            assistanceResponse.setEvent(event);
            try {
                assistanceResponseService.save(assistanceResponse);
                byte[] qrCodeBytes = qrCodeService.generateQRCodeBytes(String.valueOf(code), 300, 300);
		        Map<String, Object> message = new HashMap<>();
		        message.put("name", assistanceResponse);
		        message.put("lastName", assistanceResponse);
		        message.put("eventName", event.getName());
		        message.put("eventStartDate", event.getStartDate());
		        message.put("eventEndDate", event.getEndDate());

		        mailService.sendEmail(assistanceResponse.getEmail(), "Prueba de envio", message, qrCodeBytes);
            } catch (Exception e) {
				// TODO: add errors on view
			}
            return "redirect:/" + ViewRouteHelper.REGISTRO_EXITO;
    	}else {
    		return "redirect:/" + ViewRouteHelper.REGISTRO_NOTFOUND;
    	}
    }

    private String generateQRCode(int code) {
        try {
            return qrCodeService.generateQRCodeImage(String.valueOf(code), 300, 300);
        } catch (Exception e) {
            return "Error al generar el código QR";
        }
    }
}
