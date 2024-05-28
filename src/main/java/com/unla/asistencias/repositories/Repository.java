package com.unla.asistencias.repositories;

public class Repository {
	private IUsuarioRepository usuarioRepository;
	
	public Repository(IUsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	public IUsuarioRepository getUsuarioRepository () {
		return usuarioRepository;
	}	
}
