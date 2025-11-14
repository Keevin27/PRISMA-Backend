package com.PRISMA.Alumno.Grado;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.Grado;

@Repository
public interface GradoRepositorio extends JpaRepository<Grado, Integer> {

    @Query("SELECT g FROM Grado g WHERE  g.anioAcademico.anio = :anio")
    List<Grado> buscarGradosPorAnioAcademico(@Param("anio") Integer anio);

    @Query("SELECT g FROM Grado g WHERE g.anioAcademico.anio_activo = true")
    List<Grado> findGradosPorAnioActivo();

    @Query("""
        SELECT g
        FROM Grado g
        WHERE g.anioAcademico.anio_activo = true
        AND g.id_grado NOT IN (
            SELECT b.grado.id_grado
            FROM Bloque b
            WHERE b.materia.codigo_materia = :codigo_materia
        )
    """)
    List<Grado> findGradosDisponiblesPorMateria(@Param("codigo_materia") String codigo_materia);


}
