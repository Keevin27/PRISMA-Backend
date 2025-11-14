package com.PRISMA.Notas;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.Actividad;

@Repository
public interface ActividadRepositorio extends JpaRepository<Actividad, Long> {
    
    @Query("SELECT a FROM Actividad a WHERE a.bloque.id_bloque = :idBloque AND a.trimestre.numero_periodo = :numeroPeriodo ORDER BY a.fecha_actividad")
    List<Actividad> buscarPorBloqueYTrimestre(@Param("idBloque") Long idBloque, @Param("numeroPeriodo") Integer numeroPeriodo);
    
    @Query("SELECT COUNT(a) FROM Actividad a WHERE a.bloque.id_bloque = :idBloque AND a.trimestre.numero_periodo = :numeroPeriodo")
    Long contarActividadesPorBloqueYTrimestre(@Param("idBloque") Long idBloque, @Param("numeroPeriodo") Integer numeroPeriodo);
}
