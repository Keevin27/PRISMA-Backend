package com.PRISMA.Alimentos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.Menu;

@Repository
public interface MenuRepositorio extends JpaRepository<Menu, Integer>{

    @Query("SELECT m FROM Menu m WHERE m.estado_menu = :estado")
    List<Menu> listarMenusActivos(@Param("estado") boolean estado);

    @Query("SELECT m FROM Menu m " +
           "WHERE m.semana.id_semana = :idSemana " +
           "AND m.dia.id_dia = :idDia " +
           "AND m.estado_menu = true")
    Optional<Menu> findBySemanaAndDia(
        @Param("idSemana") int idSemana,
        @Param("idDia") int idDia
    );
}
