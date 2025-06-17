package com.PRISMA.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "paquete_escolar")
public class PaqueteEscolar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_paquete_e;
    private String nombre_paquete;
    private boolean paquete_activo = true;
    
    public PaqueteEscolar() {
    }

    public PaqueteEscolar(String nombre_paquete) {
        this.nombre_paquete = nombre_paquete;
    }
    
    public String getNombre_paquete() {
        return nombre_paquete;
    }
    public void setNombre_paquete(String nombre_paquete) {
        this.nombre_paquete = nombre_paquete;
    }

    public int getId_paquete_e() {
        return id_paquete_e;
    }

    public void setId_paquete_e(int id_paquete_e) {
        this.id_paquete_e = id_paquete_e;
    }

    public boolean isPaquete_activo() {
        return paquete_activo;
    }

    public void setPaquete_activo(boolean paquete_activo) {
        this.paquete_activo = paquete_activo;
    }

    
}
