package com.PRISMA.Materia;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.Materia;

@Repository
public interface MateriaRepositorio extends JpaRepository<Materia,String>{
    //Obtenemos todas las materias activas
    @Query("SELECT m FROM Materia m WHERE m.estado_materia = true")
    List<Materia> obtenerMateriasActivas();

    //ordenar por activos
    @Query("SELECT m FROM Materia m ORDER BY m.estado_materia DESC")
    List<Materia> obtenerMateriasActivasOrdenadas();
}
