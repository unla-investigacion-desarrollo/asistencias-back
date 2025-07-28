package com.unla.eventos.services.implementation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unla.eventos.entities.AssistanceDays;
import com.unla.eventos.entities.Event;
import com.unla.eventos.entities.EventDays;
import com.unla.eventos.repositories.IAssistanceDaysRepository;
import com.unla.eventos.repositories.IEventDaysRepository;
import com.unla.eventos.services.IEventDaysService;

@Service
public class EventDaysService implements IEventDaysService {

    @Autowired
    private IEventDaysRepository eventDaysRepository;

    @Autowired
    private IAssistanceDaysRepository assistanceDaysRepository;

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
        List<EventDays> existentes = eventDaysRepository.findByEventId(event.getId());
        if (existentes.isEmpty()) {
            return generarYGuardar(event);
        }

        List<AssistanceDays> assistanceDays = assistanceDaysRepository.findByEventDayId(event.getId());
        if (!assistanceDays.isEmpty()) {
            return existentes;
        }

        LocalDate hoy = LocalDate.now();
        LocalDate inicioExistente = existentes.stream()
            .map(EventDays::getDate)
            .min(LocalDate::compareTo)
            .orElseThrow();

        LocalDate finExistente = existentes.stream()
            .map(EventDays::getDate)
            .max(LocalDate::compareTo)
            .orElseThrow();

        if (!inicioExistente.isAfter(hoy)) {
            return existentes;
        }

        LocalDate nuevoInicio = event.getStartDate().toLocalDate();
        LocalDate nuevoFin = event.getEndDate().toLocalDate();

        long cantidadDias = ChronoUnit.DAYS.between(nuevoInicio, nuevoFin) + 1;

        if(cantidadDias != existentes.size()){
            eventDaysRepository.deleteAll(existentes);
            return generarYGuardar(event);
        }

        if (nuevoInicio.equals(inicioExistente) && nuevoFin.equals(finExistente)) {
           return existentes;
        }

        eventDaysRepository.deleteAll(existentes);
        return generarYGuardar(event);
    }

    private List<EventDays> generarYGuardar(Event event) {
        LocalDate inicio = event.getStartDate().toLocalDate();
        LocalDate fin = event.getEndDate().toLocalDate();

        List<EventDays> nuevos = new ArrayList<>();
        for (LocalDate date = inicio; !date.isAfter(fin); date = date.plusDays(1)) {
            EventDays nuevoDia = new EventDays();
            nuevoDia.setEvent(event);
            nuevoDia.setDate(date);
            nuevos.add(nuevoDia);
        }

        return eventDaysRepository.saveAll(nuevos);
    }

    @Override
    public void deleteByEventId (int eventId) {
        eventDaysRepository.deleteByEventId(eventId);
    }
    
}
