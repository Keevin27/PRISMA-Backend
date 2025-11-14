package com.PRISMA.Administración;

import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.Alumno.AlumnoRepositorio;
import com.PRISMA.Entity.Alumno;
import com.PRISMA.Entity.Sancion;

@RestController
@RequestMapping("/Sancion/")
@CrossOrigin(origins = "http://localhost:4200/")
public class SancionControlador {

    @Autowired
    private SancionRepositorio sancionRepositorio;

    @Autowired
    private AlumnoRepositorio alumnoRepositorio;

    // Crea una nueva sanción para un alumno
    @PostMapping("/crear")
    public ResponseEntity<?> crearSancion(@RequestBody Map<String, Object> payload) {
        try {
            Integer idAlumno = (Integer) payload.get("idAlumno");
            String tipoSancion = (String) payload.get("tipoSancion");
            String descripcionSancion = (String) payload.get("descripcionSancion");
            LocalDate fechaSancion = LocalDate.parse((String) payload.get("fechaSancion"));

            if (idAlumno == null || tipoSancion == null || descripcionSancion == null || fechaSancion == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Faltan datos"));
            }

            Optional<Alumno> alumnoOpt = alumnoRepositorio.findById(idAlumno);
            if (!alumnoOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "El alumno no existe"));
            }

            Sancion nuevaSancion = new Sancion();
            nuevaSancion.setAlumno(alumnoOpt.get());
            nuevaSancion.setTipoSancion(tipoSancion);
            nuevaSancion.setDescripcionSancion(descripcionSancion);
            nuevaSancion.setFechaSancion(fechaSancion);

            Sancion sancionGuardada = sancionRepositorio.save(nuevaSancion);
            
            System.out.println("Sancion creada exitosamente: ID=" + sancionGuardada.getIdSancion());
            
