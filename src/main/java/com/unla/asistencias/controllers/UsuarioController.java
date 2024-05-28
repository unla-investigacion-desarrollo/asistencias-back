package com.unla.asistencias.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unla.asistencias.configuration.Seguridad.InternalUserServices;
import com.unla.asistencias.models.response.HelloDTO;
import com.unla.asistencias.models.response.UserDTO;
import com.unla.asistencias.models.request.UsuarioLogin;

@RestController
@CrossOrigin("*")
@RequestMapping("/usuario")
public class UsuarioController {
	
	private InternalUserServices internalUserServices;			
	
	public UsuarioController(InternalUserServices internalUserServices) {
		this.internalUserServices = internalUserServices;
	}

	@PostMapping("/login")
	public UserDTO autenticarUsuario(@RequestBody UsuarioLogin request) {
		return internalUserServices.autenticarUsuario(request);
	}

	@PostMapping("/access")
	public List<String> access(@RequestBody UserDTO user) throws Exception {
		return internalUserServices.access(user.getToken());
	}

	@GetMapping("/hello")
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HelloDTO> hello(){
		HelloDTO hello = new HelloDTO("Hello Wold!", "UNLa");
        return ResponseEntity.status(HttpStatus.OK).body(hello);		
    }
}
