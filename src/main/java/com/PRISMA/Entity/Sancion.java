package com.PRISMA.Entity;

import java.time.LocalDate; // Usamos LocalDate para fechas

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sancion")
public class Sancion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSancion;

    private String tipoSancion; 
    
    // Ajustado a 255 caracteres
    private String descripcionSancion; 
    
    private LocalDate fechaSancion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_alumno") // Conectamos con Alumno por id_alumno
    private Alumno alumno;

    // Constructores
    public Sancion() {
    }

    // Getters y Setters
    public int getIdSancion() {
        return idSancion;
    }

    public void setIdSancion(int idSancion) {
        this.idSancion = idSancion;
    }

    public String getTipoSancion() {
        return tipoSancion;
    }

    public void setTipoSancion(String tipoSancion) {
        this.tipoSancion = tipoSancion;
    }

    public String getDescripcionSancion() {
        return descripcionSancion;
    }

    public void setDescripcionSancion(String descripcionSancion) {
        this.descripcionSancion = descripcionSancion;
    }

    public LocalDate getFechaSancion() {
        return fechaSancion;
    }

    public void setFechaSancion(LocalDate fechaSancion) {
        this.fechaSancion = fechaSancion;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }
}