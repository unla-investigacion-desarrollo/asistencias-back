package com.unla.eventos.services.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unla.eventos.entities.AssistanceDays;
import com.unla.eventos.repositories.IAssistanceDaysRepository;
import com.unla.eventos.services.IAssistanceDaysService;

@Service
public class AssistanceDaysService implements IAssistanceDaysService {

    @Autowired
    private IAssistanceDaysRepository assistanceDaysRepository;

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
    
}
