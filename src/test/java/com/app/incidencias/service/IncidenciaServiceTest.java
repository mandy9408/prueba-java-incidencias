package com.app.incidencias.service;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.app.incidencias.exception.IncidenciaCerradaException;
import com.app.incidencias.model.Estado;
import com.app.incidencias.model.Incidencia;
import com.app.incidencias.model.Prioridad;
import com.app.incidencias.repository.IncidenciaRepository;
import com.app.incidencias.repository.UsuarioRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class IncidenciaServiceTest {

    private IncidenciaRepository incidenciaRepository;
    private UsuarioRepository usuarioRepository;
    private IncidenciaService incidenciaService;

    @BeforeEach
    void preparar() {
        incidenciaRepository = mock(IncidenciaRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);

        incidenciaService = new IncidenciaService(
                incidenciaRepository,
                usuarioRepository
        );
    }

    @Test
    void permiteCambiarUnaIncidenciaAbiertaAEnCurso() {
        // Preparar
        Incidencia incidencia = new Incidencia(
                "Error de impresora",
                "No imprime",
                Prioridad.ALTA,
                null
        );
       
        when(incidenciaRepository.findById(1L))
                .thenReturn(Optional.of(incidencia));

        when(incidenciaRepository.save(incidencia))
                .thenReturn(incidencia);

        // Ejecutar
        Incidencia resultado =
                incidenciaService.cambiarEstado(1L, Estado.EN_CURSO);

        // Comprobar
        assertEquals(Estado.EN_CURSO, resultado.getEstado());

        verify(incidenciaRepository).save(incidencia);
    }

    @Test
    void impideReabrirUnaIncidenciaCerrada() {
        // Preparar
        Incidencia incidencia = new Incidencia(
                "Error de impresora",
                "No imprime",
                Prioridad.ALTA,
                null
        );

        incidencia.setEstado(Estado.CERRADA);
         //Cuando el servicio busque la incidencia 1, el repositorio simulado devolverá el objeto que he preparado
        when(incidenciaRepository.findById(1L))
                .thenReturn(Optional.of(incidencia));

        // Ejecutar y comprobar
        assertThrows(
                IncidenciaCerradaException.class,
                () -> incidenciaService.cambiarEstado(1L, Estado.ABIERTA)
        );

        assertEquals(Estado.CERRADA, incidencia.getEstado());
        //Compruebo que, al intentar reabrir una incidencia cerrada, se lance la excepción y no se intente guardar ningún cambio
        verify(incidenciaRepository, never()).save(any(Incidencia.class));
    }
}