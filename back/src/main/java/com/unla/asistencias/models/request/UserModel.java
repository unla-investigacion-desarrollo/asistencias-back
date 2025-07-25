package com.unla.asistencias.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
	private String nombre;
	private String apellido;
	private String email;
	private String password;
	private String rol;
}
