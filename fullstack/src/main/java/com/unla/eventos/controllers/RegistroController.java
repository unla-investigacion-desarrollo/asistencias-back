package com.unla.eventos.controllers;

import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.unla.eventos.entities.AssistanceResponse;
import com.unla.eventos.entities.Event;
import com.unla.eventos.services.IAssistanceResponseService;
import com.unla.eventos.services.implementation.QRCodeService;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/registro")
public class RegistroController {

    @Autowired
    private IAssistanceResponseService assistanceResponseService;
    
    @Autowired
    private QRCodeService qrCodeService;
    
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
                                   @ModelAttribute AssistanceResponse assistanceResponse,
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
		        // Configurar la respuesta HTTP para descargar la imagen
		        response.setContentType("image/png");
		        response.setContentLength(qrCodeBytes.length);
		        response.setHeader("Content-Disposition", "attachment; filename=\"qrcode.png\"");
		
		        // Escribir los bytes del código QR en el flujo de salida de la respuesta
		        try {
		            ServletOutputStream outputStream = response.getOutputStream();
		            outputStream.write(qrCodeBytes);
		            outputStream.close();
		        } catch (IOException e) {
		            // Manejo de errores al escribir la respuesta
		            e.printStackTrace();
		        }
            } catch (Exception e) {
				// TODO: add errors on view
			}
            return "redirect:/registro/exito";	
    	}else {
    		return "redirect:/registro/notfound";
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
