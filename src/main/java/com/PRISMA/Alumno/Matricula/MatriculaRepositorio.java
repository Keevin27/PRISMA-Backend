package com.PRISMA.Alumno.Matricula;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.Matricula;

@Repository
public interface MatriculaRepositorio extends JpaRepository<Matricula, Integer> {

    // Busca matrículas por año académico
    @Query("SELECT m FROM Matricula m WHERE m.grado.anioAcademico.anio = :anio")
    List<Matricula> buscarPorAnio(@Param("anio") int anio);

    // Busca matrículas por grado
    @Query("SELECT m FROM Matricula m WHERE m.grado.id_grado = :idGrado")
    List<Matricula> buscarPorGrado(@Param("idGrado") int idGrado);

    // Busca todas las matrículas de un alumno
    @Query("SELECT m FROM Matricula m WHERE m.alumno.idAlumno = :id_alumno")
    List<Matricula> buscarMatriculasPorIdAlumno(@Param("id_alumno") Integer id_alumno);

    // Cuenta alumnos matriculados en un grado específico
    @Query("SELECT COUNT(m) FROM Matricula m WHERE m.grado.id_grado = :idGrado AND m.estadoMatricula = 'Matriculado'")
    Long contarAlumnosPorGrado(@Param("idGrado") Integer idGrado);

    // Lista alumnos activos sin grado asignado
    @Query("SELECT m FROM Matricula m JOIN m.alumno a WHERE m.grado IS NULL AND a.estado_alumno = true")
    List<Matricula> buscarMatriculasSinGrado();
}
