package com.PRISMA.PaqueteEscolar.AlumnoPaquete;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.Alumno.AlumnoRepositorio;
import com.PRISMA.Entity.Alumno;
import com.PRISMA.Entity.AlumnoPaquete;
import com.PRISMA.Entity.PaqueteEscolar;
import com.PRISMA.PaqueteEscolar.PaqueteEscolarRepositorio;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/AluPaq/")
public class AlumnoPaqueteControlador {

    @Autowired
    private AlumnoPaqueteRepositorio repositorio;
    @Autowired
    private AlumnoRepositorio alumnoRepository;
    @Autowired
    private PaqueteEscolarRepositorio paqueteRepository;

    @GetMapping("/alumnos-paquetes")
    public List<AlumnoPaquete> obtenerAsignacionAlumnosPaquetes() {
        return repositorio.findAll();
    }

    @GetMapping("/alumnos-paquetes-filtro")
    public List<AlumnoPaquete> obtenerAsignacionAlumnosPaquetesThisYear() {
        LocalDate inicio = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDate fin = LocalDate.of(LocalDate.now().getYear(), 12, 31);
        return repositorio.findByFechaEntre(inicio, fin);
    }
    //OBTIENE LOS Alumno-Paquete DEL ANYO ACTUAL Y DE UN GRADO 
    @GetMapping("/alumnos-paquetes-filtro-grado/{idgrado}")
    public List<AlumnoPaquete> obtenerAsignacionesAnyoGrado(@PathVariable int idgrado) {

        LocalDate inicio = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDate fin = LocalDate.of(LocalDate.now().getYear(), 12, 31);
        return repositorio.findByGradoAndAnioEntrega(idgrado, inicio, fin);
    }

    @PostMapping("/alumnos-paquetes")
    public AlumnoPaquete guardarAsignacion(@RequestBody AlumnoPaquete alumnoPaquete) {
        Alumno alumnoExistente = alumnoRepository.findById(alumnoPaquete.getAlumno().getIdAlumno())
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
        PaqueteEscolar paqueteExistente = paqueteRepository
                .findById(alumnoPaquete.getPaqueteEscolar().getId_paquete_e())
                .orElseThrow(() -> new RuntimeException("Paquete no encontrado"));

        alumnoPaquete.setAlumno(alumnoExistente);
        alumnoPaquete.setPaqueteEscolar(paqueteExistente);
        return repositorio.save(alumnoPaquete);
    }

    @PutMapping("/alumnos-paquetes/{id}")
    public ResponseEntity<AlumnoPaquete> actualizarAsignacion(@PathVariable int id,
            @RequestBody AlumnoPaquete alumnoPaquete) {
        AlumnoPaquete asignacion = repositorio.findById(id).orElseThrow(() -> new RuntimeException("No se encontro"));

        asignacion.setPaquete_entregado(alumnoPaquete.isPaquete_entregado());
        asignacion.setAlumno(alumnoPaquete.getAlumno());
        asignacion.setPaqueteEscolar(alumnoPaquete.getPaqueteEscolar());
        AlumnoPaquete asignacionActualizada = repositorio.save(asignacion);
        return ResponseEntity.ok(asignacionActualizada);
    }
}
