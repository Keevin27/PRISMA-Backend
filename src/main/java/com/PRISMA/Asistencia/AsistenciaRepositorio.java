package com.PRISMA.Asistencia;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.AsistenciaAlumno;


@Repository
public interface AsistenciaRepositorio extends JpaRepository<AsistenciaAlumno, Integer>{
    
  

    @Query("SELECT a FROM AsistenciaAlumno a WHERE a.alumno.grado.id_grado = :idGrado AND a.fecha_asistencia BETWEEN :inicio AND :fin")
    List<AsistenciaAlumno> findByGradoAndAnioAsistenciaAlumnos(@Param("idGrado") int idGrado, @Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    

}
