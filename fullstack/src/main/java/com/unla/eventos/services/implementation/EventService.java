package com.unla.eventos.services.implementation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unla.eventos.entities.AssistanceResponse;
import com.unla.eventos.entities.Event;
import com.unla.eventos.repositories.IAssistanceResponseRepository;
import com.unla.eventos.repositories.IEventRepository;
import com.unla.eventos.services.IEventService;

@Service
public class EventService implements IEventService {

    @Autowired
    private IEventRepository eventRepository;
    
    @Autowired
    private IAssistanceResponseRepository assistanceResponseRepository;

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Optional<Event> findById(int id) {
        return eventRepository.findById(id);
    }
    
    public Optional<Event> findByUniqueCode(String uniqueCode) {
        return eventRepository.findByUniqueCode(uniqueCode);
    }

    public Event save(Event event) {
    	Optional<Event> oldEvent = eventRepository.findById(event.getId());
    	if(oldEvent.isPresent()) {
    		event.setCreatedAt(oldEvent.get().getCreatedAt());
    		event.setUniqueCode(oldEvent.get().getUniqueCode());
    	}
    	else {
    		event.setUniqueCode(generateUniqueCode());
    	}
        return eventRepository.save(event);
    }

    public void deleteById(int id) {
    	List<AssistanceResponse> responses = assistanceResponseRepository.findByEventId(id);
    	try {
    		for (AssistanceResponse assistanceResponse : responses) {
    			assistanceResponseRepository.deleteById(assistanceResponse.getId());
			}
    		eventRepository.deleteById(id);
    	}
        catch (Exception e) {
			throw e;
		}
    }
    
    private String generateUniqueCode() {
        return UUID.randomUUID().toString();
    }
}
