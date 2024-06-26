package com.unla.asistencias.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter 
@Setter
public class UserDTO {
	
	public UserDTO() {
	}
	
	private String email;
	private String name;
	private String lastname;
	private String password;
	private String role;
	private String token;
	
}
