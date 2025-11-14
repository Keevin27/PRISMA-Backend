package com.PRISMA.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "detalle_menu")
public class DetalleMenu {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_detalle_menu;

    private float racion_gramos;

    @ManyToOne
	@JoinColumn(name = "id_menu")
	private Menu menu;

    @ManyToOne
	@JoinColumn(name = "id_alimento")
	private Alimento alimento;

    public DetalleMenu(){

    }

    public DetalleMenu(float racion_gramos, Menu menu, Alimento alimento) {
        this.racion_gramos = racion_gramos;
        this.menu = menu;
        this.alimento = alimento;
    }

    public float getRacion_gramos() {
        return racion_gramos;
    }

    public void setRacion_gramos(float racion_gramos) {
        this.racion_gramos = racion_gramos;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Alimento getAlimento() {
        return alimento;
    }

    public void setAlimento(Alimento alimento) {
        this.alimento = alimento;
    }

    public int getId_detalle_menu() {
        return id_detalle_menu;
    }

    public void setId_detalle_menu(int id_detalle_menu) {
        this.id_detalle_menu = id_detalle_menu;
    }

}
