package com.PRISMA.AnioAcademico;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.PRISMA.Entity.AnioAcademico;

public interface AnioAcademicoRepositorio extends JpaRepository<AnioAcademico, Integer> {
    boolean existsByAnio(int anio);

     //
    @Query("SELECT a FROM AnioAcademico a WHERE a.anio_activo = true")
    Optional<AnioAcademico> findByAnioActivoTrue();
    
    Optional<AnioAcademico> findByAnio(int anio);
}