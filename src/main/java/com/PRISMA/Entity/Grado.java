package com.PRISMA.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name="grado")
public class Grado {
    //CAMPOS DE LA TABLAS GRADO
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_grado;
    private String nombre_grado;
    private String seccion;
    @ManyToOne
    @JoinColumn(name = "anio_academico_id")
    private AnioAcademico anioAcademico;

    //CONSTRUCTORES
    public Grado() {
    }
    public Grado(int id_grado, String nombre_grado, String seccion, AnioAcademico anioAcademico) {
        this.id_grado = id_grado;
        this.nombre_grado = nombre_grado;
        this.seccion = seccion;
        this.anioAcademico = anioAcademico;
    }
    //GETTERS Y SETTERS 
    public int getId_grado() {
        return id_grado;
    }
    public void setId_grado(int id_grado) {
        this.id_grado = id_grado;
    }
    public String getNombre_grado() {
        return nombre_grado;
    }
    public void setNombre_grado(String nombre_grado) {
        this.nombre_grado = nombre_grado;
    }
    public String getSeccion() {
        return seccion;
    }
    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }
    public AnioAcademico getAnioAcademico() {
        return anioAcademico;
    }
    public void setAnioAcademico(AnioAcademico anioAcademico) {
        this.anioAcademico = anioAcademico;
    }

    @Override
    public String toString() {
        return "Grado [NombreGrado = " + nombre_grado
                + ", seccion=" + seccion +"]";
    }
}
