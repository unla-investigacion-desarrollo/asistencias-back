package com.unla.eventos.services.implementation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unla.eventos.entities.Event;
import com.unla.eventos.repositories.IEventRepository;
import com.unla.eventos.services.IEventService;

@Service
public class EventService implements IEventService {

    @Autowired
    private IEventRepository eventRepository;

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Optional<Event> findById(int id) {
        return eventRepository.findById(id);
    }

    public Event save(Event event) {
    	Event oldEvent = eventRepository.findById(event.getId()).get();
    	event.setCreatedAt(oldEvent.getCreatedAt());
    	if (event.getPublicFormLink() == null || event.getPublicFormLink().isEmpty()) {
            event.setPublicFormLink(generateUniqueCode());
        }
        return eventRepository.save(event);
    }

    public void deleteById(int id) {
        eventRepository.deleteById(id);
    }
    
    private String generateUniqueCode() {
        return UUID.randomUUID().toString();
    }
}
