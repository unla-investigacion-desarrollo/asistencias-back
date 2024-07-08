package com.unla.eventos.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.unla.eventos.entities.Event;
import com.unla.eventos.services.IEventService;

@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private IEventService eventService;

    // Método para listar eventos
    @GetMapping
    public String listEvents(Model model) {
        model.addAttribute("events", eventService.findAll());
        return "events";
    }

    // Método para mostrar el formulario de creación de eventos
    @GetMapping("/new")
    public String createEventForm(Model model) {
        model.addAttribute("event", new Event());
        return "eventForm";
    }

    // Método para mostrar el formulario de edición de eventos
    @GetMapping("/edit/{id}")
    public String editEventForm(@PathVariable int id, Model model) {
        Optional<Event> event = eventService.findById(id);
        if (event.isPresent()) {
            model.addAttribute("event", event.get());
            return "eventForm";
        } else {
            return "redirect:/events";
        }
    }

    // Método para guardar un evento (nuevo o editado)
    @PostMapping
    public String saveEvent(@ModelAttribute("event") Event event) {
        eventService.save(event);
        return "redirect:/events";
    }

    // Método para eliminar un evento
    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable int id) {
        eventService.deleteById(id);
        return "redirect:/events";
    }
}
