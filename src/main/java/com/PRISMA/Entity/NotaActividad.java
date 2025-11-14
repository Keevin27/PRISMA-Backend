package com.PRISMA.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class NotaActividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_notaActividad;

    @ManyToOne
    @JoinColumn(name = "nie", referencedColumnName = "nie")
    private Alumno alumno;

    @ManyToOne
    @JoinColumn(name = "id_actividad", referencedColumnName = "id_actividad")
    private Actividad actividad;

    private Double nota_obtenida;
    private LocalDate fecha_modificacion;

    public NotaActividad() {}

    public NotaActividad(Long id_notaActividad, Alumno alumno, Actividad actividad, Double nota_obtenida,
                         LocalDate fecha_modificacion) {
        this.id_notaActividad = id_notaActividad;
        this.alumno = alumno;
        this.actividad = actividad;
        this.nota_obtenida = nota_obtenida;
        this.fecha_modificacion = fecha_modificacion;
    }

    public Long getId_notaActividad() {
        return id_notaActividad;
    }

    public void setId_notaActividad(Long id_notaActividad) {
        this.id_notaActividad = id_notaActividad;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public Double getNota_obtenida() {
        return nota_obtenida;
    }

    public void setNota_obtenida(Double nota_obtenida) {
        this.nota_obtenida = nota_obtenida;
    }

    public LocalDate getFecha_modificacion() {
        return fecha_modificacion;
    }

    public void setFecha_modificacion(LocalDate fecha_modificacion) {
        this.fecha_modificacion = fecha_modificacion;
    }
}