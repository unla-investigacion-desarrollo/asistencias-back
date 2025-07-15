package com.unla.eventos.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.unla.eventos.entities.AssistanceDays;
import com.unla.eventos.entities.AssistanceResponse;
import com.unla.eventos.entities.Event;
import com.unla.eventos.entities.EventDays;
import com.unla.eventos.helpers.ViewRouteHelper;
import com.unla.eventos.services.IAssistanceDaysService;
import com.unla.eventos.services.IAssistanceResponseService;
import com.unla.eventos.services.IEventDaysService;
import com.unla.eventos.services.IEventService;
import com.unla.eventos.services.IMailService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

@Controller
@RequestMapping("/events")
public class EventController {

    @Value("${PUBLIC_QR_LINK_SERVER}")
    private String PUBLIC_QR_LINK_SERVER;

    @Autowired
    private IEventService eventService;

    @Autowired
    private IEventDaysService eventDaysService;

    @Autowired
    private IAssistanceResponseService assistanceResponseService;

    @Autowired
    private IAssistanceDaysService assistanceDaysService;

    @Autowired
    private IMailService mailService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("events", eventService.findAll());
		model.addAttribute("username", getLoggedUser().getUsername());
        return ViewRouteHelper.EVENT_INDEX;
    }

    @GetMapping("/{id}/days")
    public String viewEventDays(@PathVariable("id") int eventId, Model model) {
        List<EventDays> days = eventDaysService.findByEventId(eventId);
        model.addAttribute("days", days);
        return ViewRouteHelper.EVENT_DAYS;
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("event", new Event());
		model.addAttribute("username", getLoggedUser().getUsername());
        return ViewRouteHelper.EVENT_SAVE;
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
		model.addAttribute("username", getLoggedUser().getUsername());
        Optional<Event> event = eventService.findById(id);
        if (event.isPresent()) {
            Event existingEvent = event.get();
            model.addAttribute("event", existingEvent);
            return ViewRouteHelper.EVENT_SAVE;
        } else {
            return ViewRouteHelper.REDIRECT_EVENTS_CRUD;
        }
    }

    @PostMapping
    public String save(@ModelAttribute Event event, @RequestParam MultipartFile image) {
    	try {
    		if (!image.isEmpty() && image.getContentType().equals("image/png")) {
                String imagePath = saveImage(image);
                event.setImagePath(imagePath);
            } else {
    			Optional<Event> existingEventOpt = event.getId() > 0 ? eventService.findById(event.getId()) : Optional.empty();
    			if(existingEventOpt.isPresent() && existingEventOpt.get().getImagePath() != null && !existingEventOpt.get().getImagePath().isEmpty()) {
    				deleteImage(existingEventOpt.get().getImagePath());
                    event.setImagePath(null);
    			}
    		}
    		eventService.save(event);
            eventDaysService.save(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return ViewRouteHelper.REDIRECT_EVENTS_CRUD;
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable int id) {
    	try {
    		eventService.deleteById(id);
            eventDaysService.deleteByEventId(id);
		} catch (Exception e) {
			//TODO: Add errors on view
		}
    	return ViewRouteHelper.REDIRECT_EVENTS_CRUD;
    }
    
    private String saveImage(MultipartFile image) throws IOException {
    	String fileName = UUID.randomUUID().toString() + ".png";
        Path imageDirectory = Paths.get(ViewRouteHelper.UPLOADS_IMAGES_EVENTS);
        
        if (!Files.exists(imageDirectory)) {
            Files.createDirectories(imageDirectory);
        }
        BufferedImage originalImage = ImageIO.read(image.getInputStream());
        
        int maxWidth = 500;
        if (originalImage.getWidth() > maxWidth) {
            int newHeight = (originalImage.getHeight() * maxWidth) / originalImage.getWidth();
            
            BufferedImage resizedImage = new BufferedImage(maxWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = resizedImage.createGraphics();
            g2d.drawImage(originalImage.getScaledInstance(maxWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
            g2d.dispose();
            
            Path imagePath = imageDirectory.resolve(fileName);
            ImageIO.write(resizedImage, "png", imagePath.toFile());
            
        } else {
            Path imagePath = imageDirectory.resolve(fileName);
            Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
        }
        return fileName;
    }
    
    private void deleteImage(String imageName) throws IOException {
        try {
            Path imageDirectory = Paths.get(ViewRouteHelper.UPLOADS_IMAGES_EVENTS);
            Path imagePath = imageDirectory.resolve(imageName);
            Files.deleteIfExists(imagePath);
        } catch (IOException e) {
            throw e;
        }
    }
    
    private User getLoggedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PostMapping("/enviar-feedback")
    public String enviarEncuestaPorMail(@RequestParam String uniqueCode, RedirectAttributes redirectAttributes) {
        Optional<Event> eventOp = eventService.findByUniqueCode(uniqueCode);

        if (eventOp.isPresent()) {
            Event event = eventOp.get();

            List<AssistanceResponse> assistanceResponses = assistanceResponseService.findByEventId(event.getId());

            for (AssistanceResponse asistente : assistanceResponses) {
                List<AssistanceDays> dias = assistanceDaysService.findByAssistanceResponseId(asistente.getId());
                boolean asistioAlMenosUnDia = dias.stream().anyMatch(AssistanceDays::isPresent);

                if (asistioAlMenosUnDia) {
                    // Armar asunto y cuerpo
                    String usrServer = PUBLIC_QR_LINK_SERVER.replace("/qr/", "/feedback/");
                    String subject = "Encuesta de satisfacción - " + event.getName();
                    String body = "Hola " + asistente.getName() + ",\n\n" +
                            "Gracias por asistir al evento \"" + event.getName() + "\".\n" +
                            "Por favor, completá la encuesta en el siguiente enlace:\n\n" +
                            usrServer + uniqueCode + "\n\n" +
                            "¡Muchas gracias!\n\nUniversidad Nacional de Lanús";

                    mailService.sendEncuesta(asistente.getEmail(), subject, body);
                }
            }

            redirectAttributes.addFlashAttribute("messageMails", "El envío de encuestas ha comenzado. El proceso puede demorar unos minutos.");
        } else {
            redirectAttributes.addFlashAttribute("messageMailsError", "No se encontró el evento.");
        }

        return ViewRouteHelper.REDIRECT_EVENTS_CRUD;
    }

}
