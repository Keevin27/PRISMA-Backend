package com.PRISMA.Asistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.AsistenciaAlumno;


@Repository
public interface AsistenciaRepositorio extends JpaRepository<AsistenciaAlumno, Integer>{

}
