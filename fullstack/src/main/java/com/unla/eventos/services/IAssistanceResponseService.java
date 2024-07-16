package com.unla.eventos.services;

import java.util.Optional;

import com.unla.eventos.entities.AssistanceResponse;
import com.unla.eventos.entities.Event;

public interface IAssistanceResponseService {
	
	public Optional<Event> findByUniqueCode(String uniqueCode);
	
	public AssistanceResponse findByQRCode(String QRCode);
	
	public Optional<AssistanceResponse> findByEmail(String email);
	
    public AssistanceResponse save(AssistanceResponse assistanceResponse);
}
