package com.unla.eventos.entities;

import java.time.LocalDate;

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
public class EventDays {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    private LocalDate date;
}
