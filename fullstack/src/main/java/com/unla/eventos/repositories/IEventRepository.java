package com.unla.eventos.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.unla.eventos.entities.Event;

public interface IEventRepository extends JpaRepository<Event, Integer> {
	
	public Optional<Event> findByUniqueCode(String uniqueCode);
	
}
