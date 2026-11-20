package com.app.incidencias.dto;

import com.app.incidencias.model.Estado;

import jakarta.validation.constraints.NotNull;

public record CambiarEstadoRequest(

    @NotNull(message = "El estado es obligatorio")
    Estado estado

) {
}