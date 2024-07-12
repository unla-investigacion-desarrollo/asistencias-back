package com.unla.eventos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unla.eventos.entities.AssistanceResponse;

@Repository
public interface IAssistanceResponseRepository extends JpaRepository<AssistanceResponse, Integer> {
	public AssistanceResponse findByQRCode(String QRCode);
}
