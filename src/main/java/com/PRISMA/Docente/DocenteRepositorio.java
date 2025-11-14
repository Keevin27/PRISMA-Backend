package com.PRISMA.Docente;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.Docente;
import java.util.Optional;
@Repository
public interface DocenteRepositorio extends JpaRepository<Docente,String>{
    //Obtenes docentes activos
    @Query("SELECT d FROM Docente d WHERE d.docente_Activo = true")
    List<Docente> obtenerDocentesActivos();

    //ordenar por activos
    @Query("SELECT d FROM Docente d ORDER BY d.docente_Activo DESC")
    List<Docente> obtenerDocentesActivosOrdenados();

    // Método para buscar docente por correo
    @Query("SELECT d FROM Docente d WHERE d.correo_Docente = :correo")
    Optional<Docente> findByCorreo(@Param("correo") String correo);
}
