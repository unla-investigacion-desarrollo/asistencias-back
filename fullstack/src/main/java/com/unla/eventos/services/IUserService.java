package com.unla.eventos.services;

import java.util.List;
import java.util.Optional;

import com.unla.eventos.entities.User;

public interface IUserService {
	public List<User> findAll();

    public Optional<User> findById(int id);

    public User save(User user);

    public void deleteById(int id);
}
