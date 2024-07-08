package com.unla.asistencias.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.unla.asistencias.models.database.Event;
import com.unla.asistencias.models.response.EventDTO;

@Service
public interface IEventService {

    public List<EventDTO> getAllEvents();

    public Optional<EventDTO> getEventById(int id);

    public EventDTO createEvent(Event event);

    public EventDTO updateEvent(int id, Event eventDetails);
}
