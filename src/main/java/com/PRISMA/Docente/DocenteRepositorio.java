package com.PRISMA.Docente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.Docente;

@Repository
public interface DocenteRepositorio extends JpaRepository<Docente,String>{

}
