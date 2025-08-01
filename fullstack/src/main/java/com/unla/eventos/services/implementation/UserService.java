package com.unla.eventos.services.implementation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.unla.eventos.entities.UserRole;
import com.unla.eventos.repositories.IUserRepository;
import com.unla.eventos.repositories.IUserRoleRepository;
import com.unla.eventos.services.IUserService;

@Service("userService")
public class UserService implements UserDetailsService, IUserService {

	private IUserRepository userRepository;
	private IUserRoleRepository userRoleRepository;

	public UserService(IUserRepository userRepository, IUserRoleRepository userRoleRepository) {
		this.userRepository = userRepository;
		this.userRoleRepository = userRoleRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		com.unla.eventos.entities.User user = userRepository.findByUsernameAndFetchUserRolesEagerly(username);
		return buildUser(user, buildGrantedAuthorities(user.getUserRoles()));
	}

	private User buildUser(com.unla.eventos.entities.User user, List<GrantedAuthority> grantedAuthorities) {
		return new User(user.getUsername(), user.getPassword(), user.isEnabled(),
						true, true, true, //accountNonExpired, credentialsNonExpired, accountNonLocked,
						grantedAuthorities);
	}

	private List<GrantedAuthority> buildGrantedAuthorities(Set<UserRole> userRoles) {
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		for(UserRole userRole: userRoles) {
			grantedAuthorities.add(new SimpleGrantedAuthority(userRole.getRole()));
		}
		return new ArrayList<>(grantedAuthorities);
	}

	@Override
	public List<com.unla.eventos.entities.User> findAll() {
        return userRepository.findAll();
    }

	@Override
    public Optional<com.unla.eventos.entities.User> findById(int id) {
        return userRepository.findById(id);
    }

	@Override
    public com.unla.eventos.entities.User save(com.unla.eventos.entities.User user, String role) {
		user.setEnabled(true); //TODO: By now this code don't use states for users
    	user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
    	
    	com.unla.eventos.entities.User userSaved = null;
		Optional<com.unla.eventos.entities.User> oldUser = userRepository.findById(user.getId());
		if(oldUser.isPresent()) {
    		user.setCreatedAt(oldUser.get().getCreatedAt());
    		user.setEnabled(oldUser.get().isEnabled());
    		userSaved = userRepository.save(user);
    		
    		UserRole userRoleSaved = userRoleRepository.findByUserId(user.getId());
    		userRoleSaved.setRole("ROLE_" + role);
    		userRoleRepository.save(userRoleSaved);
    	}else {
    		try {
        		userSaved = userRepository.save(user);
        		UserRole userRole = new UserRole();
                userRole.setUser(userSaved);
                userRole.setRole("ROLE_" + role);
                userRoleRepository.save(userRole);
        	}catch (Exception e) {
    			throw e;
    		}
    	}
    	return userSaved;
    }

	@Override
    public void deleteById(int id) {
		UserRole userRoleSaved = userRoleRepository.findByUserId(id);
		try {
	        userRoleRepository.deleteById(userRoleSaved.getId());
			userRepository.deleteById(id);
		}catch (Exception e) {
			throw e;
		}
    }
	
	@Override
	public List<String> getAllRoles() {
        return List.of("ADMIN", "USER");
    }
}
