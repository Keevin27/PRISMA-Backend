package com.PRISMA.Asistencia;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.PRISMA.Alumno.AlumnoRepositorio;
import com.PRISMA.Entity.Alumno;
import com.PRISMA.Entity.AsistenciaAlumno;

@RestController
@RequestMapping("/AsisAlum/")
@CrossOrigin(origins = "http://localhost:4200/")
public class AsistenciaControlador {

    @Autowired
    private AsistenciaRepositorio repositorio;

    @Autowired
    private AlumnoRepositorio alumnoRepository;

    /**
     * Obtener todas las asistencias
     */
    @GetMapping("/asistencia-alumno")
    public List<AsistenciaAlumno> obtenerAsistenciaAlumnos() {
        return repositorio.findAll();
    }

    /**
     * Obtener asistencias por grado y rango de fechas
     * Ejemplo de URL:
     * http://localhost:8080/AsisAlum/asistencia-alumno-filtro?idGrado=1&inicio=2025-06-01&fin=2025-06-30
     */
    @GetMapping("/asistencia-alumno-filtro")
    public List<AsistenciaAlumno> obtenerAsistenciaPorGradoYFechas(
            @RequestParam int idGrado,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return repositorio.findByGradoAndAnioAsistenciaAlumnos(idGrado, inicio, fin);
    }

    /**
     * Guardar nueva asistencia de un alumno
     */
    @PostMapping("/asistencia-alumno")
    public AsistenciaAlumno guardarAsistenciaAlumno(@RequestBody AsistenciaAlumno asistenciaAlumno) {
        Alumno alumnoExistente = alumnoRepository.findById(asistenciaAlumno.getAlumno().getIdAlumno())
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

        asistenciaAlumno.setAlumno(alumnoExistente);
        return repositorio.save(asistenciaAlumno);
    }

    // Devuelve lista de un grado y fecha especifico    
    @GetMapping("/asistencia-alumno-filtro-dia")
    public List<AsistenciaAlumno> obtenerAsistenciaPorGradoYFecha(
            @RequestParam int idGrado,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        return repositorio.findByGradoAndFechaAsistenciaAlumnos(idGrado, fecha);
    }

    @PutMapping("/asistencia-alumno/{id}")
    public ResponseEntity<AsistenciaAlumno> actualizarAsistencia(@PathVariable int id,
            @RequestBody AsistenciaAlumno asistencia) {
        AsistenciaAlumno asis = repositorio.findById(id).orElseThrow(() -> new RuntimeException("No se encontro"));

        asis.setEstado_asistencia(asistencia.getEstado_asistencia());
        
        AsistenciaAlumno asistenciaActualizada = repositorio.save(asis);
        return ResponseEntity.ok(asistenciaActualizada);
    }
}
