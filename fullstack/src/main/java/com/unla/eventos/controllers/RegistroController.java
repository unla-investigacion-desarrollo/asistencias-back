package com.unla.eventos.controllers;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.unla.eventos.entities.AssistanceResponse;
import com.unla.eventos.entities.Event;
import com.unla.eventos.services.IAssistanceResponseService;

@Controller
@RequestMapping("/registro")
public class RegistroController {

    @Autowired
    private IAssistanceResponseService assistanceResponseService;

    @GetMapping("exito")
    public String exito() {
        return "exito";
    }
    
    @GetMapping("notfound")
    public String notfound() {
        return "notfound";
    }
    
    @GetMapping("/{publicFormLink}")
    public String mostrarFormularioRegistro(@PathVariable("publicFormLink") String publicFormLink, Model model) {
    	Optional<Event> eventOp = assistanceResponseService.findByPublicFormLink(publicFormLink);
    	if(eventOp.isPresent()) {
    		Event event = eventOp.get();
    		model.addAttribute("eventName", event.getName());
        	model.addAttribute("publicFormLink", publicFormLink);
            model.addAttribute("assistanceResponse", new AssistanceResponse());
            return "registro";	
    	}else {
    		return "redirect:/registro/notfound";
    	}
    }

    @PostMapping("/submit")
    public String procesarRegistro(@RequestParam("publicFormLink") String publicFormLink,
                                   @ModelAttribute AssistanceResponse assistanceResponse) {
        
    	Optional<Event> eventOp = assistanceResponseService.findByPublicFormLink(publicFormLink);
    	if(eventOp.isPresent()) {
    		Event event = eventOp.get();
    		assistanceResponse.setPresent(false);
            assistanceResponse.setAssistanceCertifySent(false);
            assistanceResponse.setQRCode(generateQRCode());
            assistanceResponse.setEvent(event);
            try {
                assistanceResponseService.save(assistanceResponse);
            } catch (Exception e) {
				// TODO: add errors on view
			}
            return "redirect:/registro/exito";	
    	}else {
    		return "redirect:/registro/notfound";
    	}
    }

    private String generateQRCode() {
        return "GeneratedQRCode";
    }
}
