package com.PRISMA.DTO;

import java.sql.Date;

public class AnexoDTO {
    private Long id_Anexo_D;
    private String nombre_Anexo_D;
    private Date fecha_Anexo_D;
    
    public AnexoDTO(Long id, String nombre, Date fecha) {
        this.id_Anexo_D = id;
        this.nombre_Anexo_D = nombre;
        this.fecha_Anexo_D = fecha;
    }

    public Long getId_Anexo_D() {
        return id_Anexo_D;
    }

    public void setId_Anexo_D(Long id_Anexo_D) {
        this.id_Anexo_D = id_Anexo_D;
    }

    public String getNombre_Anexo_D() {
        return nombre_Anexo_D;
    }

    public void setNombre_Anexo_D(String nombre_Anexo_D) {
        this.nombre_Anexo_D = nombre_Anexo_D;
    }

    public Date getFecha_Anexo_D() {
        return fecha_Anexo_D;
    }

    public void setFecha_Anexo_D(Date fecha_Anexo_D) {
        this.fecha_Anexo_D = fecha_Anexo_D;
    }
}
