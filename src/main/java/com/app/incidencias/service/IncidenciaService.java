package com.app.incidencias.service;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.incidencias.dto.CrearIncidenciaRequest;
import com.app.incidencias.exception.UsuarioNoExisteException;
import com.app.incidencias.model.Estado;
import com.app.incidencias.model.Incidencia;
import com.app.incidencias.model.Usuario;
import com.app.incidencias.repository.IncidenciaRepository;
import com.app.incidencias.repository.UsuarioRepository;
import com.app.incidencias.exception.IncidenciaCerradaException;
import com.app.incidencias.exception.IncidenciaNoExisteException;

@Service
public class IncidenciaService {

    private final IncidenciaRepository incidenciaRepository;
    private final UsuarioRepository usuarioRepository;

    public IncidenciaService(
            IncidenciaRepository incidenciaRepository,
            UsuarioRepository usuarioRepository) {

        this.incidenciaRepository = incidenciaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Incidencia> listar(Estado estado) {
        if (estado == null) {
            return incidenciaRepository.findAll();
        }

        return incidenciaRepository.findByEstado(estado);
    }

    @SuppressWarnings("null")
    @Transactional
    public Incidencia crear(CrearIncidenciaRequest request) {
        Usuario usuario = null;

        if (request.usuarioId() != null) {
            usuario = usuarioRepository.findById(request.usuarioId())
                    .orElseThrow(() ->
                            new UsuarioNoExisteException(request.usuarioId()));
        }

        Incidencia incidencia = new Incidencia(
                request.titulo(),
                request.descripcion(),
                request.prioridad(),
                usuario
        );

        return incidenciaRepository.save(incidencia);
    }

    @Transactional
public Incidencia cambiarEstado(@NonNull Long id, Estado nuevoEstado) {

    Incidencia incidencia = incidenciaRepository.findById(id)
            .orElseThrow(() -> new IncidenciaNoExisteException(id));

    if (incidencia.getEstado() == Estado.CERRADA
            && nuevoEstado != Estado.CERRADA) {

        throw new IncidenciaCerradaException();
    }

    incidencia.setEstado(nuevoEstado);

    return incidenciaRepository.save(incidencia);
}
}