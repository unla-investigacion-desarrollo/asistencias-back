package com.unla.eventos.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.unla.eventos.entities.AssistanceResponse;

@Repository
public interface IAssistanceResponseRepository extends JpaRepository<AssistanceResponse, Integer> {
	
	@Query("SELECT ar FROM AssistanceResponse ar JOIN FETCH ar.event WHERE ar.QRCode = :QRCode")
	public AssistanceResponse findByQRCode(String QRCode);
	
	public Optional<AssistanceResponse> findByEmailAndEventId(String email, int eventId);
	
	public List<AssistanceResponse> findByEventId(int eventId);
	
	public List<AssistanceResponse> findByEventIdAndWelcomeMailSent(int eventId, boolean welcomeMailSent);
}
