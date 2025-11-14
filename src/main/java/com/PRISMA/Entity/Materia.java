package com.PRISMA.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Materia {
    @Id
    private String codigo_materia;
    private String nombre_materia;
    private String tipo_materia;
    private Boolean estado_materia;

    public Materia(){}

    public Materia(String codigo_materia, String nombre_materia, String tipo_materia,
            Boolean estado_materia) {
        this.codigo_materia = codigo_materia;
        this.nombre_materia = nombre_materia;
        this.tipo_materia = tipo_materia;
        this.estado_materia = estado_materia;
    }

    public String getCodigo_materia() {
        return codigo_materia;
    }

    public void setCodigo_materia(String codigo_materia) {
        this.codigo_materia = codigo_materia;
    }

    public String getNombre_materia() {
        return nombre_materia;
    }

    public void setNombre_materia(String nombre_materia) {
        this.nombre_materia = nombre_materia;
    }

    public String getTipo_materia() {
        return tipo_materia;
    }

    public void setTipo_materia(String tipo_materia) {
        this.tipo_materia = tipo_materia;
    }

    public Boolean getEstado_materia() {
        return estado_materia;
    }

    public void setEstado_materia(Boolean estado_materia) {
        this.estado_materia = estado_materia;
    }
    
}
