package com.unla.asistencias.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Value;
import jakarta.servlet.http.HttpSession;
import com.unla.asistencias.models.response.UserDTO;
import com.unla.asistencias.models.request.UserLogin;

@Controller
@RequestMapping("/user")
public class UsuarioController {

    @Value("${SERVER_API}")
    private String serverAPI;
	
	private final RestTemplate restTemplate;

    public UsuarioController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return  new ModelAndView("login");
    }

	@GetMapping("/home")
    public ModelAndView home(HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> healthCheckResponse = restTemplate.exchange(
            serverAPI + "/api/user/health_check", 
            HttpMethod.GET, 
            entity, 
            String.class
        );

        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("username", user.getName());
        modelAndView.addObject("healthCheck", healthCheckResponse.getBody());
        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView loginSubmit(@RequestParam String username, @RequestParam String password, HttpSession session) {
        UserLogin request = new UserLogin(username, password);
        UserDTO response = restTemplate.postForObject(serverAPI + "/api/user/login", request, UserDTO.class);
		ModelAndView modelAndView = null;
        if (response != null && response.getToken() != null) {
            session.setAttribute("user", response);
            return new ModelAndView("redirect:/user/home");
        } else {
			modelAndView = new ModelAndView("login");
			modelAndView.addObject("error", "Invalid username or password");
        }
		return modelAndView;
    }
}
