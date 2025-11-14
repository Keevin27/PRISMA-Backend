package com.PRISMA.Alimentos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.PRISMA.Entity.DetalleMenu;

@Repository
public interface DetalleMenuRepositorio extends JpaRepository<DetalleMenu, Integer>{

    @Query("SELECT d FROM DetalleMenu d WHERE d.menu.id_menu = :id_menu")
    List<DetalleMenu> findbyIdMenu(int id_menu);
}
