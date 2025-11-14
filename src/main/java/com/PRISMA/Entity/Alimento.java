package com.PRISMA.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "alimento")
public class Alimento {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_alimento;

    private String nombre_alimento;
    
    private boolean estado_alimento=true;

    public Alimento() {
    }

    public Alimento(String nombre_alimento, boolean estado_alimento) {
        this.nombre_alimento = nombre_alimento;
        this.estado_alimento = estado_alimento;
    }

    public String getNombre_alimento() {
        return nombre_alimento;
    }

    public void setNombre_alimento(String nombre_alimento) {
        this.nombre_alimento = nombre_alimento;
    }

    public int getId_alimento() {
        return id_alimento;
    }

    public void setId_alimento(int id_alimento) {
        this.id_alimento = id_alimento;
    }

    public boolean isEstado_alimento() {
        return estado_alimento;
    }

    public void setEstado_alimento(boolean estado_alimento) {
        this.estado_alimento = estado_alimento;
    }

    
}
