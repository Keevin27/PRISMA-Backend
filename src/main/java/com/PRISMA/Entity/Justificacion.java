package com.PRISMA.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "justificacion")

public class Justificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_justificacion;
    private String descripcion_justificacion;
    @Lob
    private byte[] archivo;

    @OneToOne
    @JoinColumn(name = "id_asistencia")
    private AsistenciaAlumno asistenciaAlumno;

    public Justificacion(){

    }

    

    public Justificacion(int id_justificacion, String descripcion_justificacion, byte[] archivo,
            AsistenciaAlumno asistenciaAlumno) {
        this.id_justificacion = id_justificacion;
        this.descripcion_justificacion = descripcion_justificacion;
        this.archivo = archivo;
        this.asistenciaAlumno = asistenciaAlumno;
    }



    public int getId_justificacion() {
        return id_justificacion;
    }

    public void setId_justificacion(int id_justificacion) {
        this.id_justificacion = id_justificacion;
    }

    public String getDescripcion_justificacion() {
        return descripcion_justificacion;
    }

    public void setDescripcion_justificacion(String descripcion_justificacion) {
        this.descripcion_justificacion = descripcion_justificacion;
    }

    public byte[] getArchivo() {
        return archivo;
    }

    public void setArchivo(byte[] archivo) {
        this.archivo = archivo;
    }

    public AsistenciaAlumno getAsistenciaAlumno() {
        return asistenciaAlumno;
    }

    public void setAsistenciaAlumno(AsistenciaAlumno asistenciaAlumno) {
        this.asistenciaAlumno = asistenciaAlumno;
    }

       


}
