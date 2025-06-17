package com.PRISMA.Entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="alumnos")
public class Alumno {
	//CAMPOS DE LA TABLA ALUMNOS
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAlumno;
    private int nie;
    private String nombre_alumno;
    private String apellido_alumno;
    private String correo_alumno;
    private Date fecha_nacimiento_alumno;
    private String direccion_a;
    private String sexo_a;
    private String telefono_alumno;
	private String enfermedades;
    private String medicamento;
    private String vive_con;
    private String parentezco_encargado;
    private String telefono_encargado;
    private String correo_encargado;
    private String dui_encargado;
    private String lugar_de_trabajo;
    private boolean estado_alumno;

	@ManyToOne
	private Grado grado;

	//CONSTRUCTORES VACIOS Y CON PARAMETROS
	public Alumno() {
	}
	
	public Alumno(int nie, String nombre_alumno, String apellido_alumno, String correo_alumno,
			Date fecha_nacimiento_alumno, String direccion_a,
			String sexo_a, String telefono_alumno, String enfermedades, String medicamento,
			String vive_con, String parentezco_encargado, String telefono_encargado, String correo_encargado,
			String dui_encargado, String lugar_de_trabajo, boolean estado_alumno, Grado grado) {
		this.nie = nie;
		this.nombre_alumno = nombre_alumno;
		this.apellido_alumno = apellido_alumno;
		this.correo_alumno = correo_alumno;
		this.fecha_nacimiento_alumno = fecha_nacimiento_alumno;
		this.direccion_a = direccion_a;
		this.sexo_a = sexo_a;
		this.telefono_alumno = telefono_alumno;
		this.enfermedades = enfermedades;
		this.medicamento = medicamento;
		this.vive_con = vive_con;
		this.parentezco_encargado = parentezco_encargado;
		this.telefono_encargado = telefono_encargado;
		this.correo_encargado = correo_encargado;
		this.dui_encargado = dui_encargado;
		this.lugar_de_trabajo = lugar_de_trabajo;
		this.estado_alumno = estado_alumno;
		this.grado = grado;
	}

	//GETTERS Y SETTERS
	
	public int getNie() {
		return nie;
	}
	public void setNie(int nie) {
		this.nie = nie;
	}
	public String getNombre_alumno() {
		return nombre_alumno;
	}
	public void setNombre_alumno(String nombre_alumno) {
		this.nombre_alumno = nombre_alumno;
	}
	public String getApellido_alumno() {
		return apellido_alumno;
	}
	public void setApellido_alumno(String apellido_alumno) {
		this.apellido_alumno = apellido_alumno;
	}
	public String getCorreo_alumno() {
		return correo_alumno;
	}
	public void setCorreo_alumno(String correo_alumno) {
		this.correo_alumno = correo_alumno;
	}
	public Date getFecha_nacimiento_alumno() {
		return fecha_nacimiento_alumno;
	}
	public void setFecha_nacimiento_alumno(Date fecha_nacimiento_alumno) {
		this.fecha_nacimiento_alumno = fecha_nacimiento_alumno;
	}
	public String getDireccion_a() {
		return direccion_a;
	}
	public void setDireccion_a(String direccion_a) {
		this.direccion_a = direccion_a;
	}
	public String getSexo_a() {
		return sexo_a;
	}
	public void setSexo_a(String sexo_a) {
		this.sexo_a = sexo_a;
	}
	public String getTelefono_alumno() {
		return telefono_alumno;
	}
	public void setTelefono_alumno(String telefono_alumno) {
		this.telefono_alumno = telefono_alumno;
	}
	public String getEnfermedades() {
		return enfermedades;
	}
	public void setEnfermedades(String enfermedades) {
		this.enfermedades = enfermedades;
	}
	public String getMedicamento() {
		return medicamento;
	}
	public void setMedicamento(String medicamento) {
		this.medicamento = medicamento;
	}
	public String getVive_con() {
		return vive_con;
	}
	public void setVive_con(String vive_con) {
		this.vive_con = vive_con;
	}
	public String getParentezco_encargado() {
		return parentezco_encargado;
	}
	public void setParentezco_encargado(String parentezco_encargado) {
		this.parentezco_encargado = parentezco_encargado;
	}
	public String getTelefono_encargado() {
		return telefono_encargado;
	}
	public void setTelefono_encargado(String telefono_encargado) {
		this.telefono_encargado = telefono_encargado;
	}
	public String getCorreo_encargado() {
		return correo_encargado;
	}
	public void setCorreo_encargado(String correo_encargado) {
		this.correo_encargado = correo_encargado;
	}
	public String getDui_encargado() {
		return dui_encargado;
	}
	public void setDui_encargado(String dui_encargado) {
		this.dui_encargado = dui_encargado;
	}
	public String getLugar_de_trabajo() {
		return lugar_de_trabajo;
	}
	public void setLugar_de_trabajo(String lugar_de_trabajo) {
		this.lugar_de_trabajo = lugar_de_trabajo;
	}
	public boolean isEstado_alumno() {
		return estado_alumno;
	}
	public void setEstado_alumno(boolean estado_alumno) {
		this.estado_alumno = estado_alumno;
	}
	public Grado getGrado() {
		return grado;
	}
	public void setGrado(Grado grado) {
		this.grado = grado;
	}
}
