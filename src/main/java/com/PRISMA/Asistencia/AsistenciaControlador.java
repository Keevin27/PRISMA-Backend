package com.PRISMA.Asistencia;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.Entity.AsistenciaAlumno;
import com.PRISMA.Entity.PaqueteEscolar;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


 
@RestController
@RequestMapping("/AsisAlum/")
@CrossOrigin(origins ="http://localhost:4200/")
public class AsistenciaControlador {

    @Autowired
    private AsistenciaServicio asistenciaServicio;
    private AsistenciaRepositorio repositorio;
    
    //:8080/AsisAlum/asistencia-alumno
    @GetMapping("/asistencia-alumno")
    public List<AsistenciaAlumno> obtenerAsistenciaAlumnos(){
        List<AsistenciaAlumno> asistenciaAlumnos = this.asistenciaServicio.listarAsistencia();

        //  asistenciaAlumnos.forEach(asistenciaAlumno -> logger.info());
        return asistenciaAlumnos;
    }

    @PostMapping("/guardar-asistencia")
    public AsistenciaAlumno guardarAsistencia(@RequestBody AsistenciaAlumno asistenciaAlumno) {
        
       // return asistenciaAlumno = this.asistenciaRepositorio.save(asistenciaAlumno);
       return repositorio.save(asistenciaAlumno);
    }

}
