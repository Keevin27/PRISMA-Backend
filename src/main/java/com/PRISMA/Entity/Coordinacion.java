package com.PRISMA.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "coordinacion")
public class Coordinacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_coordinacion")
    private Long idCoordinacion;

    // Relación OneToOne: Un grado solo puede tener un coordinador
    @OneToOne
    @JoinColumn(name = "id_grado", referencedColumnName = "id_grado", unique = true, nullable = false)
    private Grado grado;

    // Relación ManyToOne: Un docente puede ser coordinador de varios grados (en diferentes años académicos)
    @ManyToOne
    @JoinColumn(name = "dui_docente", referencedColumnName = "duiDocente", nullable = false)
    private Docente docente;

    // Constructores
    public Coordinacion() {
    }

    public Coordinacion(Grado grado, Docente docente) {
        this.grado = grado;
        this.docente = docente;
    }

    // Getters y Setters
    public Long getIdCoordinacion() {
        return idCoordinacion;
    }

    public void setIdCoordinacion(Long idCoordinacion) {
        this.idCoordinacion = idCoordinacion;
    }

    public Grado getGrado() {
        return grado;
    }

    public void setGrado(Grado grado) {
        this.grado = grado;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    @Override
    public String toString() {
        return "Coordinacion [idCoordinacion=" + idCoordinacion + 
               ", grado=" + (grado != null ? grado.getNombre_grado() + " " + grado.getSeccion() : "null") + 
               ", docente=" + (docente != null ? docente.getNombre_Docente() + " " + docente.getApellido_Docente() : "null") + "]";
    }
}