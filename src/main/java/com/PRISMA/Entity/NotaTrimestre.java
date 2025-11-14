package com.PRISMA.Entity;

import jakarta.persistence.*;

@Entity
public class NotaTrimestre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_notaTrimestre;

    @ManyToOne
    @JoinColumn(name = "id_bloque", referencedColumnName = "id_bloque")
    private Bloque bloque;

    @ManyToOne
    @JoinColumn(name = "id_trimestre", referencedColumnName = "id_trimestre")
    private Trimestre trimestre;

    @ManyToOne
    @JoinColumn(name = "nie", referencedColumnName = "nie")
    private Alumno alumno;

    private Double nota_trimestre;

    public NotaTrimestre() {}

    public NotaTrimestre(Long id_notaTrimestre, Bloque bloque, Trimestre trimestre, Alumno alumno, Double nota_trimestre) {
        this.id_notaTrimestre = id_notaTrimestre;
        this.bloque = bloque;
        this.trimestre = trimestre;
        this.alumno = alumno;
        this.nota_trimestre = nota_trimestre;
    }

    public Long getId_notaTrimestre() {
        return id_notaTrimestre;
    }

    public void setId_notaTrimestre(Long id_notaTrimestre) {
        this.id_notaTrimestre = id_notaTrimestre;
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

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Double getNota_trimestre() {
        return nota_trimestre;
    }

    public void setNota_trimestre(Double nota_trimestre) {
        this.nota_trimestre = nota_trimestre;
    }
}
