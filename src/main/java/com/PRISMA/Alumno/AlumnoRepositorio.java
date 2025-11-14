package com.PRISMA.Alumno;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.Alumno;

@Repository
public interface AlumnoRepositorio extends JpaRepository<Alumno, Integer> {


    // Buscar alumnos por grado (ordenados por apellido)
    @Query("SELECT a FROM Alumno a JOIN Matricula m ON m.alumno.idAlumno = a.idAlumno WHERE m.grado.id_grado = :id_grado ORDER BY a.apellido_alumno")
    List<Alumno> buscarPorGrado(@Param("id_grado") Integer id_grado);

    // Buscar alumno por NIE
    @Query("SELECT a FROM Alumno a WHERE a.nie = :nie")
    Optional<Alumno> buscarPorNie(@Param("nie") Integer nie);

    // Buscar solo alumnos activos
    @Query("SELECT a FROM Alumno a WHERE a.estado_alumno = true ORDER BY a.apellido_alumno, a.nombre_alumno")
    List<Alumno> buscarAlumnosActivos();

     // Contar alumnos por grado
    @Query("SELECT COUNT(a) FROM Alumno a JOIN Matricula m ON m.alumno.idAlumno = a.idAlumno WHERE m.grado.id_grado = :id_grado AND a.estado_alumno = true")
    Long contarAlumnosPorGrado(@Param("id_grado") Integer id_grado);

    // Verificar si existe un NIE (para validaciones)
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Alumno a WHERE a.nie = :nie")
    boolean existePorNie(@Param("nie") Integer nie);

}
