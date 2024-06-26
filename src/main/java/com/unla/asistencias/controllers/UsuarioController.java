package com.unla.asistencias.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.unla.asistencias.models.response.UserDTO;
import com.unla.asistencias.models.request.UserLogin;

@Controller
@RequestMapping("/user")
public class UsuarioController {
	
	private final RestTemplate restTemplate;

    public UsuarioController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return  new ModelAndView("login");
    }

	@GetMapping("/home")
    public ModelAndView home() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        String token = "";
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }
            if (authentication.getCredentials() instanceof String) {
                token = (String) authentication.getCredentials();
            }
        }
        ModelAndView modelAndView = new ModelAndView("home");
		modelAndView.addObject("username", username);
        modelAndView.addObject("token", token);
		modelAndView.addObject("healthCheck", "healthCheckResponse.getBody()");
        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView loginSubmit(@RequestParam String username, @RequestParam String password) {
        UserLogin request = new UserLogin(username, password);
        UserDTO response = restTemplate.postForObject("http://localhost:8080/api/user/login", request, UserDTO.class);
		ModelAndView modelAndView = null;
        if (response != null && response.getToken() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(response.getToken());
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> healthCheckResponse = restTemplate.exchange(
                "http://localhost:8080/api/user/health_check", 
                HttpMethod.GET, 
                entity, 
                String.class
            );

			modelAndView = new ModelAndView("home");
            modelAndView.addObject("username", username);
			modelAndView.addObject("token", response.getToken());
			modelAndView.addObject("healthCheck", healthCheckResponse.getBody());
            return modelAndView;
        } else {
			modelAndView = new ModelAndView("login");
			modelAndView.addObject("error", "Invalid username or password");
        }
		return modelAndView;
    }
}
