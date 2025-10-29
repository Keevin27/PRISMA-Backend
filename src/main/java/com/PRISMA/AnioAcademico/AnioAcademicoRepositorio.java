package com.PRISMA.AnioAcademico;

import org.springframework.data.jpa.repository.JpaRepository;

import com.PRISMA.Entity.AnioAcademico;

public interface AnioAcademicoRepositorio extends JpaRepository<AnioAcademico, Integer> {
    boolean existsByAnio(int anio);
}