package com.unla.eventos.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.unla.eventos.entities.AssistanceResponse;
import com.unla.eventos.entities.Event;
import com.unla.eventos.helpers.FunctionsHelper;
import com.unla.eventos.helpers.ViewRouteHelper;
import com.unla.eventos.services.IAssistanceResponseService;
import com.unla.eventos.services.IMailService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/registro")
public class RegistroController {

	@Autowired
	private IMailService mailService;
	
    @Autowired
    private IAssistanceResponseService assistanceResponseService;

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
    
    @GetMapping("error")
    public String error() {
        return ViewRouteHelper.REGISTRO_ERROR;
    }
    
    @GetMapping("mail_in_use")
    public String mailInUse() {
        return ViewRouteHelper.REGISTRO_MAIL_IN_USE;
    }
    
    @GetMapping("/{uniqueCode}")
    public String mostrarFormularioRegistro(@PathVariable String uniqueCode, Model model) {
    	Optional<Event> eventOp = assistanceResponseService.findEventByUniqueCode(uniqueCode);
    	if(eventOp.isPresent()) {
    		model = buildModelByEvent(uniqueCode, model, eventOp.get());
            return ViewRouteHelper.REGISTRO_INDEX;	
    	}else {
    		return  "redirect:/" + ViewRouteHelper.REGISTRO_NOTFOUND;
    	}
    }
    
    @GetMapping("/min/{uniqueCode}")
    public String mostrarFormularioResumido(@PathVariable String uniqueCode, Model model) {
    	Optional<Event> eventOp = assistanceResponseService.findEventByUniqueCode(uniqueCode);
    	if(eventOp.isPresent()) {
    		model = buildModelByEvent(uniqueCode, model, eventOp.get());
            return ViewRouteHelper.REGISTRO_RESUME;	
    	}else {
    		return  "redirect:/" + ViewRouteHelper.REGISTRO_NOTFOUND;
    	}
    }
    
    private Model buildModelByEvent(String uniqueCode, Model model, Event event) {
		model.addAttribute("eventName", event.getName());
		model.addAttribute("imagePath", event.getImagePath());
		model.addAttribute("title", event.getTitle());
		model.addAttribute("description", event.getDescription());
		model.addAttribute("eventStartDate", FunctionsHelper.formatLocalDateToARGTime(event.getStartDate()));
		model.addAttribute("eventEndDate", FunctionsHelper.formatLocalDateToARGTime(event.getEndDate()));
    	model.addAttribute("uniqueCode", uniqueCode);
        model.addAttribute("assistanceResponse", new AssistanceResponse());
        return model;
    }

    @PostMapping("/submit")
    public String procesarRegistro(@RequestParam String uniqueCode,
                                   @ModelAttribute AssistanceResponse assistanceResponse,
                                   @ModelAttribute String eventName,
                                   @ModelAttribute String eventStartDate,
    							   @ModelAttribute String eventEndDate,
                                   HttpServletResponse response) {
    	Optional<Event> eventOp = assistanceResponseService.findEventByUniqueCode(uniqueCode);
    	if(eventOp.isPresent()) {
    		Optional<AssistanceResponse> preExisting = assistanceResponseService.findByEmailAndEventId(assistanceResponse.getEmail(), eventOp.get().getId());
    		if(!preExisting.isPresent()) {
	    		Event event = eventOp.get();
	    		assistanceResponse.setPresent(false);
	            assistanceResponse.setAssistanceCertifySent(false);
	            assistanceResponse.setWelcomeMailSent(true);
	            assistanceResponse.setSource("Interno");
	            assistanceResponse.setQRCode(UUID.randomUUID().toString());
	            
	            assistanceResponse.setEvent(event);
	            try {
	            	mailService.prepareAndSendEmail(assistanceResponse.getQRCode(), assistanceResponse.getName(), assistanceResponse.getLastName(),
	            									event.getName(), event.getStartDate(), event.getEndDate(), event.getMailContact(),
	            									assistanceResponse.getEmail());
			        assistanceResponseService.save(assistanceResponse);
	            } catch (Exception e) {
					// TODO: add errors on view
	            	e.printStackTrace();
	            	return "redirect:/" + ViewRouteHelper.REGISTRO_ERROR;
				}
    		} else {
    			return "redirect:/" + ViewRouteHelper.REGISTRO_MAIL_IN_USE;
    		}
            return "redirect:/" + ViewRouteHelper.REGISTRO_EXITO;
    	}else {
    		return "redirect:/" + ViewRouteHelper.REGISTRO_NOTFOUND;
    	}
    }
}
