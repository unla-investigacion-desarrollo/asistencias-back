package com.unla.asistencias.configuration.Seguridad;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter{
	
	public static final String AUTHORIZATION = "Authorization";
	private final JwtService jwtService;

	public JwtAuthorizationFilter(JwtService jwtService){
		this.jwtService = jwtService;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader(AUTHORIZATION);
		if (jwtService.isBearer(authHeader)){
			String user = jwtService.getUser(authHeader);
			List<GrantedAuthority> authorities;
			try {
				authorities = jwtService.getRoles(authHeader).stream()
						.map( role -> new SimpleGrantedAuthority(role) ).collect( Collectors.toList() );
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		filterChain.doFilter(request, response);
	}	
}