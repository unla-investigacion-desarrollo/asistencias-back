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
    
    private boolean welcomeMailSent;

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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getQRCode() {
        return QRCode;
    }

    public boolean isIsPresent() {
        return isPresent;
    }

    public boolean isIsAssistanceCertifySent() {
        return isAssistanceCertifySent;
    }

    public Event getEvent() {
        return event;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setQRCode(String QRCode) {
        this.QRCode = QRCode;
    }

    public void setIsPresent(boolean isPresent) {
        this.isPresent = isPresent;
    }

    public void setIsAssistanceCertifySent(boolean isAssistanceCertifySent) {
        this.isAssistanceCertifySent = isAssistanceCertifySent;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getMiembro() {
        return miembro;
    }

    public String getRolPrincipal() {
        return rolPrincipal;
    }

    public String getRolPrincipalOtro() {
        return rolPrincipalOtro;
    }

    public String getInvestigadorCarreras() {
        return investigadorCarreras;
    }

    public String getInvestigadorCarrerasOtro() {
        return investigadorCarrerasOtro;
    }

    public String getTipoInscripcion() {
        return tipoInscripcion;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isWelcomeMailSent() {
        return welcomeMailSent;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean isPresent) {
        this.isPresent = isPresent;
    }

    public boolean isAssistanceCertifySent() {
        return isAssistanceCertifySent;
    }

    public void setAssistanceCertifySent(boolean isAssistanceCertifySent) {
        this.isAssistanceCertifySent = isAssistanceCertifySent;
    }

    public void setMiembro(String miembro) {
        this.miembro = miembro;
    }

    public void setRolPrincipal(String rolPrincipal) {
        this.rolPrincipal = rolPrincipal;
    }

    public void setRolPrincipalOtro(String rolPrincipalOtro) {
        this.rolPrincipalOtro = rolPrincipalOtro;
    }

    public void setInvestigadorCarreras(String investigadorCarreras) {
        this.investigadorCarreras = investigadorCarreras;
    }

    public void setInvestigadorCarrerasOtro(String investigadorCarrerasOtro) {
        this.investigadorCarrerasOtro = investigadorCarrerasOtro;
    }

    public void setTipoInscripcion(String tipoInscripcion) {
        this.tipoInscripcion = tipoInscripcion;
    }

    public void setWelcomeMailSent(boolean welcomeMailSent) {
        this.welcomeMailSent = welcomeMailSent;
    }

    
}
