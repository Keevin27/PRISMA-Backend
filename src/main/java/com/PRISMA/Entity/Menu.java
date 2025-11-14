package com.PRISMA.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "menu")
public class Menu {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_menu;

    private String nombre_menu;
    private boolean estado_menu;

    @ManyToOne
	@JoinColumn(name = "id_semana")
	private Semana semana;

    @ManyToOne
	@JoinColumn(name = "id_dia")
	private Dia dia;

    public Menu(){

    }

    public Menu(String nombre_menu, boolean estado_menu, Semana semana, Dia dia) {
        this.nombre_menu = nombre_menu;
        this.estado_menu = estado_menu;
        this.semana = semana;
        this.dia = dia;
    }

    public String getNombre_menu() {
        return nombre_menu;
    }

    public void setNombre_menu(String nombre_menu) {
        this.nombre_menu = nombre_menu;
    }

    public boolean isEstado_menu() {
        return estado_menu;
    }

    public void setEstado_menu(boolean estado_menu) {
        this.estado_menu = estado_menu;
    }

    public Semana getSemana() {
        return semana;
    }

    public void setSemana(Semana semana) {
        this.semana = semana;
    }
    public Dia getDia() {
        return dia;
    }
    public void setDia(Dia dia) {
        this.dia = dia;
    }

    public int getId_menu() {
        return id_menu;
    }

    public void setId_menu(int id_menu) {
        this.id_menu = id_menu;
    }
    
}
