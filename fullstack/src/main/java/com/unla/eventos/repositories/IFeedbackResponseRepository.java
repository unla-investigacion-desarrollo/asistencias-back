package com.unla.eventos.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unla.eventos.entities.FeedbackResponse;

public interface IFeedbackResponseRepository extends JpaRepository<FeedbackResponse, Integer> {
    public Optional<FeedbackResponse> findByEmailAndEventId(String email, int eventId);
	
	public List<FeedbackResponse> findByEventId(int eventId);
}
