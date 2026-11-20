package com.app.incidencias.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.incidencias.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}