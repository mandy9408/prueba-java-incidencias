package com.app.incidencias.exception;

public class IncidenciaCerradaException extends RuntimeException {

    public IncidenciaCerradaException() {
        super("Una incidencia cerrada no puede reabrirse");
    }
}