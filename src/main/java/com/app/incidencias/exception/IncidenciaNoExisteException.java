package com.app.incidencias.exception;

public class IncidenciaNoExisteException extends RuntimeException {

    public IncidenciaNoExisteException(Long id) {
        super("No existe la incidencia con id " + id);
    }
}