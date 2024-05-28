package com.unla.RestApi.models.response;

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
	
	private String userName;
	private String razon;
	private String nombre;
	private String apellido;
	private String token;
	
}
