package com.unla.eventos.services.implementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unla.eventos.entities.Event;
import com.unla.eventos.entities.EventDays;
import com.unla.eventos.repositories.IEventDaysRepository;
import com.unla.eventos.services.IEventDaysService;

@Service
public class EventDaysService implements IEventDaysService {

    @Autowired
    private IEventDaysRepository eventDaysRepository;

    @Override
    public List<EventDays> findByEventId(int eventId) {
        return eventDaysRepository.findByEventId(eventId);
    }

    @Override
    public Optional<EventDays> findById(int id) {
        return eventDaysRepository.findById(id);
    }

    @Override
    public List<EventDays> save(Event event) {
        LocalDate inicio = event.getStartDate().toLocalDate();
        LocalDate fin = event.getEndDate().toLocalDate();
        List<EventDays> days = new ArrayList<>();
        for (LocalDate date = inicio; !date.isAfter(fin); date = date.plusDays(1)) {
            EventDays eventDays = new EventDays();
            eventDays.setEvent(event);
            eventDays.setDate(date);
            days.add(eventDays);
        }
        return eventDaysRepository.saveAll(days);
    }

    @Override
    public void deleteByEventId (int eventId) {
        eventDaysRepository.deleteByEventId(eventId);
    }
    
}
