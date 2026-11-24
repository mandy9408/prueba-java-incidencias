package com.app.incidencias.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.app.incidencias.dto.CrearIncidenciaRequest;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.incidencias.model.Estado;
import com.app.incidencias.model.Incidencia;
import com.app.incidencias.service.IncidenciaService;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.app.incidencias.dto.CambiarEstadoRequest;

@RestController
//URL base para todas las rutas de este controlador.
@RequestMapping("/api/incidencias")
public class IncidenciaController {

    private final IncidenciaService incidenciaService;

    public IncidenciaController(IncidenciaService incidenciaService) {
        this.incidenciaService = incidenciaService;
    }

    @GetMapping
    public List<Incidencia> listar(
            @RequestParam(required = false) Estado estado) {

        return incidenciaService.listar(estado);
    }
//Crear incidencia 
    @PostMapping
    public ResponseEntity<Incidencia> crear(
            @Valid @RequestBody CrearIncidenciaRequest request) {

        Incidencia incidencia = incidenciaService.crear(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(incidencia);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Incidencia> cambiarEstado(
            @PathVariable @NonNull Long id,
            @Valid @RequestBody CambiarEstadoRequest request) {

        Incidencia incidencia = incidenciaService.cambiarEstado(id, request.estado());

        return ResponseEntity.ok(incidencia);
    }
}