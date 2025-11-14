package com.PRISMA.AnioAcademico;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.Entity.AnioAcademico;

@RestController
@RequestMapping("/AnioAcademico/")
@CrossOrigin(origins = "http://localhost:4200/")
public class AnioAcademicoControlador {

    @Autowired
    private AnioAcademicoRepositorio repositorioAnio;

    //Listar anios academicos registrados
    @GetMapping("/")
    public List<AnioAcademico> listarAniosAcademicos() {
        return repositorioAnio.findAll();
    }

    //Crear nuevo año académico
    @PostMapping("/crear")
    public ResponseEntity<?> crearAnioAcademico(@RequestBody Map<String, Integer> request) {
        try {
            int anio = request.get("anio");
            
            // Verificar si ya existe el año
            if (repositorioAnio.existsByAnio(anio)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El año " + anio + " ya existe");
            }

            AnioAcademico nuevoAnio = new AnioAcademico();
            nuevoAnio.setAnio(anio);
            nuevoAnio.setAnio_activo(true);
            nuevoAnio.setAnio_cerrado(false);
            
            AnioAcademico anioGuardado = repositorioAnio.save(nuevoAnio);
            return ResponseEntity.status(HttpStatus.CREATED).body(anioGuardado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al crear el año académico: " + e.getMessage());
        }
    }

    //Actualizar estado del año (activo/inactivo)
    @PutMapping("/actualizar-estado/{id}")
    public ResponseEntity<?> actualizarEstado(@PathVariable int id, @RequestBody Map<String, Boolean> request) {
        try {
            AnioAcademico anio = repositorioAnio.findById(id)
                .orElseThrow(() -> new RuntimeException("Año académico no encontrado"));
            
            if (request.containsKey("anio_activo")) {
                anio.setAnio_activo(request.get("anio_activo"));
            }
            if (request.containsKey("anio_cerrado")) {
                anio.setAnio_cerrado(request.get("anio_cerrado"));
            }
            
            AnioAcademico anioActualizado = repositorioAnio.save(anio);
            return ResponseEntity.ok(anioActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar el año académico: " + e.getMessage());
        }
    }

    //Obtener año académico por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable int id) {
        try {
            AnioAcademico anio = repositorioAnio.findById(id)
                .orElseThrow(() -> new RuntimeException("Año académico no encontrado"));
            return ResponseEntity.ok(anio);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Año académico no encontrado");
        }
    }

//Obtener año activo
    @GetMapping("/activo")
    public ResponseEntity<?> obtenerAnioActivo() {
        try {
            Optional<AnioAcademico> anioActivo = repositorioAnio.findByAnioActivoTrue();
            if (anioActivo.isPresent()) {
                return ResponseEntity.ok(anioActivo.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No hay año académico activo");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al obtener año activo: " + e.getMessage());
        }
    }

    @GetMapping("/todos")
    public ResponseEntity<List<AnioAcademico>> obtenerTodos() {
        try {
            List<AnioAcademico> anios = repositorioAnio.findAll();
            return ResponseEntity.ok(anios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}