package com.PRISMA.Alumno;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.PRISMA.Entity.Alumno;


@RestController
@RequestMapping("/Alu/")
@CrossOrigin(origins ="http://localhost:4200/")
public class AlumnoControlador {

    @Autowired
    private AlumnoRepositorio repositorio;

    @GetMapping("/alumnos")
    public List<Alumno> listarTodosLosAlumnos() {
        return repositorio.findAll();
    }

    @GetMapping("/alumnos/{id_grado}")
    public List<Alumno> listarAlumnoPorGrado(@PathVariable int id_grado) {
        return repositorio.buscarPorGrado(id_grado);
    }

    @PostMapping("/alumnos")
    public Alumno guardarAlumno(@RequestBody Alumno alumno) {
        
        return repositorio.save(alumno);
    }
    
}
