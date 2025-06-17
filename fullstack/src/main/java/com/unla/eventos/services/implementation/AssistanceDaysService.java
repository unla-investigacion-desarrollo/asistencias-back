package com.unla.eventos.services.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unla.eventos.entities.AssistanceDays;
import com.unla.eventos.entities.AssistanceResponse;
import com.unla.eventos.entities.Event;
import com.unla.eventos.entities.EventDays;
import com.unla.eventos.repositories.IAssistanceDaysRepository;
import com.unla.eventos.repositories.IEventDaysRepository;
import com.unla.eventos.services.IAssistanceDaysService;

@Service
public class AssistanceDaysService implements IAssistanceDaysService {

    @Autowired
    private IAssistanceDaysRepository assistanceDaysRepository;

    @Autowired
    private IEventDaysRepository eventDaysRepository;

    @Override
    public Optional<AssistanceDays> findById(int id) {
        return assistanceDaysRepository.findById(id);
    }

    @Override
    public List<AssistanceDays> findByAssistanceResponseId(int id) {
        return assistanceDaysRepository.findByAssistanceResponseId(id);
    }

    @Override
    public List<AssistanceDays> findByEventDaysId(int id) {
        return assistanceDaysRepository.findByEventDayId(id);
    }

    @Override
    public List<AssistanceDays> createAssistanceDaysForResponse(AssistanceResponse assistanceResponse) {
        Event event = assistanceResponse.getEvent();
        List<EventDays> eventDaysList = eventDaysRepository.findByEventId(event.getId());
        List<AssistanceDays> assistanceDaysList = new ArrayList<>();

        for(EventDays day: eventDaysList){
            AssistanceDays assistanceDays = new AssistanceDays();
            assistanceDays.setAssistanceResponse(assistanceResponse);
            assistanceDays.setEventDay(day);
            assistanceDays.setPresent(false);
            assistanceDaysList.add(assistanceDays);
        }

        return assistanceDaysRepository.saveAll(assistanceDaysList);
    }

    @Override
    public AssistanceDays save(AssistanceDays assistanceDays) {
        return assistanceDaysRepository.save(assistanceDays);
    }
    
}
