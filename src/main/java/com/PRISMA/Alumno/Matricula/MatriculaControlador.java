package com.PRISMA.Alumno.Matricula;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.Alumno.AlumnoRepositorio;
import com.PRISMA.Alumno.Grado.GradoRepositorio;
import com.PRISMA.Entity.Alumno;
import com.PRISMA.Entity.Grado;
import com.PRISMA.Entity.Matricula;

@RestController
@RequestMapping("/Matricula/")
@CrossOrigin(origins = "http://localhost:4200/")
public class MatriculaControlador {

    @Autowired
    private MatriculaRepositorio repositorioMatricula;

    @Autowired
    private AlumnoRepositorio alumnoRepositorio;

    @Autowired
    private GradoRepositorio gradoRepositorio;

    // Listar todas las matriculas
    @GetMapping("/")
    public List<Matricula> listarMatriculas() {
        return repositorioMatricula.findAll();
    }

    // Matriculas por anio
    @GetMapping("/{anio}")
    public List<Matricula> listarMatriculasPorAnio(@PathVariable int anio) {
        return repositorioMatricula.buscarPorAnio(anio);
    }

    // Matriculas por grado
    @GetMapping("/{anio}/{id_grado}")
    public List<Matricula> listarMatriculasPorAnioGrado(@PathVariable int id_grado) {
        return repositorioMatricula.buscarPorGrado(id_grado);
    }

    // Matriculas por grado (nueva ruta más clara)
    @GetMapping("/grado/{idGrado}")
    public List<Matricula> listarMatriculasPorGrado(@PathVariable int idGrado) {
        return repositorioMatricula.buscarPorGrado(idGrado);
    }

    // Buscar matricula por id de alumno
    @GetMapping("/buscarAlumno/{idAlumno}")
    public ResponseEntity<Matricula> obtenerAlumnoPorId(@PathVariable int idAlumno) {
        Optional<Matricula> matricula = repositorioMatricula.buscarMatriculaPorIdAlumno(idAlumno);
        if (matricula.isPresent()) {
            return ResponseEntity.ok(matricula.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear matrícula
    @PostMapping("/crear")
    public ResponseEntity<?> crearMatricula(@RequestBody Map<String, Object> payload) {
        Map<String, String> respuesta = new HashMap<>();

        try {
            // Extraer datos del payload
            Integer idAlumno = (Integer) payload.get("idAlumno");
            Integer idGrado = (Integer) payload.get("idGrado");

            if (idAlumno == null || idGrado == null) {
                respuesta.put("error", "Debe proporcionar el ID del alumno y del grado");
                return ResponseEntity.badRequest().body(respuesta);
            }

            // Verificar que el alumno existe
            Optional<Alumno> alumnoOpt = alumnoRepositorio.findById(idAlumno);
            if (!alumnoOpt.isPresent()) {
                respuesta.put("error", "El alumno no existe");
                return ResponseEntity.badRequest().body(respuesta);
            }

            // Verificar que el grado existe
            Optional<Grado> gradoOpt = gradoRepositorio.findById(idGrado);
            if (!gradoOpt.isPresent()) {
                respuesta.put("error", "El grado no existe");
                return ResponseEntity.badRequest().body(respuesta);
            }

            Alumno alumno = alumnoOpt.get();
            Grado grado = gradoOpt.get();

            // Verificar si el alumno ya está matriculado en ese año académico
            Optional<Matricula> matriculaExistente = repositorioMatricula.buscarMatriculaPorIdAlumno(idAlumno);
            if (matriculaExistente.isPresent()) {
                Matricula matExist = matriculaExistente.get();
                if (matExist.getGrado().getAnioAcademico().getAnio() == grado.getAnioAcademico().getAnio()) {
                    respuesta.put("error", "El alumno ya está matriculado en este año académico");
                    return ResponseEntity.badRequest().body(respuesta);
                }
            }

            // Verificar cupo del grado (máximo 45 alumnos)
            Long cantidadAlumnos = repositorioMatricula.contarAlumnosPorGrado(idGrado);
            if (cantidadAlumnos >= 45) {
                respuesta.put("error", "El grado ha alcanzado su cupo máximo (45 alumnos)");
                return ResponseEntity.badRequest().body(respuesta);
            }

            // Crear la matrícula
            Matricula nuevaMatricula = new Matricula();
            nuevaMatricula.setAlumno(alumno);
            nuevaMatricula.setGrado(grado);
            nuevaMatricula.setEstadoMatricula("Matriculado");

            Matricula matriculaGuardada = repositorioMatricula.save(nuevaMatricula);

            respuesta.put("mensaje", "Matrícula creada exitosamente");
            return ResponseEntity.ok(matriculaGuardada);

        } catch (Exception e) {
            respuesta.put("error", "Error al crear la matrícula: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    // Verificar si alumno ya está matriculado en un año
    @GetMapping("/verificar/{idAlumno}/{anio}")
    public ResponseEntity<Boolean> verificarMatriculaExistente(
            @PathVariable Integer idAlumno,
            @PathVariable Integer anio) {

        Optional<Matricula> matricula = repositorioMatricula.buscarMatriculaPorIdAlumno(idAlumno);

        if (matricula.isPresent()) {
            int anioMatricula = matricula.get().getGrado().getAnioAcademico().getAnio();
            return ResponseEntity.ok(anioMatricula == anio);
        }

        return ResponseEntity.ok(false);
    }

    // Contar alumnos en un grado
    @GetMapping("/contar/{idGrado}")
    public ResponseEntity<Long> contarAlumnosPorGrado(@PathVariable Integer idGrado) {
        Long cantidad = repositorioMatricula.contarAlumnosPorGrado(idGrado);
        return ResponseEntity.ok(cantidad);
    }

    // Eliminar matricula
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarMatricula(@PathVariable int id) {
        Optional<Matricula> matricula = repositorioMatricula.findById(id);

        if (matricula.isPresent()) {
            try {
                repositorioMatricula.deleteById(id); // Eliminar físicamente
                return ResponseEntity.ok("matricula eliminadas exitosamente");
            } catch (Exception e) {
                return ResponseEntity.badRequest()
                        .body("Error al eliminar las matriculas: " + e.getMessage());
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar matricula existente
    @PutMapping("/{id}")
    public ResponseEntity<Matricula> actualizarMatricula(@PathVariable int id,
            @RequestBody Matricula matriculaActualizada) {
        Optional<Matricula> matriculaExistente = repositorioMatricula.findById(id);

        if (matriculaExistente.isPresent()) {
            Matricula matricula = matriculaExistente.get();

            // Actualizar campos
            matricula.setEstadoMatricula("Matriculado");
            matricula.setGrado(matriculaActualizada.getGrado());

            Matricula matriculaGuardada = repositorioMatricula.save(matricula);
            return ResponseEntity.ok(matriculaGuardada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}