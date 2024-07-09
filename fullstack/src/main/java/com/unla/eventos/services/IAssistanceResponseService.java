package com.unla.eventos.services;

import java.util.Optional;

import com.unla.eventos.entities.AssistanceResponse;
import com.unla.eventos.entities.Event;

public interface IAssistanceResponseService {
	
	public Optional<Event> findByPublicFormLink(String code);
	
    public AssistanceResponse save(AssistanceResponse assistanceResponse);
}
