package com.PRISMA.PaqueteEscolar.AlumnoPaquete;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.AlumnoPaquete;

@Repository
public interface AlumnoPaqueteRepositorio extends JpaRepository<AlumnoPaquete, Integer> {

    @Query("SELECT a FROM AlumnoPaquete a WHERE a.fecha_entrega_p BETWEEN :inicio AND :fin")
    List<AlumnoPaquete> findByFechaEntre(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    
    @Query("SELECT a FROM AlumnoPaquete a WHERE a.alumno.grado.id_grado = :idGrado AND a.fecha_entrega_p BETWEEN :inicio AND :fin")
    List<AlumnoPaquete> findByGradoAndAnioEntrega(@Param("idGrado") int idGrado, @Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

}
