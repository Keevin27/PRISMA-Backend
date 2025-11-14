package com.PRISMA.SemanaDia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.Dia;
@Repository
public interface DiaRepositorio extends JpaRepository<Dia, Long> {

}
