package com.unla.eventos.services;

import java.util.List;
import java.util.Optional;

import com.unla.eventos.entities.Event;

public interface IEventService {
	public List<Event> findAll();

    public Optional<Event> findById(int id);
    
    public Optional<Event> findByUniqueCode(String uniqueCode);

    public Event save(Event event);

    public void deleteById(int id);
}
