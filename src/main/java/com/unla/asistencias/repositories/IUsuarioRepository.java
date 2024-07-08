package com.unla.asistencias.repositories;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unla.asistencias.models.database.InternalUser;

@Repository("usuarioRepository")
public interface IUsuarioRepository extends JpaRepository<InternalUser,Serializable> {
	
	Optional<InternalUser> findByEmail(String email);
}