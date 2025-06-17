package com.PRISMA.PaqueteEscolar.AlumnoPaquete;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.Entity.AlumnoPaquete;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/AluPaq/")
@CrossOrigin(origins ="http://localhost:4200/")
public class AlumnoPaqueteControlador {

    @Autowired
    private AlumnoPaqueteRepositorio repositorio;

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

    @PostMapping("/alumnos-paquetes")
    public AlumnoPaquete guardarAsignacion(@RequestBody AlumnoPaquete alumnoPaquete) {
        
        return repositorio.save(alumnoPaquete);
    }
    
    @PutMapping("/alumnos-paquetes/{id}")
    public ResponseEntity<AlumnoPaquete> actualizarAsignacion(@PathVariable int id, @RequestBody AlumnoPaquete alumnoPaquete) {
        AlumnoPaquete asignacion = repositorio.findById(id).orElseThrow(()-> new RuntimeException("No se encontro"));
        
        asignacion.setPaquete_entregado(alumnoPaquete.isPaquete_entregado());
        asignacion.setFecha_entrega_p(alumnoPaquete.getFecha_entrega_p());
        asignacion.setAlumno(alumnoPaquete.getAlumno());
        asignacion.setPaqueteEscolar(alumnoPaquete.getPaqueteEscolar());
        AlumnoPaquete asignacionActualizada = repositorio.save(asignacion);
        return ResponseEntity.ok(asignacionActualizada);
    }
}
