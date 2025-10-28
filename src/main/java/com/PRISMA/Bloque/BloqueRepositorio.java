package com.PRISMA.Bloque;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.Bloque;

@Repository
public interface BloqueRepositorio extends JpaRepository<Bloque, Long> {
    @Query("SELECT b FROM Bloque b WHERE b.materia.codigo_materia = :codigoMateria")
    List<Bloque> findByCodigoMateria(@Param("codigoMateria") String codigoMateria);
}

