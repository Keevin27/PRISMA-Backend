package com.PRISMA.Notas;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.NotaActividad;

@Repository
public interface NotaActividadRepositorio extends JpaRepository<NotaActividad, Long> {
    
    @Query("SELECT na FROM NotaActividad na WHERE na.actividad.id_actividad = :idActividad ORDER BY na.alumno.apellido_alumno, na.alumno.nombre_alumno")
    List<NotaActividad> buscarPorActividad(@Param("idActividad") Long idActividad);
    
    @Query("SELECT na FROM NotaActividad na WHERE na.actividad.id_actividad = :idActividad AND na.alumno.nie = :nie")
    Optional<NotaActividad> buscarPorActividadYAlumno(@Param("idActividad") Long idActividad, @Param("nie") Integer nie);
    
    @Query("SELECT na FROM NotaActividad na WHERE na.alumno.nie = :nie AND na.actividad.bloque.id_bloque = :idBloque ORDER BY na.actividad.trimestre.numero_periodo, na.actividad.fecha_actividad")
    List<NotaActividad> buscarPorAlumnoYBloque(@Param("nie") Integer nie, @Param("idBloque") Long idBloque);
}

