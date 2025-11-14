package com.PRISMA.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_Horario;

    @ManyToOne
    @JoinColumn(name = "id_hora",referencedColumnName = "id_hora")
    private Hora hora;

    @ManyToOne
    @JoinColumn(name = "id_dia", referencedColumnName = "id_dia")
    private Dia dia;

    public Horario(){

    }

    public Horario(Long id_Horario, Hora hora, Dia dia) {
        this.id_Horario = id_Horario;
        this.hora = hora;
        this.dia = dia;
    }

    public Long getId_Horario() {
        return id_Horario;
    }

    public void setId_Horario(Long id_Horario) {
        this.id_Horario = id_Horario;
    }

    public Hora getHora() {
        return hora;
    }

    public void setHora(Hora hora) {
        this.hora = hora;
    }

    public Dia getDia() {
        return dia;
    }

    public void setDia(Dia dia) {
        this.dia = dia;
    }
    
}
