package com.PRISMA.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "coordinacion")
public class Coordinacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_coordinacion")
    private Long idCoordinacion;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grado", referencedColumnName = "id_grado", unique = true)
    private Grado grado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dui_docente", referencedColumnName = "duiDocente")
    private Docente docente;

    public Coordinacion() {
    }

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
}
