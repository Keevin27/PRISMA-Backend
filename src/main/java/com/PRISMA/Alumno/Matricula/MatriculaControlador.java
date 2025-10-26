package com.PRISMA.Alumno.Matricula;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.Entity.Alumno;
import com.PRISMA.Entity.Matricula;


@RestController
@RequestMapping("/Matricula/")
@CrossOrigin(origins = "http://localhost:4200/")
public class MatriculaControlador {

    @Autowired
    private MatriculaRepositorio repositorioMatricula;

    //Listar todas las matriculas
    @GetMapping("/")
    public List<Matricula> listarMatriculas() {
        return repositorioMatricula.findAll();
    }

    //Matriculas por anio
    @GetMapping("/{anio}")
    public List<Matricula> listarMatriculasPorAnio(@PathVariable int anio) {
        return repositorioMatricula.buscarPorAnio(anio);
    }

    //Matriculas por grado
    @GetMapping("/{anio}/{id_grado}")
    public List<Matricula> listarMatriculasPorAnioGrado(@PathVariable int id_grado) {
        return repositorioMatricula.buscarPorGrado(id_grado);
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
    public ResponseEntity<Matricula> actualizarMatricula(@PathVariable int id, @RequestBody Matricula matriculaActualizada) {
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
