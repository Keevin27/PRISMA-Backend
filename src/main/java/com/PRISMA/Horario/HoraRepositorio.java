package com.PRISMA.Horario;

import org.springframework.data.jpa.repository.JpaRepository;

import com.PRISMA.Entity.Hora;

public interface HoraRepositorio extends JpaRepository<Hora, Long> {
    
}
