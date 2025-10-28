package com.PRISMA.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Bloque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_bloque;

    @ManyToOne
    @JoinColumn(name = "id_grado",referencedColumnName = "id_grado")
    private Grado grado;

    @ManyToOne
    @JoinColumn(name = "duiDocente", referencedColumnName = "duiDocente")
    private Docente docente;

    @ManyToOne
    @JoinColumn(name = "codigo_materia", referencedColumnName = "codigo_materia")
    private Materia materia;
    private Integer anioAcademico;

    public Bloque(){}

    
    public Bloque(Long id_bloque, Grado grado, Docente docente, Materia materia, Integer anioAcademico) {
        this.id_bloque = id_bloque;
        this.grado = grado;
        this.docente = docente;
        this.materia = materia;
        this.anioAcademico = anioAcademico;
    }

    public Long getId_bloque() {
        return id_bloque;
    }

    public void setId_bloque(Long id_bloque) {
        this.id_bloque = id_bloque;
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

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }


    public Integer getAnioAcademico() {
        return anioAcademico;
    }


    public void setAnioAcademico(Integer anioAcademico) {
        this.anioAcademico = anioAcademico;
    }
    

    
}
