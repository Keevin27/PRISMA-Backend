package com.PRISMA.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class AsignacionHorario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_asignacionHorario;

    @ManyToOne
    @JoinColumn(name = "id_horario",referencedColumnName = "id_horario")
    private Horario horario;

    @ManyToOne
    @JoinColumn(name = "id_bloque",referencedColumnName = "id_bloque")
    private Bloque bloque;

    public AsignacionHorario(){
    }

    public AsignacionHorario(Long id_asignacionHorario, Horario horario, Bloque bloque) {
        this.id_asignacionHorario = id_asignacionHorario;
        this.horario = horario;
        this.bloque = bloque;
    }

    public Long getId_asignacionHorario() {
        return id_asignacionHorario;
    }

    public void setId_asignacionHorario(Long id_asignacionHorario) {
        this.id_asignacionHorario = id_asignacionHorario;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public Bloque getBloque() {
        return bloque;
    }

    public void setBloque(Bloque bloque) {
        this.bloque = bloque;
    }
    
}
