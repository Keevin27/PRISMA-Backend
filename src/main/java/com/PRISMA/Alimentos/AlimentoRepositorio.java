package com.PRISMA.Alimentos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.Alimento;

@Repository
public interface AlimentoRepositorio extends JpaRepository<Alimento, Integer>{

    //Sentencia sql que devuelve los alimentos activos
    @Query("SELECT a FROM Alimento a WHERE a.estado_alimento = true")
    List<Alimento> alimentosActivos();
}
