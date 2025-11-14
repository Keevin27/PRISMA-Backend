package com.PRISMA.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "anio_academico")
public class AnioAcademico {
    //CAMPOS DE LA TABLA ANIO_ACADEMICO
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_anio_academico;
    private int anio;
    private boolean anio_activo;
    private boolean anio_cerrado;
    
    //CONSTRUCTORES VACIO Y CON PARAMETROS
    public AnioAcademico() {
    }
    public AnioAcademico(int id_anio_academico, int anio, boolean anio_activo, boolean anio_cerrado) {
        this.id_anio_academico = id_anio_academico;
        this.anio = anio;
        this.anio_activo = anio_activo;
        this.anio_cerrado = anio_cerrado;
    }
    //GETTERS Y SETTERS
    public int getId_anio_academico() {
        return id_anio_academico;
    }
    public void setId_anio_academico(int id_anio_academico) {
        this.id_anio_academico = id_anio_academico;
    }
    public int getAnio() {
        return anio;
    }
    public void setAnio(int anio) {
        this.anio = anio;
    }
    public boolean isAnio_activo() {
        return anio_activo;
    }
    public void setAnio_activo(boolean anio_activo) {
        this.anio_activo = anio_activo;
    }
    public boolean isAnio_cerrado() {
        return anio_cerrado;
    }
    public void setAnio_cerrado(boolean anio_cerrado) {
        this.anio_cerrado = anio_cerrado;
    }

    

}
