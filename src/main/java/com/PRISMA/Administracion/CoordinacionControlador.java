package com.PRISMA.Administracion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.PRISMA.Alumno.Grado.GradoRepositorio;
import com.PRISMA.Docente.DocenteRepositorio;
import com.PRISMA.Entity.Coordinacion;
import com.PRISMA.Entity.Docente;
import com.PRISMA.Entity.Grado;

@RestController
@RequestMapping("/coordinacion")
@CrossOrigin(origins = "http://localhost:4200")
public class CoordinacionControlador {

    @Autowired
    private CoordinacionRepositorio coordinacionRepositorio;

    @Autowired
    private DocenteRepositorio docenteRepositorio;

    @Autowired
    private GradoRepositorio gradoRepositorio;

    // ========================================================
    // 1. LISTAR TODAS LAS ASIGNACIONES DE COORDINADORES
    // GET /coordinacion/asignaciones
    // ========================================================
    @GetMapping("/asignaciones")
    public ResponseEntity<List<Map<String, Object>>> listarAsignaciones(
            @RequestParam(required = false) Integer anioAcademico) {
        
        try {
            List<Coordinacion> coordinaciones;
            
            // Filtrar por año académico si se proporciona
            if (anioAcademico != null) {
                coordinaciones = coordinacionRepositorio.findByAnioAcademico(anioAcademico);
            } else {
                coordinaciones = coordinacionRepositorio.findAll();
            }
            
            // Formatear la respuesta
            List<Map<String, Object>> resultado = coordinaciones.stream().map(c -> {
                Map<String, Object> datos = new HashMap<>();
                datos.put("idCoordinacion", c.getIdCoordinacion());
                
                // Datos del docente
                if (c.getDocente() != null) {
                    datos.put("duiDocente", c.getDocente().getDuiDocente());
                    datos.put("nombreDocente", c.getDocente().getNombre_Docente() + " " + c.getDocente().getApellido_Docente());
                }
                
                // Datos del grado
                if (c.getGrado() != null) {
                    datos.put("idGrado", c.getGrado().getId_grado());
                    datos.put("nombreGrado", c.getGrado().getNombre_grado());
                    datos.put("seccion", c.getGrado().getSeccion());
                    datos.put("turno", c.getGrado().getTurno_grado());
                    datos.put("gradoCompleto", c.getGrado().getNombre_grado() + " - Sección " + c.getGrado().getSeccion());
                    
                    // Año académico
                    if (c.getGrado().getAnioAcademico() != null) {
                        datos.put("anioAcademico", c.getGrado().getAnioAcademico().getAnio());
                    }
                }
                
                return datos;
            }).collect(Collectors.toList());
            
            return ResponseEntity.ok(resultado);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // ========================================================
    // 2. OBTENER DOCENTES DISPONIBLES (sin asignación o activos)
    // GET /coordinacion/docentes-disponibles
    // ========================================================
    @GetMapping("/docentes-disponibles")
    public ResponseEntity<List<Map<String, Object>>> obtenerDocentesDisponibles() {
        try {
            // Obtener todos los docentes activos
            List<Docente> docentes = docenteRepositorio.findAll().stream()
                    .filter(d -> d.isDocente_Activo() != null && d.isDocente_Activo())
                    .collect(Collectors.toList());
            
            List<Map<String, Object>> resultado = docentes.stream().map(d -> {
                Map<String, Object> datos = new HashMap<>();
                datos.put("duiDocente", d.getDuiDocente());
                datos.put("nombreCompleto", d.getNombre_Docente() + " " + d.getApellido_Docente());
                datos.put("correo", d.getCorreo_Docente());
                return datos;
            }).collect(Collectors.toList());
            
            return ResponseEntity.ok(resultado);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // ========================================================
    // 3. OBTENER GRADOS DISPONIBLES (sin coordinador asignado)
    // GET /coordinacion/grados-disponibles
    // ========================================================
    @GetMapping("/grados-disponibles")
    public ResponseEntity<List<Map<String, Object>>> obtenerGradosDisponibles(
            @RequestParam(required = false) Integer anioAcademico) {
        
        try {
            List<Grado> todosGrados;
            
            // Filtrar por año académico si se proporciona
            if (anioAcademico != null) {
                todosGrados = gradoRepositorio.findByAnioAcademico_anio(anioAcademico);
            } else {
                todosGrados = gradoRepositorio.findAll();
            }
            
            // Filtrar grados que NO tienen coordinador asignado
            List<Map<String, Object>> gradosDisponibles = todosGrados.stream()
                    .filter(g -> !coordinacionRepositorio.existsByGradoId(g.getId_grado()))
                    .filter(g -> g.isEstadoGrado()) // Solo grados activos
                    .map(g -> {
                        Map<String, Object> datos = new HashMap<>();
                        datos.put("idGrado", g.getId_grado());
                        datos.put("nombreGrado", g.getNombre_grado());
                        datos.put("seccion", g.getSeccion());
                        datos.put("turno", g.getTurno_grado());
                        datos.put("gradoCompleto", g.getNombre_grado() + " - Sección " + g.getSeccion());
                        
                        if (g.getAnioAcademico() != null) {
                            datos.put("anioAcademico", g.getAnioAcademico().getAnio());
                        }
                        
                        return datos;
                    }).collect(Collectors.toList());
            
            return ResponseEntity.ok(gradosDisponibles);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // ========================================================
    // 4. CREAR ASIGNACIÓN DE COORDINADOR
    // POST /coordinacion/asignar
    // Body: { "duiDocente": "12345678-9", "idGrado": 1 }
    // ========================================================
    @PostMapping("/asignar")
    public ResponseEntity<Map<String, Object>> asignarCoordinador(@RequestBody Map<String, Object> datos) {
        
        Map<String, Object> respuesta = new HashMap<>();
        
        try {
            String duiDocente = (String) datos.get("duiDocente");
            Integer idGrado = Integer.valueOf(datos.get("idGrado").toString());
            
            // Validar que el docente existe
            Optional<Docente> docenteOpt = docenteRepositorio.findById(duiDocente);
            if (!docenteOpt.isPresent()) {
                respuesta.put("error", "Docente no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
            }
            
            // Validar que el grado existe
            Optional<Grado> gradoOpt = gradoRepositorio.findById(idGrado);
            if (!gradoOpt.isPresent()) {
                respuesta.put("error", "Grado no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
            }
            
            // Validar que el grado NO tenga ya un coordinador asignado
            if (coordinacionRepositorio.existsByGradoId(idGrado)) {
                respuesta.put("error", "Este grado ya tiene un coordinador asignado");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
            }
            
            // Crear la nueva asignación
            Coordinacion nuevaCoordinacion = new Coordinacion();
            nuevaCoordinacion.setDocente(docenteOpt.get());
            nuevaCoordinacion.setGrado(gradoOpt.get());
            
            Coordinacion guardada = coordinacionRepositorio.save(nuevaCoordinacion);
            
            // Formatear respuesta
            respuesta.put("mensaje", "Coordinador asignado exitosamente");
            respuesta.put("idCoordinacion", guardada.getIdCoordinacion());
            respuesta.put("nombreDocente", guardada.getDocente().getNombre_Docente() + " " + guardada.getDocente().getApellido_Docente());
            respuesta.put("grado", guardada.getGrado().getNombre_grado() + " - Sección " + guardada.getGrado().getSeccion());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
            
        } catch (Exception e) {
            e.printStackTrace();
            respuesta.put("error", "Error al asignar coordinador: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    // ========================================================
    // 5. ACTUALIZAR ASIGNACIÓN DE COORDINADOR
    // PUT /coordinacion/actualizar/{id}
    // Body: { "duiDocente": "12345678-9" }
    // ========================================================
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Map<String, Object>> actualizarCoordinador(
            @PathVariable Long id,
            @RequestBody Map<String, Object> datos) {
        
        Map<String, Object> respuesta = new HashMap<>();
        
        try {
            // Buscar la coordinación existente
            Optional<Coordinacion> coordinacionOpt = coordinacionRepositorio.findById(id);
            if (!coordinacionOpt.isPresent()) {
                respuesta.put("error", "Asignación no encontrada");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
            }
            
            String nuevoDocente = (String) datos.get("duiDocente");
            
            // Validar que el nuevo docente existe
            Optional<Docente> docenteOpt = docenteRepositorio.findById(nuevoDocente);
            if (!docenteOpt.isPresent()) {
                respuesta.put("error", "Docente no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
            }
            
            // Actualizar el coordinador
            Coordinacion coordinacion = coordinacionOpt.get();
            coordinacion.setDocente(docenteOpt.get());
            
            Coordinacion actualizada = coordinacionRepositorio.save(coordinacion);
            
            respuesta.put("mensaje", "Coordinador actualizado exitosamente");
            respuesta.put("idCoordinacion", actualizada.getIdCoordinacion());
            respuesta.put("nombreDocente", actualizada.getDocente().getNombre_Docente() + " " + actualizada.getDocente().getApellido_Docente());
            respuesta.put("grado", actualizada.getGrado().getNombre_grado() + " - Sección " + actualizada.getGrado().getSeccion());
            
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            e.printStackTrace();
            respuesta.put("error", "Error al actualizar coordinador: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    // ========================================================
    // 6. ELIMINAR ASIGNACIÓN DE COORDINADOR
    // DELETE /coordinacion/eliminar/{id}
    // ========================================================
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Map<String, Object>> eliminarCoordinador(@PathVariable Long id) {
        
        Map<String, Object> respuesta = new HashMap<>();
        
        try {
            if (!coordinacionRepositorio.existsById(id)) {
                respuesta.put("error", "Asignación no encontrada");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
            }
            
            coordinacionRepositorio.deleteById(id);
            
            respuesta.put("mensaje", "Asignación eliminada exitosamente");
            respuesta.put("idCoordinacion", id);
            
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            e.printStackTrace();
            respuesta.put("error", "Error al eliminar asignación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    // ========================================================
    // 7. OBTENER DETALLES DE UNA ASIGNACIÓN ESPECÍFICA
    // GET /coordinacion/detalle/{id}
    // ========================================================
    @GetMapping("/detalle/{id}")
    public ResponseEntity<Map<String, Object>> obtenerDetalleCoordinacion(@PathVariable Long id) {
        
        try {
            Optional<Coordinacion> coordinacionOpt = coordinacionRepositorio.findById(id);
            
            if (!coordinacionOpt.isPresent()) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Asignación no encontrada");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            
            Coordinacion c = coordinacionOpt.get();
            Map<String, Object> datos = new HashMap<>();
            
            datos.put("idCoordinacion", c.getIdCoordinacion());
            
            if (c.getDocente() != null) {
                datos.put("duiDocente", c.getDocente().getDuiDocente());
                datos.put("nombreDocente", c.getDocente().getNombre_Docente() + " " + c.getDocente().getApellido_Docente());
                datos.put("correoDocente", c.getDocente().getCorreo_Docente());
            }
            
            if (c.getGrado() != null) {
                datos.put("idGrado", c.getGrado().getId_grado());
                datos.put("nombreGrado", c.getGrado().getNombre_grado());
                datos.put("seccion", c.getGrado().getSeccion());
                datos.put("turno", c.getGrado().getTurno_grado());
            }
            
            return ResponseEntity.ok(datos);
            
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error al obtener detalle: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}