package com.PRISMA.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Dia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_dia;

    private String nombre_dia;

    public Dia(){

    }

    public Dia(Long id_dia, String nombre_dia) {
        this.id_dia = id_dia;
        this.nombre_dia = nombre_dia;
    }

    public Long getId_dia() {
        return id_dia;
    }
    public void setId_dia(Long id_dia) {
        this.id_dia = id_dia;
    }
    public String getNombre_dia() {
        return nombre_dia;
    }
    public void setNombre_dia(String nombre_dia) {
        this.nombre_dia = nombre_dia;
    }
}
