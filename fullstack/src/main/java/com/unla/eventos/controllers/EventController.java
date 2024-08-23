package com.unla.eventos.controllers;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.unla.eventos.entities.Event;
import com.unla.eventos.helpers.ViewRouteHelper;
import com.unla.eventos.services.IEventService;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.web.multipart.MultipartFile;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private IEventService eventService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("events", eventService.findAll());
		model.addAttribute("username", getLoggedUser().getUsername());
        return ViewRouteHelper.EVENT_INDEX;
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
            }
    		eventService.save(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return ViewRouteHelper.REDIRECT_EVENTS_CRUD;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
    	try {
    		eventService.deleteById(id);
		} catch (Exception e) {
			//TODO: Add errors on view
		}
    	return ViewRouteHelper.REDIRECT_EVENTS_CRUD;
    }
    
    private String saveImage(MultipartFile image) throws IOException {
    	String fileName = UUID.randomUUID().toString() + ".png";
        Path imageDirectory = Paths.get("src/main/resources/static/images/events");
        
        if (!Files.exists(imageDirectory)) {
            Files.createDirectories(imageDirectory);
        }
        BufferedImage originalImage = ImageIO.read(image.getInputStream());
        
        int maxWidth = 500;
        if (originalImage.getWidth() > maxWidth) {
            int newHeight = (originalImage.getHeight() * maxWidth) / originalImage.getWidth();
            
            BufferedImage resizedImage = new BufferedImage(maxWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = resizedImage.createGraphics();
            g2d.drawImage(originalImage.getScaledInstance(maxWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
            g2d.dispose();
            
            Path imagePath = imageDirectory.resolve(fileName);
            ImageIO.write(resizedImage, "png", imagePath.toFile());
            
        } else {
            Path imagePath = imageDirectory.resolve(fileName);
            Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
        }
        return "/images/events/" + fileName;
    }
    
    private User getLoggedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
