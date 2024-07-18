package com.unla.eventos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.unla.eventos.entities.UserRole;

@Repository
public interface IUserRoleRepository extends JpaRepository<UserRole, Integer> {
	
	@Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId")
    UserRole findByUserId(int userId);
}
