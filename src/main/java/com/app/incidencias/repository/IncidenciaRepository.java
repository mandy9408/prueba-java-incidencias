package com.app.incidencias.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.incidencias.model.Estado;
import com.app.incidencias.model.Incidencia;
//Consulta y guarda datos.

public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {

    List<Incidencia> findByEstado(Estado estado);
}