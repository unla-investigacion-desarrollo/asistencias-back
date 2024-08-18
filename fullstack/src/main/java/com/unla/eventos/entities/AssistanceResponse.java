package com.unla.eventos.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssistanceResponse {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

    private String lastName;
	
    private String documentNumber;

    private String email;
    
    private String miembro;
    
    private String rolPrincipal;

    private String rolPrincipalOtro;
    
    private String investigadorCarreras;

    private String investigadorCarrerasOtro;
    
    private String tipoInscripcion;
    
    private String source;

    @Column(length = 500)
    private String QRCode;

    private boolean isPresent;

    private boolean isAssistanceCertifySent;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="event_id", nullable=false)
	private Event event;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

	@Override
	public String toString() {
		return "AssistanceResponse [id=" + id + ", name=" + name + ", lastName=" + lastName + ", documentNumber="
				+ documentNumber + ", email=" + email + ", miembro=" + miembro + ", rolPrincipal=" + rolPrincipal
				+ ", rolPrincipalOtro=" + rolPrincipalOtro + ", investigadorCarreras=" + investigadorCarreras
				+ ", investigadorCarrerasOtro=" + investigadorCarrerasOtro + ", tipoInscripcion=" + tipoInscripcion
				+ ", source=" + source + ", QRCode=" + QRCode + ", isPresent=" + isPresent
				+ ", isAssistanceCertifySent=" + isAssistanceCertifySent + ", event=" + event.getId() + ", createdAt="
				+ createdAt + ", updatedAt=" + updatedAt + "]";
	}
}
