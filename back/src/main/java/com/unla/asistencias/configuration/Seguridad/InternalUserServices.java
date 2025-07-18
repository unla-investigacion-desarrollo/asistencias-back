package com.unla.asistencias.configuration.Seguridad;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.unla.asistencias.models.database.InternalUser;
import com.unla.asistencias.models.request.UserLogin;
import com.unla.asistencias.models.response.UserDTO;
import com.unla.asistencias.repositories.IUsuarioRepository;

@Service
public class InternalUserServices implements UserDetailsService{
	
	private IUsuarioRepository usuarioRepository;
	private JwtService jwtService;

	public InternalUserServices(IUsuarioRepository usuarioRepository, JwtService jwtService){
		this.usuarioRepository = usuarioRepository;
		this.jwtService = jwtService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<InternalUser> usOp = usuarioRepository.findByEmail(username);
		
		InternalUser us = usOp.get();
		String Rol = "ROLE_" + us.getRole();
		
		List <GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
		roles.add(new SimpleGrantedAuthority(Rol));
		
		UserDetails UsDetails = new User(us.getEmail(), us.getPassword(), roles);
		return UsDetails;
	}

	public UserDTO autenticarUsuario(UserLogin request) {
		UserDTO response = new UserDTO();
		try {
			Optional<InternalUser> user = usuarioRepository.findByEmail(request.getEmail());	
			UserDetails UsDetails = loadUserByUsername(request.getEmail());
			if (UsDetails.getPassword() != null) {
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				if (passwordEncoder.matches(request.getPassword(), UsDetails.getPassword()) ) {
					List<String> roles = UsDetails.getAuthorities().stream().map
						(authority -> authority.getAuthority()).collect(Collectors.toList());					
					String token = jwtService.createToken(UsDetails.getUsername(), roles);
					response.setName(user.get().getName());
					response.setLastname(user.get().getLastName());
					response.setEmail(user.get().getEmail());
					response.setToken(token);
				}
			}
		} catch (Exception e) {
			System.out.println("System Error: " + e.getMessage());
		}
		return response;
	}
}