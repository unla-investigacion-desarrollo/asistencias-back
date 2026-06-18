package com.unla.eventos.configuration;

import java.io.IOException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        String errorParam;
        if (exception instanceof UsernameNotFoundException) {
            errorParam = "usuario_no_existe";
        } else if (exception instanceof BadCredentialsException) {
            errorParam = "credenciales_invalidas";
        } else if (exception instanceof DisabledException) {
            errorParam = "usuario_deshabilitado";
        } else {
            errorParam = "error_general";
        }
        request.getSession().setAttribute("loginError", errorParam);
        response.sendRedirect("/eventos/login");
    }
}
