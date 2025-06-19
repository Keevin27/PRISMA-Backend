package com.PRISMA.Alumno.Grado;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.Grado;

@Repository
public interface GradoRepositorio extends JpaRepository<Grado, Integer> {

    List<Grado> findByAnioAcademico_anio(Integer anio);

}
