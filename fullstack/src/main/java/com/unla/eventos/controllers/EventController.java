package com.unla.eventos.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.unla.eventos.entities.Event;
import com.unla.eventos.helpers.ViewRouteHelper;
import com.unla.eventos.services.IEventService;

@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private IEventService eventService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("events", eventService.findAll());
        return ViewRouteHelper.EVENT_INDEX;
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("event", new Event());
        return ViewRouteHelper.EVENT_SAVE;
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        Optional<Event> event = eventService.findById(id);
        if (event.isPresent()) {
            Event existingEvent = event.get();
            model.addAttribute("event", existingEvent);
            return ViewRouteHelper.EVENT_SAVE;
        } else {
            return ViewRouteHelper.EVENTS_CRUD;
        }
    }

    @PostMapping
    public String save(@ModelAttribute("event") Event event) {
    	try {
    		eventService.save(event);
		} catch (Exception e) {
			//TODO: Add errors on view
		}
        return ViewRouteHelper.EVENTS_CRUD;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
    	try {
    		eventService.deleteById(id);
		} catch (Exception e) {
			//TODO: Add errors on view
		}
    	return ViewRouteHelper.EVENTS_CRUD;
    }
}
