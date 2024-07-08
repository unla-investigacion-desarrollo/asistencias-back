package com.unla.eventos.services.implementation;

import java.util.List;
import java.util.Optional;

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
        return eventRepository.save(event);
    }

    public void deleteById(int id) {
        eventRepository.deleteById(id);
    }
}
