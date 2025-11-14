package com.PRISMA.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Hora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_hora;

    private String hora_inicio;
    private String hora_fin;

    public Hora(){

    }

    public Hora(Long id_hora, String hora_inicio, String hora_fin) {
        this.id_hora = id_hora;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
    }

    public Long getId_hora() {
        return id_hora;
    }

    public void setId_hora(Long id_hora) {
        this.id_hora = id_hora;
    }

    public String gethora_inicio() {
        return hora_inicio;
    }

    public void sethora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String gethora_fin() {
        return hora_fin;
    }

    public void sethora_fin(String hora_fin) {
        this.hora_fin = hora_fin;
    }
    
}
