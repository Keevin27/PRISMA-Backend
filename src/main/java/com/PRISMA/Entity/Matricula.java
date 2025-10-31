package com.PRISMA.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "matricula")
public class Matricula {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMatricula;
    private String estadoMatricula;

    @ManyToOne
	@JoinColumn(name = "id_grado")
	private Grado grado;

    @ManyToOne
	@JoinColumn(name = "nie")
	private Alumno alumno;

    public Matricula(){

    }

    public Matricula(String estadoMatricula, Grado grado, Alumno alumno) {
        this.estadoMatricula = estadoMatricula;
        this.grado = grado;
        this.alumno = alumno;
    }

    public int getIdMatricula() {
        return idMatricula;
    }

    public void setIdMatricula(int idMatricula) {
        this.idMatricula = idMatricula;
    }

    public String getEstadoMatricula() {
        return estadoMatricula;
    }

    public void setEstadoMatricula(String estadoMatricula) {
        this.estadoMatricula = estadoMatricula;
    }

    public Grado getGrado() {
        return grado;
    }

    public void setGrado(Grado grado) {
        this.grado = grado;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    
}
