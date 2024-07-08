package com.unla.asistencias.services.implementation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.unla.asistencias.models.database.Event;
import com.unla.asistencias.models.response.EventDTO;
import com.unla.asistencias.repositories.IEventRepository;
import com.unla.asistencias.services.IEventService;

@Service
public class EventService implements IEventService {

    private IEventRepository eventRepository;

    private ModelMapper modelMapper;

    public EventService(IEventRepository eventRepository, ModelMapper modelMapper){
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
    }

    private String generateUniqueURL() {
        String uniqueID = UUID.randomUUID().toString();
        return "http://localhost:8080/evento/" + uniqueID;
    }

    private EventDTO convertToDTO(Event event) {
        return modelMapper.map(event, EventDTO.class);
    }

    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<EventDTO> getEventById(int id) {
        return eventRepository.findById(id).map(this::convertToDTO);
    }

    public EventDTO createEvent(Event event) {
        event.setPublicFormLink(generateUniqueURL());
        return convertToDTO(eventRepository.save(event));
    }

    public EventDTO updateEvent(int id, Event eventDetails) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        event.setName(eventDetails.getName());
        event.setStartDate(eventDetails.getStartDate());
        event.setEndDate(eventDetails.getEndDate());
        event.setPublicFormLink(eventDetails.getPublicFormLink());
        return convertToDTO(eventRepository.save(event));
    }
}
