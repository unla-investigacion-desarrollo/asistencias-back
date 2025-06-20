package com.unla.eventos.services;

import java.util.List;
import java.util.Optional;

import com.unla.eventos.entities.AssistanceResponse;
import com.unla.eventos.entities.Event;
import java.io.InputStream;

public interface IAssistanceResponseService {
	
	public int importFromExcel(InputStream is, int eventId) throws Exception;
	
	public void sendMailsForNewResponses(int eventId) throws Exception;
	
	public Optional<Event> findEventByUniqueCode(String uniqueCode);
	
	public Optional<Event> findEventById(int eventId);
	
	public AssistanceResponse findByQRCode(String QRCode);
	
	public Optional<AssistanceResponse> findByEmailAndEventId(String email, int eventId);
	
    public AssistanceResponse save(AssistanceResponse assistanceResponse);
    
    public List<AssistanceResponse> findByEventId(int eventId);

	public List<AssistanceResponse> findByEventIdWithEvent(int eventId);
}
