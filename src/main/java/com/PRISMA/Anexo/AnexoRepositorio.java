package com.PRISMA.Anexo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PRISMA.DTO.AnexoDTO;
import com.PRISMA.Entity.Anexo;

@Repository
public interface AnexoRepositorio extends JpaRepository<Anexo, Long> {
    @Query("SELECT new com.PRISMA.DTO.AnexoDTO(a.id_Anexo_D, a.nombre_Anexo_D, a.fecha_Anexo_D) " +
       "FROM Anexo a WHERE a.docente.duiDocente = :duiDocente")
    List<AnexoDTO> findAnexosDTOByDocente(@Param("duiDocente") String duiDocente);
}
