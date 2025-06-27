package com.unla.eventos.services;

import java.util.List;
import java.util.Optional;

import com.unla.eventos.entities.Event;
import com.unla.eventos.entities.FeedbackResponse;

public interface IFeedbackResponseService {
    public Optional<FeedbackResponse> findByEmailAndEventId(String email, int eventId);
	
	public List<FeedbackResponse> findByEventId(int eventId);

    public FeedbackResponse save(FeedbackResponse feedbackResponse);

    public Optional<Event> findEventByUniqueCode(String uniqueCode);
}
