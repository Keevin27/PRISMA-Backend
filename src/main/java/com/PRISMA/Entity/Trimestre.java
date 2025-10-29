package com.PRISMA.Entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Trimestre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_trimestre;

    private Integer numero_periodo;

    // Relaciones inversas (opcional, si quieres navegar desde trimestre)
    @OneToMany(mappedBy = "trimestre")
    private List<Actividad> actividades;

    @OneToMany(mappedBy = "trimestre")
    private List<NotaTrimestre> notasTrimestre;

    public Trimestre() {}

    public Trimestre(Long id_trimestre, Integer numero_periodo) {
        this.id_trimestre = id_trimestre;
        this.numero_periodo = numero_periodo;
    }

    public Long getId_trimestre() {
        return id_trimestre;
    }

    public void setId_trimestre(Long id_trimestre) {
        this.id_trimestre = id_trimestre;
    }

    public Integer getNumero_periodo() {
        return numero_periodo;
    }

    public void setNumero_periodo(Integer numero_periodo) {
        this.numero_periodo = numero_periodo;
    }
}
