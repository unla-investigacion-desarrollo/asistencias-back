package com.unla.eventos.services;

import java.util.List;
import java.util.Optional;

import com.unla.eventos.entities.AssistanceDays;
import com.unla.eventos.entities.AssistanceResponse;

public interface IAssistanceDaysService {
    public Optional<AssistanceDays> findById(int id);

    public List<AssistanceDays> findByAssistanceResponseId(int id);

    public List<AssistanceDays> findByEventDaysId(int id);

    public List<AssistanceDays> createAssistanceDaysForResponse(AssistanceResponse assistanceResponse);

    public AssistanceDays save(AssistanceDays assistanceDays);
}
