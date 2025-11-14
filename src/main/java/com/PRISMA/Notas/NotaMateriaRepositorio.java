package com.PRISMA.Notas;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.NotaMateria;

@Repository
public interface NotaMateriaRepositorio extends JpaRepository<NotaMateria, Long> {
    
    @Query("SELECT nm FROM NotaMateria nm WHERE nm.alumno.nie = :nie AND nm.bloque.id_bloque = :idBloque")
    Optional<NotaMateria> buscarPorAlumnoYBloque(@Param("nie") Integer nie, @Param("idBloque") Long idBloque);
    
    @Query("SELECT nm FROM NotaMateria nm WHERE nm.alumno.nie = :nie")
    List<NotaMateria> buscarPorAlumno(@Param("nie") Integer nie);
}