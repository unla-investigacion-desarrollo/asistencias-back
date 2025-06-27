package com.unla.eventos.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.unla.eventos.entities.EventDays;

public interface IEventDaysRepository extends JpaRepository<EventDays, Integer>{
    public Optional<EventDays> findById(int id);

    public List<EventDays> findByEventId(int eventId);

    public void deleteByEventId(int eventId);
}
