package com.PRISMA.Alumno;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.Alumno;

@Repository
public interface AlumnoRepositorio extends JpaRepository<Alumno, Integer>{

}
