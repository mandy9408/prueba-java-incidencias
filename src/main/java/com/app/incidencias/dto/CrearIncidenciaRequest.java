package com.app.incidencias.dto;

import com.app.incidencias.model.Prioridad;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CrearIncidenciaRequest(

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100, message = "El título no puede superar 100 caracteres")
    String titulo,

    @Size(max = 2000, message = "La descripción no puede superar 2000 caracteres")
    String descripcion,

    @NotNull(message = "La prioridad es obligatoria")
    Prioridad prioridad,

    @Positive(message = "El identificador del usuario debe ser positivo")
    Long usuarioId

) {
}