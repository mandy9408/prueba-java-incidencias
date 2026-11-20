package com.app.incidencias.exception;

public class UsuarioNoExisteException extends RuntimeException {

    public UsuarioNoExisteException(Long id) {
        super("No existe el usuario con id " + id);
    }
}