package com.unla.eventos.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unla.eventos.entities.AssistanceDays;

public interface IAssistanceDaysRepository extends JpaRepository<AssistanceDays, Integer>{
    public Optional<AssistanceDays> findById(int id);

    public List<AssistanceDays> findByAssistanceResponseId(int id);

    public List<AssistanceDays> findByEventDayId(int id);
}
