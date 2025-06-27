package com.unla.eventos.entities;

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
public class FeedbackResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

    private String email;
    
    private int edad;
    
    private String rol;
    
    private String areaEstudio;
    
    private String calificacionRegistro;
    
    private String comentario;
    
    private String calificacionOrganizacion;

    private String calificacionPresentaciones;

    private String calificacionDuracion;

    private String calificacionClaridad;

    private String calificacionInteraccion;

    private String calificacionInstalaciones;

    private String calificacionRecursos;

    private String calificacionBreak;

    private String aspectosPositivos;

    private String aspectosAMejorar;

    private String sugerenciaEspecifica;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="event_id", nullable=false)
	private Event event;
}
