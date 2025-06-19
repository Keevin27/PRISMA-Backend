package com.PRISMA.Asistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.Justificacion;


@Repository
public interface JustificacionRepositorio extends JpaRepository<Justificacion, Integer>{

}
