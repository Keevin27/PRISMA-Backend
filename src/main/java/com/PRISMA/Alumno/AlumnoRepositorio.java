package com.PRISMA.Alumno;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.Alumno;

@Repository
public interface AlumnoRepositorio extends JpaRepository<Alumno, Integer> {

    @Query("SELECT a FROM Alumno a WHERE a.grado.id_grado = :id_grado ORDER BY a.apellido_alumno")
    List<Alumno> buscarPorGrado(@Param("id_grado") Integer id_grado);
}