            // Devolver solo los datos necesarios (sin el objeto Alumno completo)
            Map<String, Object> response = new HashMap<>();
            response.put("idSancion", sancionGuardada.getIdSancion());
            response.put("tipoSancion", sancionGuardada.getTipoSancion());
            response.put("descripcionSancion", sancionGuardada.getDescripcionSancion());
            response.put("fechaSancion", sancionGuardada.getFechaSancion());
            response.put("idAlumno", sancionGuardada.getAlumno().getIdAlumno());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            System.err.println("Error al crear sancion: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // NUEVO: Actualiza una sanción existente
    @PutMapping("/actualizar/{idSancion}")
    public ResponseEntity<?> actualizarSancion(
            @PathVariable Integer idSancion,
            @RequestBody Map<String, Object> payload) {
        try {
            System.out.println("=== ACTUALIZANDO SANCIÓN ===");
            System.out.println("ID Sanción: " + idSancion);
            System.out.println("Payload recibido: " + payload);
            
            Optional<Sancion> sancionOpt = sancionRepositorio.findById(idSancion);
            if (!sancionOpt.isPresent()) {
                System.err.println("Sanción no encontrada: ID=" + idSancion);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "La sanción no existe"));
            }

            String tipoSancion = (String) payload.get("tipoSancion");
            String descripcionSancion = (String) payload.get("descripcionSancion");
            String fechaStr = (String) payload.get("fechaSancion");
            
            System.out.println("Tipo: " + tipoSancion);
            System.out.println("Descripción: " + descripcionSancion);
            System.out.println("Fecha: " + fechaStr);

            if (tipoSancion == null || descripcionSancion == null || fechaStr == null) {
                System.err.println("Faltan datos en el payload");
                return ResponseEntity.badRequest().body(Map.of("error", "Faltan datos"));
            }

            LocalDate fechaSancion;
            try {
                fechaSancion = LocalDate.parse(fechaStr);
            } catch (Exception e) {
                System.err.println("Error al parsear fecha: " + fechaStr);
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Formato de fecha incorrecto: " + fechaStr));
            }

            Sancion sancion = sancionOpt.get();
            System.out.println("Sanción actual - Alumno ID: " + (sancion.getAlumno() != null ? sancion.getAlumno().getIdAlumno() : "null"));
            
            sancion.setTipoSancion(tipoSancion);
            sancion.setDescripcionSancion(descripcionSancion);
            sancion.setFechaSancion(fechaSancion);
            
            System.out.println("Sanción antes de guardar - Alumno: " + sancion.getAlumno());

            Sancion sancionActualizada = sancionRepositorio.saveAndFlush(sancion);
            
            System.out.println("✓ Sanción actualizada exitosamente: ID=" + sancionActualizada.getIdSancion());
            
            // Devolver solo los datos que necesita el frontend (sin el objeto Alumno completo)
            Map<String, Object> response = new HashMap<>();
            response.put("idSancion", sancionActualizada.getIdSancion());
            response.put("tipoSancion", sancionActualizada.getTipoSancion());
            response.put("descripcionSancion", sancionActualizada.getDescripcionSancion());
            response.put("fechaSancion", sancionActualizada.getFechaSancion());
            response.put("idAlumno", sancionActualizada.getAlumno().getIdAlumno());
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Error al actualizar sanción: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno: " + e.getMessage()));
        }
    }

    // Obtiene todas las sanciones de un alumno por su ID
    @GetMapping("/alumno/{idAlumno}")
    public ResponseEntity<?> obtenerSancionesPorAlumno(
            @PathVariable Integer idAlumno,
            @RequestParam(required = false) Integer anio) {
        
        try {
            System.out.println("Recibiendo peticion de sanciones para alumno: " + idAlumno);
            System.out.println("Año solicitado: " + (anio != null ? anio : "NO ESPECIFICADO"));
            
            // Verificar que el alumno existe
            Optional<Alumno> alumnoOpt = alumnoRepositorio.findById(idAlumno);
            if (!alumnoOpt.isPresent()) {
                System.err.println("Alumno no encontrado: ID=" + idAlumno);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Alumno no encontrado"));
            }
            
            List<Sancion> sanciones;
            
            if (anio != null) {
                // Buscar por alumno y año
                System.out.println("Buscando sanciones con filtro de año...");
                sanciones = sancionRepositorio.findByAlumnoAndAnio(idAlumno, anio);
            } else {
                // Buscar todas las sanciones del alumno
                System.out.println("Buscando TODAS las sanciones del alumno...");
                sanciones = sancionRepositorio.findByAlumno_IdAlumnoOrderByFechaSancionDesc(idAlumno);
            }
            
            System.out.println("Sanciones encontradas: " + sanciones.size());
            
            // Debug: mostrar cada sanción
            for (Sancion s : sanciones) {
                System.out.println("  Sancion ID=" + s.getIdSancion() + 
                                 ", Tipo=" + s.getTipoSancion() + 
                                 ", Fecha=" + s.getFechaSancion());
            }
            
            return ResponseEntity.ok(sanciones);
            
        } catch (Exception e) {
            System.err.println("ERROR al obtener sanciones:");
            System.err.println("   Mensaje: " + e.getMessage());
            System.err.println("   Clase: " + e.getClass().getName());
            e.printStackTrace();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al cargar sanciones: " + e.getMessage()));
        }
    }

    // Elimina una sanción por su ID
    @DeleteMapping("/{idSancion}")
    public ResponseEntity<?> eliminarSancion(@PathVariable Integer idSancion) {
        try {
            if (!sancionRepositorio.existsById(idSancion)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "La sanción no existe"));
            }

            sancionRepositorio.deleteById(idSancion);
            System.out.println("Sancion eliminada: ID=" + idSancion);
            
            return ResponseEntity.ok(Map.of("mensaje", "Sanción eliminada correctamente"));
            
        } catch (Exception e) {
            System.err.println("Error al eliminar sancion: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Descarga datos del reporte de una sanción grave
    @GetMapping("/descargar/{idSancion}")
    public ResponseEntity<?> descargarSancion(@PathVariable Integer idSancion) {
        try {
            Optional<Sancion> sancionOpt = sancionRepositorio.findById(idSancion);

            if (!sancionOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "La sanción no existe"));
            }

            Sancion sancion = sancionOpt.get();

            if (!"Grave".equalsIgnoreCase(sancion.getTipoSancion())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Solo se pueden descargar sanciones graves"));
            }

            // Devuelve los datos en JSON
            Map<String, String> reporte = new HashMap<>();
            reporte.put("titulo", "REPORTE DE SANCIÓN GRAVE");
            reporte.put("centro", "Centro Escolar Gustavo Vides Valdes");
            reporte.put("alumno", sancion.getAlumno().getNombre_alumno() + " " + sancion.getAlumno().getApellido_alumno());
            reporte.put("nie", String.valueOf(sancion.getAlumno().getNie()));
            reporte.put("fecha", sancion.getFechaSancion().toString());
            reporte.put("tipo", sancion.getTipoSancion());
            reporte.put("descripcion", sancion.getDescripcionSancion());

            return ResponseEntity.ok(reporte);
            
        } catch (Exception e) {
            System.err.println("Error al generar reporte: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}