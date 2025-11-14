package com.PRISMA.Horario;

import org.springframework.data.jpa.repository.JpaRepository;

import com.PRISMA.Entity.Horario;

public interface HorarioRepositorio extends JpaRepository<Horario, Long> {
    
}
