package com.unla.eventos.services;

import java.util.List;
import java.util.Optional;

import com.unla.eventos.entities.Event;
import com.unla.eventos.entities.EventDays;

public interface IEventDaysService {
    public List<EventDays> findByEventId(int eventId);

    public Optional<EventDays> findById(int id);

    public List<EventDays> save(Event event);

    public void deleteByEventId(int eventId);
}
