package com.unla.eventos.entities;

import jakarta.persistence.Entity;
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
public class AssistanceDays {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

    @ManyToOne
    @JoinColumn(name = "assistance_response_id")
    private AssistanceResponse assistanceResponse;

    @ManyToOne
    @JoinColumn(name = "event_days_id")
    private EventDays eventDay;

    private boolean isPresent;
}
