package com.PRISMA.Administracion;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.Coordinacion;
import com.PRISMA.Entity.Grado;

@Repository
public interface CoordinacionRepositorio extends JpaRepository<Coordinacion, Long> {

    // Buscar coordinación por ID de grado
    @Query("SELECT c FROM Coordinacion c WHERE c.grado.id_grado = :idGrado")
    Optional<Coordinacion> findByGradoId(@Param("idGrado") Integer idGrado);

    // Verificar si un grado ya tiene coordinador asignado
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Coordinacion c WHERE c.grado.id_grado = :idGrado")
    boolean existsByGradoId(@Param("idGrado") Integer idGrado);

    // Buscar todas las coordinaciones de un docente
    @Query("SELECT c FROM Coordinacion c WHERE c.docente.duiDocente = :duiDocente")
    List<Coordinacion> findByDocenteDui(@Param("duiDocente") String duiDocente);

    // Buscar coordinaciones por año académico
    @Query("SELECT c FROM Coordinacion c WHERE c.grado.anioAcademico.id = :anioAcademicoId")
    List<Coordinacion> findByAnioAcademico(@Param("anioAcademicoId") Integer anioAcademicoId);

    // Obtener grados que NO tienen coordinador asignado
    @Query("SELECT g FROM Grado g WHERE g.id_grado NOT IN (SELECT c.grado.id_grado FROM Coordinacion c)")
    List<Grado> findGradosSinCoordinador();

    // Buscar coordinación específica por docente y grado
    @Query("SELECT c FROM Coordinacion c WHERE c.docente.duiDocente = :duiDocente AND c.grado.id_grado = :idGrado")
    Optional<Coordinacion> findByDocenteAndGrado(@Param("duiDocente") String duiDocente, @Param("idGrado") Integer idGrado);

    @Query("SELECT c FROM Coordinacion c " +
           "JOIN FETCH c.grado " +
           "JOIN FETCH c.docente " +
           "ORDER BY c.grado.nombre_grado")
    List<Coordinacion> findAllWithGradoAndDocente();
    
}