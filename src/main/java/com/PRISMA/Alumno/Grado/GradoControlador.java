package com.PRISMA.Alumno.Grado;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.Entity.Grado;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/Grado/")
@CrossOrigin(origins="http://localhost:4200/")
public class GradoControlador {
    @Autowired
    private GradoRepositorio repositorio;

    @GetMapping("/grados")
    public List<Grado> obtnerGrados() {
        return repositorio.findAll();
    }

    @GetMapping("/grados/{anio}")
    public List<Grado> obtenerGradosPorAnio(@PathVariable int anio) {
        return repositorio.findByAnioAcademico_anio(anio);
    }

    @GetMapping("/grados/activos")
    public ResponseEntity<List<Grado>> obtenerGradosDelAnioActivo() {
        List<Grado> gradosActivos = repositorio.findGradosPorAnioActivo();
        return ResponseEntity.ok(gradosActivos);
    }
    
    
}
