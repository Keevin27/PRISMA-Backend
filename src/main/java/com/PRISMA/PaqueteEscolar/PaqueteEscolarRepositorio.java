package com.PRISMA.PaqueteEscolar;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.PaqueteEscolar;

@Repository
public interface PaqueteEscolarRepositorio extends JpaRepository<PaqueteEscolar, Integer>{

    @Query("SELECT p FROM PaqueteEscolar p WHERE p.paquete_activo = true")
    List<PaqueteEscolar> paquetesActivos();
}
