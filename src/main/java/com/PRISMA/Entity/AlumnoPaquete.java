package com.PRISMA.Entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "alumno_paquete")
public class AlumnoPaquete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_asignacion;
    private boolean paquete_entregado=false;
    private Date fecha_entrega_p;

    @ManyToOne
    private Alumno alumno;
    @ManyToOne
    private PaqueteEscolar paqueteEscolar;
    
    
    public AlumnoPaquete() {
    }
    public AlumnoPaquete(boolean paquete_entregado, Date fecha_entrega_p, Alumno alumno,
            PaqueteEscolar paqueteEscolar) {
        this.paquete_entregado = paquete_entregado;
        this.fecha_entrega_p = fecha_entrega_p;
        this.alumno = alumno;
        this.paqueteEscolar = paqueteEscolar;
    }


    public int getId_asignacion() {
        return id_asignacion;
    }
    public void setId_asignacion(int id_asignacion) {
        this.id_asignacion = id_asignacion;
    }
    public boolean isPaquete_entregado() {
        return paquete_entregado;
    }
    public void setPaquete_entregado(boolean paquete_entregado) {
        this.paquete_entregado = paquete_entregado;
    }
    public Date getFecha_entrega_p() {
        return fecha_entrega_p;
    }
    public void setFecha_entrega_p(Date fecha_entrega_p) {
        this.fecha_entrega_p = fecha_entrega_p;
    }
    public Alumno getAlumno() {
        return alumno;
    }
    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }
    public PaqueteEscolar getPaqueteEscolar() {
        return paqueteEscolar;
    }
    public void setPaqueteEscolar(PaqueteEscolar paqueteEscolar) {
        this.paqueteEscolar = paqueteEscolar;
    }

    
}
