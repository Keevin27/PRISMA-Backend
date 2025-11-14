package com.PRISMA.Alumno.Matricula;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.Matricula;

@Repository
public interface MatriculaRepositorio extends JpaRepository<Matricula, Integer> {

    @Query("SELECT m FROM Matricula m WHERE m.grado.anioAcademico.anio = :anio")
    List<Matricula> buscarPorAnio(@Param("anio") int anio);

    @Query("SELECT m FROM Matricula m WHERE m.grado.id = :idGrado")
    List<Matricula> buscarPorGrado(@Param("idGrado") int idGrado);

    @Query("SELECT m FROM Matricula m WHERE m.alumno.idAlumno = :id_alumno")
    Optional<Matricula> buscarMatriculaPorIdAlumno(@Param("id_alumno") Integer id_alumno);

    //Contar alumnos matriculados en un grado específico
    @Query("SELECT COUNT(m) FROM Matricula m WHERE m.grado.id_grado = :idGrado AND m.estadoMatricula = 'Matriculado'")
    Long contarAlumnosPorGrado(@Param("idGrado") Integer idGrado);

    // Consulta para buscar alumnos activos sin grado asignado
    @Query("SELECT m FROM Matricula m JOIN m.alumno a WHERE m.grado IS NULL AND a.estado_alumno = true")
    List<Matricula> buscarMatriculasSinGrado();

}
