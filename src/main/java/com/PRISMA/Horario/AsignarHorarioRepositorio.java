package com.PRISMA.Horario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.PRISMA.Entity.AsignacionHorario;

public interface AsignarHorarioRepositorio extends JpaRepository<AsignacionHorario, Long> {
    @Query("SELECT a FROM AsignacionHorario a WHERE a.bloque.id_bloque = :idBloque")
    List<AsignacionHorario> findByBloqueId(@Param("idBloque") Long idBloque);

    
    @Query("SELECT a FROM AsignacionHorario a " +
        "JOIN a.bloque b " +
        "JOIN b.grado g " +
        "WHERE g.id_grado = :idGrado " +
        "ORDER BY a.horario.dia.id_dia, a.horario.hora.hora_inicio")
    List<AsignacionHorario> findByGrado(@Param("idGrado") Long idGrado);

    @Query("SELECT a FROM AsignacionHorario a " +
        "JOIN a.bloque b " +
        "JOIN b.docente d " +
        "WHERE d.duiDocente = :duiDocente " +
        "ORDER BY a.horario.dia.id_dia, a.horario.hora.hora_inicio")
    List<AsignacionHorario> findByDocente(@Param("duiDocente") String duiDocente);

     @Query("SELECT COUNT(a) > 0 FROM AsignacionHorario a " +
       "WHERE a.horario.id_Horario = :id_Horario " +
       "AND a.bloque.docente.duiDocente = :duiDocente")
        boolean docenteOcupado(
        @Param("id_Horario") Long id_Horario,
        @Param("duiDocente") String duiDocente);
        
        @Query("SELECT COUNT(a) > 0 FROM AsignacionHorario a " +
       "WHERE a.horario.id_Horario = :idHorario " +
       "AND a.bloque.grado.id_grado = :idGrado")
boolean gradoOcupado(@Param("idHorario") Long idHorario,
                     @Param("idGrado") Long idGrado);



}
