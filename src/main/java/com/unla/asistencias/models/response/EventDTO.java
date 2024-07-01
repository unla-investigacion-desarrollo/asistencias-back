package com.unla.asistencias.models.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter 
@Setter
public class EventDTO {
	private int id;

	private String name;
	
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String publicFormLink;
}