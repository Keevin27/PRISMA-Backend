package com.PRISMA.PaqueteEscolar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.PaqueteEscolar;

@Repository
public interface PaqueteEscolarRepositorio extends JpaRepository<PaqueteEscolar, Integer>{

}
