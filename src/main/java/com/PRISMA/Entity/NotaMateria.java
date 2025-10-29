package com.PRISMA.Entity;

import jakarta.persistence.*;

@Entity
public class NotaMateria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_notaMateria;

    @ManyToOne
    @JoinColumn(name = "nie", referencedColumnName = "nie")
    private Alumno alumno;

    @ManyToOne
    @JoinColumn(name = "id_bloque", referencedColumnName = "id_bloque")
    private Bloque bloque;

    private Double nota_materia;

    public NotaMateria() {}

    public NotaMateria(Long id_notaMateria, Alumno alumno, Bloque bloque, Double nota_materia) {
        this.id_notaMateria = id_notaMateria;
        this.alumno = alumno;
        this.bloque = bloque;
        this.nota_materia = nota_materia;
    }

    public Long getId_notaMateria() {
        return id_notaMateria;
    }

    public void setId_notaMateria(Long id_notaMateria) {
        this.id_notaMateria = id_notaMateria;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Bloque getBloque() {
        return bloque;
    }

    public void setBloque(Bloque bloque) {
        this.bloque = bloque;
    }

    public Double getNota_materia() {
        return nota_materia;
    }

    public void setNota_materia(Double nota_materia) {
        this.nota_materia = nota_materia;
    }
}
