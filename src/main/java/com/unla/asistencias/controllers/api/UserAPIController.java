package com.unla.asistencias.controllers.api;

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
import com.unla.asistencias.models.response.UserDTO;
import com.unla.asistencias.models.request.UserLogin;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/user")
public class UserAPIController {
	
	private InternalUserServices internalUserServices;			
	
	public UserAPIController(InternalUserServices internalUserServices) {
		this.internalUserServices = internalUserServices;
	}

	@PostMapping("/login")
	public UserDTO autenticarUsuario(@RequestBody UserLogin request) {
		return internalUserServices.autenticarUsuario(request);
	}

	@GetMapping("/health_check")
	@PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.status(HttpStatus.OK).body("System works!");		
    }
}
