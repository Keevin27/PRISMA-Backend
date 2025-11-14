package com.PRISMA.Entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "asistencia_alumno")
public class AsistenciaAlumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_asistencia;
    private String estado_asistencia;
    private Date fecha_asistencia;

    @ManyToOne
    private Alumno alumno;


    public AsistenciaAlumno() {

    }
    

    public AsistenciaAlumno(int id_asistencia, String estado_asistencia, Date fecha_asistencia, Alumno alumno) {
        this.id_asistencia = id_asistencia;
        this.estado_asistencia = estado_asistencia;
        this.fecha_asistencia = fecha_asistencia;
        this.alumno = alumno;
    }


    public int getId_asistencia() {
        return id_asistencia;
    }

    public void setId_asistencia(int id_asistencia) {
        this.id_asistencia = id_asistencia;
    }

    public String getEstado_asistencia() {
        return estado_asistencia;
    }

    public void setEstado_asistencia(String estado_asistencia) {
        this.estado_asistencia = estado_asistencia;
    }

    public Date getFecha_asistencia() {
        return fecha_asistencia;
    }

    public void setFecha_asistencia(Date fecha_asistencia) {
        this.fecha_asistencia = fecha_asistencia;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }
    

}
