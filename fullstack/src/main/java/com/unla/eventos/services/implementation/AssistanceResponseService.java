package com.unla.eventos.services.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unla.eventos.entities.AssistanceResponse;
import com.unla.eventos.entities.Event;
import com.unla.eventos.repositories.IAssistanceResponseRepository;
import com.unla.eventos.services.IAssistanceResponseService;
import com.unla.eventos.services.IEventService;

@Service
public class AssistanceResponseService implements IAssistanceResponseService {

    @Autowired
    private IAssistanceResponseRepository assistanceResponseRepository;
    
    @Autowired
    private IEventService eventService;

    public Optional<Event> findByPublicFormLink(String code) {
        return eventService.findByPublicFormLink(code);
    }
    
    public AssistanceResponse save(AssistanceResponse assistanceResponse) {
        return assistanceResponseRepository.save(assistanceResponse);
    }
}
