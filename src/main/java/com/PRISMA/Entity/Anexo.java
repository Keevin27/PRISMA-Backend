package com.PRISMA.Entity;

import java.sql.Date;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

@Entity
public class Anexo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_Anexo_D;

    private String nombre_Anexo_D;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] datos_Anexo_D;
    private Date fecha_Anexo_D;
    // Relación con Docente
    @ManyToOne
    @JoinColumn(name = "duiDocente", referencedColumnName = "duiDocente")
    private Docente docente;
    
    public String getNombre_Anexo_D() {
        return nombre_Anexo_D;
    }
    public void setNombre_Anexo_D(String nombre_Anexo_D) {
        this.nombre_Anexo_D = nombre_Anexo_D;
    }
    public byte[] getDatos_Anexo_D() {
        return datos_Anexo_D;
    }
    public void setDatos_Anexo_D(byte[] datos_Anexo_D) {
        this.datos_Anexo_D = datos_Anexo_D;
    }
    public Date getFecha_Anexo_D() {
        return fecha_Anexo_D;
    }
    public void setFecha_Anexo_D(Date fecha_Anexo_D) {
        this.fecha_Anexo_D = fecha_Anexo_D;
    }
    public Docente getDocente() {
        return docente;
    }
    public void setDocente(Docente docente) {
        this.docente = docente;
    }
    public Long getId_Anexo_D() {
        return id_Anexo_D;
    }
    public void setId_Anexo_D(Long id_Anexo_D) {
        this.id_Anexo_D = id_Anexo_D;
    }
}
