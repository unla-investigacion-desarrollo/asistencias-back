package com.unla.asistencias.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter 
@Setter
public class HelloDTO {
	
	public HelloDTO() {
	}
	
	private String msj;
	private String nombre;
	
}
