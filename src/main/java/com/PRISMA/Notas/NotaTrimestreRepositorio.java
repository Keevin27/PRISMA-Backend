package com.PRISMA.Notas;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.NotaTrimestre;

@Repository
public interface NotaTrimestreRepositorio extends JpaRepository<NotaTrimestre, Long> {
    
    @Query("SELECT nt FROM NotaTrimestre nt WHERE nt.alumno.nie = :nie AND nt.bloque.id_bloque = :idBloque AND nt.trimestre.numero_periodo = :numeroPeriodo")
    Optional<NotaTrimestre> buscarPorAlumnoBloqueYTrimestre(@Param("nie") Integer nie, @Param("idBloque") Long idBloque, @Param("numeroPeriodo") Integer numeroPeriodo);
    
    @Query("SELECT nt FROM NotaTrimestre nt WHERE nt.alumno.nie = :nie AND nt.bloque.id_bloque = :idBloque ORDER BY nt.trimestre.numero_periodo")
    List<NotaTrimestre> buscarPorAlumnoYBloque(@Param("nie") Integer nie, @Param("idBloque") Long idBloque);
    
    @Query("SELECT nt FROM NotaTrimestre nt WHERE nt.bloque.id_bloque = :idBloque AND nt.trimestre.numero_periodo = :numeroPeriodo")
    List<NotaTrimestre> buscarPorBloqueYTrimestre(@Param("idBloque") Long idBloque, @Param("numeroPeriodo") Integer numeroPeriodo);
}
