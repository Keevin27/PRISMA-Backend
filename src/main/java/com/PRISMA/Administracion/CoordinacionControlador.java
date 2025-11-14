package com.PRISMA.Administracion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coordinacion")
@CrossOrigin(origins = "http://localhost:4200")
public class CoordinacionControlador {

    @Autowired
    private CoordinacionServicio coordinacionServicio;

    @GetMapping("/asignaciones")
    public ResponseEntity<List<Map<String, Object>>> listarAsignaciones(
            @RequestParam(required = false) Integer anioAcademico) {
        try {
            List<Map<String, Object>> asignaciones = coordinacionServicio.listarAsignaciones(anioAcademico);
            return ResponseEntity.ok(asignaciones);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/docentes-disponibles")
    public ResponseEntity<List<Map<String, Object>>> obtenerDocentesDisponibles() {
        try {
            List<Map<String, Object>> docentes = coordinacionServicio.obtenerDocentesDisponibles();
            return ResponseEntity.ok(docentes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/grados-disponibles")
    public ResponseEntity<List<Map<String, Object>>> obtenerGradosDisponibles(
            @RequestParam(required = false) Integer anioAcademico) {
        try {
            List<Map<String, Object>> grados = coordinacionServicio.obtenerGradosDisponibles(anioAcademico);
            return ResponseEntity.ok(grados);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
  @PostMapping("/asignar")
    public ResponseEntity<Map<String, Object>> asignarCoordinador(@RequestBody Map<String, Object> datos) {
        Map<String, Object> respuesta = new HashMap<>();
        
        try {
            String duiDocente = (String) datos.get("duiDocente");
            Integer idGrado = Integer.valueOf(datos.get("idGrado").toString());
            
            Map<String, Object> coordinacion = coordinacionServicio.asignarCoordinador(duiDocente, idGrado);
            
            respuesta.put("mensaje", "Coordinador asignado exitosamente");
            respuesta.putAll(coordinacion);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
            
        } catch (RuntimeException e) {
            respuesta.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
        } catch (Exception e) {
            e.printStackTrace();
            respuesta.put("error", "Error al asignar coordinador: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Map<String, Object>> actualizarCoordinador(
            @PathVariable Long id,
            @RequestBody Map<String, Object> datos) {
        
        Map<String, Object> respuesta = new HashMap<>();
        
        try {
            String duiDocente = (String) datos.get("duiDocente");
            Map<String, Object> coordinacion = coordinacionServicio.actualizarCoordinador(id, duiDocente);
            
            respuesta.put("mensaje", "Coordinador actualizado exitosamente");
            respuesta.putAll(coordinacion);
            
            return ResponseEntity.ok(respuesta);
            
        } catch (RuntimeException e) {
            respuesta.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
        } catch (Exception e) {
            e.printStackTrace();
            respuesta.put("error", "Error al actualizar coordinador: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Map<String, Object>> eliminarCoordinador(@PathVariable Long id) {
        Map<String, Object> respuesta = new HashMap<>();
        
        try {
            coordinacionServicio.eliminarCoordinador(id);
            
            respuesta.put("mensaje", "Asignación eliminada exitosamente");
            respuesta.put("idCoordinacion", id);
            
            return ResponseEntity.ok(respuesta);
            
        } catch (RuntimeException e) {
            respuesta.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        } catch (Exception e) {
            e.printStackTrace();
            respuesta.put("error", "Error al eliminar asignación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<Map<String, Object>> obtenerDetalleCoordinacion(@PathVariable Long id) {
        try {
            Map<String, Object> detalle = coordinacionServicio.obtenerDetalle(id);
            return ResponseEntity.ok(detalle);
            
        } catch (RuntimeException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error al obtener detalle: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/verificar/{idGrado}")
    public ResponseEntity<Map<String, Object>> verificarCoordinador(@PathVariable Integer idGrado) {
        try {
            boolean tieneCoordinador = coordinacionServicio.tieneCoordinador(idGrado);
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("idGrado", idGrado);
            respuesta.put("tieneCoordinador", tieneCoordinador);
            
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error al verificar: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/docente/{duiDocente}")
    public ResponseEntity<List<Map<String, Object>>> obtenerCoordinacionesPorDocente(
            @PathVariable String duiDocente) {
        try {
            List<Map<String, Object>> coordinaciones = coordinacionServicio.obtenerCoordinacionesPorDocente(duiDocente);
            return ResponseEntity.ok(coordinaciones);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}