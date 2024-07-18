package com.unla.eventos.controllers;

import java.util.HashMap;
import java.util.Map;
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
    
    @GetMapping("mail_in_use")
    public String mailInUse() {
        return ViewRouteHelper.REGISTRO_MAIL_IN_USE;
    }
    
    @GetMapping("/{uniqueCode}")
    public String mostrarFormularioRegistro(@PathVariable("uniqueCode") String uniqueCode, Model model) {
    	Optional<Event> eventOp = assistanceResponseService.findByUniqueCode(uniqueCode);
    	if(eventOp.isPresent()) {
    		Event event = eventOp.get();
    		model.addAttribute("eventName", event.getName());
    		model.addAttribute("eventStartDate", FunctionsHelper.formatLocalDateToARGTime(event.getStartDate()));
    		model.addAttribute("eventEndDate", FunctionsHelper.formatLocalDateToARGTime(event.getEndDate()));
        	model.addAttribute("uniqueCode", uniqueCode);
            model.addAttribute("assistanceResponse", new AssistanceResponse());
            return ViewRouteHelper.REGISTRO_INDEX;	
    	}else {
    		return  "redirect:/" + ViewRouteHelper.REGISTRO_NOTFOUND;
    	}
    }

    @PostMapping("/submit")
    public String procesarRegistro(@RequestParam("uniqueCode") String uniqueCode,
                                   @ModelAttribute AssistanceResponse assistanceResponse,
                                   @ModelAttribute String eventName,
                                   @ModelAttribute String eventStartDate,
    							   @ModelAttribute String eventEndDate,
                                   HttpServletResponse response) {
    	Optional<Event> eventOp = assistanceResponseService.findByUniqueCode(uniqueCode);
    	if(eventOp.isPresent()) {
    		Optional<AssistanceResponse> preExisting = assistanceResponseService.findByEmailAndEventId(assistanceResponse.getEmail(), eventOp.get().getId());
    		if(!preExisting.isPresent()) {
	    		Event event = eventOp.get();
	    		assistanceResponse.setPresent(false);
	            assistanceResponse.setAssistanceCertifySent(false);
	            assistanceResponse.setQRCode(UUID.randomUUID().toString());
	            
	            assistanceResponse.setEvent(event);
	            try {
	                assistanceResponseService.save(assistanceResponse);
	                byte[] qrCodeBytes = qrCodeService.generateQRCodeBytes(assistanceResponse.getQRCode(), 300, 300);
			        Map<String, Object> message = new HashMap<>();
			        message.put("name", assistanceResponse.getName());
			        message.put("lastName", assistanceResponse.getLastName());
			        message.put("eventName", event.getName());
			        message.put("eventStartDate", FunctionsHelper.formatLocalDateToARGTime(event.getStartDate()));
			        message.put("eventEndDate", FunctionsHelper.formatLocalDateToARGTime(event.getEndDate()));
	
			        mailService.sendEmail(assistanceResponse.getEmail(), "Confirmación de registro a evento", message, qrCodeBytes);
	            } catch (Exception e) {
					// TODO: add errors on view
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
