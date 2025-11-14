package com.PRISMA.Bloque;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.Bloque;

@Repository
public interface BloqueRepositorio extends JpaRepository<Bloque, Long> {
    @Query("SELECT b FROM Bloque b WHERE b.materia.codigo_materia = :codigoMateria")
    List<Bloque> findByCodigoMateria(@Param("codigoMateria") String codigoMateria);

    @Query("SELECT b FROM Bloque b WHERE b.docente.duiDocente = :duiDocente AND b.materia.codigo_materia = :codigoMateria AND b.grado.id_grado = :idGrado")
    Optional<Bloque> findByDocenteAndMateriaAndGrado(
        @Param("duiDocente") String duiDocente,
        @Param("codigoMateria") String codigoMateria,
        @Param("idGrado") Integer idGrado);
    
    // Obtener bloques por DUI del docente
    @Query("SELECT b FROM Bloque b WHERE b.docente.duiDocente = :duiDocente")
    List<Bloque> findByDuiDocente(@Param("duiDocente") String duiDocente);
    
}

