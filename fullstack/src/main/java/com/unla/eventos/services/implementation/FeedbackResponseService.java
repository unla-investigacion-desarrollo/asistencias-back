package com.unla.eventos.services.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unla.eventos.entities.Event;
import com.unla.eventos.entities.FeedbackResponse;
import com.unla.eventos.repositories.IFeedbackResponseRepository;
import com.unla.eventos.services.IEventService;
import com.unla.eventos.services.IFeedbackResponseService;

@Service
public class FeedbackResponseService implements IFeedbackResponseService{

    @Autowired
    private IFeedbackResponseRepository feedbackResponseRepository;

    @Autowired
    private IEventService eventService;

    @Override
    public Optional<FeedbackResponse> findByEmailAndEventId(String email, int eventId) {
        return feedbackResponseRepository.findByEmailAndEventId(email, eventId);
    }

    @Override
    public List<FeedbackResponse> findByEventId(int eventId) {
        return feedbackResponseRepository.findByEventId(eventId);
    }

    @Override
    public FeedbackResponse save(FeedbackResponse feedbackResponse) {
        return feedbackResponseRepository.save(feedbackResponse);
    }

    public Optional<Event> findEventByUniqueCode(String uniqueCode) {
        return eventService.findByUniqueCode(uniqueCode);
    }
    
}
