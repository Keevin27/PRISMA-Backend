package com.PRISMA.Entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Docente {

    @Id
    private String duiDocente;

    private String nombre_Docente;
    private String apellido_Docente;
    private Date fecha_Nacimiento_D;
    private String sexo_Docente;
    private String direccion_D;
    private String municipio_D;
    private String departamento_D;
    private String distrito_D;
    private String correo_Docente;
    private String telefono_Docente;
    private Boolean docente_Activo;
    private Date fecha_Registro_D;

    public Docente(){

    }
    
    public Docente(String duiDocente, String nombre_Docente, String apellido_Docente, Date fecha_Nacimiento_D,
            String sexo_Docente, String direccion_D, String municipio_D, String departamento_D, String distrito_D,
            String correo_Docente, String telefono_Docente, Date fecha_Registro_D) {
        this.duiDocente = duiDocente;
        this.nombre_Docente = nombre_Docente;
        this.apellido_Docente = apellido_Docente;
        this.fecha_Nacimiento_D = fecha_Nacimiento_D;
        this.sexo_Docente = sexo_Docente;
        this.direccion_D = direccion_D;
        this.municipio_D = municipio_D;
        this.departamento_D = departamento_D;
        this.distrito_D = distrito_D;
        this.correo_Docente = correo_Docente;
        this.telefono_Docente = telefono_Docente;
        this.fecha_Registro_D = fecha_Registro_D;
    }


    public String getDuiDocente() {
        return duiDocente;
    }

    public void setDuiDocente(String duiDocente) {
        this.duiDocente = duiDocente;
    }

    public String getNombre_Docente() {
        return nombre_Docente;
    }

    public void setNombre_Docente(String nombre_Docente) {
        this.nombre_Docente = nombre_Docente;
    }

    public String getApellido_Docente() {
        return apellido_Docente;
    }

    public void setApellido_Docente(String apellido_Docente) {
        this.apellido_Docente = apellido_Docente;
    }

    public Date getFecha_Nacimiento_D() {
        return fecha_Nacimiento_D;
    }

    public void setFecha_Nacimiento_D(Date fecha_Nacimiento_D) {
        this.fecha_Nacimiento_D = fecha_Nacimiento_D;
    }

    public String getSexo_Docente() {
        return sexo_Docente;
    }

    public void setSexo_Docente(String sexo_Docente) {
        this.sexo_Docente = sexo_Docente;
    }

    public String getDireccion_D() {
        return direccion_D;
    }

    public void setDireccion_D(String direccion_D) {
        this.direccion_D = direccion_D;
    }

    public String getMunicipio_D() {
        return municipio_D;
    }

    public void setMunicipio_D(String municipio_D) {
        this.municipio_D = municipio_D;
    }

    public String getDepartamento_D() {
        return departamento_D;
    }

    public void setDepartamento_D(String departamento_D) {
        this.departamento_D = departamento_D;
    }

    public String getDistrito_D() {
        return distrito_D;
    }

    public void setDistrito_D(String distrito_D) {
        this.distrito_D = distrito_D;
    }

    public String getCorreo_Docente() {
        return correo_Docente;
    }

    public void setCorreo_Docente(String correo_Docente) {
        this.correo_Docente = correo_Docente;
    }

    public String getTelefono_Docente() {
        return telefono_Docente;
    }

    public void setTelefono_Docente(String telefono_Docente) {
        this.telefono_Docente = telefono_Docente;
    }

    public Boolean isDocente_Activo() {
        return docente_Activo;
    }

    public void setDocente_Activo(Boolean docente_Activo) {
        this.docente_Activo = docente_Activo;
    }

    public Date getFecha_Registro_D() {
        return fecha_Registro_D;
    }

    public void setFecha_Registro_D(Date fecha_Registro_D) {
        this.fecha_Registro_D = fecha_Registro_D;
    }
    
    
}
