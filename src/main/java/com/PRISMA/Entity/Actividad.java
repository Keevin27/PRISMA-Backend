package com.PRISMA.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_actividad;

    @ManyToOne
    @JoinColumn(name = "id_bloque", referencedColumnName = "id_bloque")
    private Bloque bloque;

    @ManyToOne
    @JoinColumn(name = "id_trimestre", referencedColumnName = "id_trimestre")
    private Trimestre trimestre;

    private String nombre_actividad;
    private Double ponderacion_actividad;
    private LocalDate fecha_actividad;

    public Actividad() {}

    public Actividad(Long id_actividad, Bloque bloque, Trimestre trimestre, String nombre_actividad,
                     Double ponderacion_actividad, LocalDate fecha_actividad) {
        this.id_actividad = id_actividad;
        this.bloque = bloque;
        this.trimestre = trimestre;
        this.nombre_actividad = nombre_actividad;
        this.ponderacion_actividad = ponderacion_actividad;
        this.fecha_actividad = fecha_actividad;
    }

    public Long getId_actividad() {
        return id_actividad;
    }

    public void setId_actividad(Long id_actividad) {
        this.id_actividad = id_actividad;
    }

    public Bloque getBloque() {
        return bloque;
    }

    public void setBloque(Bloque bloque) {
        this.bloque = bloque;
    }

    public Trimestre getTrimestre() {
        return trimestre;
    }

    public void setTrimestre(Trimestre trimestre) {
        this.trimestre = trimestre;
    }

    public String getNombre_actividad() {
        return nombre_actividad;
    }

    public void setNombre_actividad(String nombre_actividad) {
        this.nombre_actividad = nombre_actividad;
    }

    public Double getPonderacion_actividad() {
        return ponderacion_actividad;
    }

    public void setPonderacion_actividad(Double ponderacion_actividad) {
        this.ponderacion_actividad = ponderacion_actividad;
    }

    public LocalDate getFecha_actividad() {
        return fecha_actividad;
    }

    public void setFecha_actividad(LocalDate fecha_actividad) {
        this.fecha_actividad = fecha_actividad;
    }
}
