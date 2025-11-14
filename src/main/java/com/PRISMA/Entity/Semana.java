package com.PRISMA.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "semana")
public class Semana {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_semana;

    private int numero_semana;

    public Semana(){

    }
    public Semana(int numero_semana) {
        this.numero_semana = numero_semana;
    }
    public int getNumero_semana() {
        return numero_semana;
    }
    public void setNumero_semana(int numero_semana) {
        this.numero_semana = numero_semana;
    }
    public int getId_semana() {
        return id_semana;
    }
    
}
