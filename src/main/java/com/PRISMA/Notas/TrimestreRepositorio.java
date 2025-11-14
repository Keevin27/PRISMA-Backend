package com.PRISMA.Notas;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.Trimestre;

@Repository
public interface TrimestreRepositorio extends JpaRepository<Trimestre, Long> {
    
    @Query("SELECT t FROM Trimestre t WHERE t.numero_periodo = :numeroPeriodo")
    Optional<Trimestre> buscarPorNumeroPeriodo(@Param("numeroPeriodo") Integer numeroPeriodo);
}