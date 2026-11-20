package com.app.incidencias.exception;

public record ApiError(int status, String mensaje) {
}