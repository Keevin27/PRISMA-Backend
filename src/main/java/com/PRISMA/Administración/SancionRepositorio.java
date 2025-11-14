package com.PRISMA.Administración;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.Sancion;

@Repository
public interface SancionRepositorio extends JpaRepository<Sancion, Integer> {

    /**
     * Busca todas las sanciones de un alumno específico, ordenadas por fecha (la más nueva primero).
     */
    List<Sancion> findByAlumno_IdAlumnoOrderByFechaSancionDesc(Integer idAlumno);
    
    /**
     * Busca sanciones de un alumno en un año específico
     */
    @Query("SELECT s FROM Sancion s WHERE s.alumno.idAlumno = :idAlumno " +
           "AND YEAR(s.fechaSancion) = :anio " +
           "ORDER BY s.fechaSancion DESC")
    List<Sancion> findByAlumnoAndAnio(@Param("idAlumno") Integer idAlumno, 
                                       @Param("anio") Integer anio);
}