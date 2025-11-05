package com.PRISMA.Notas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.PRISMA.Alumno.AlumnoRepositorio;
import com.PRISMA.Bloque.BloqueRepositorio;
import com.PRISMA.Entity.Actividad;
import com.PRISMA.Entity.Alumno;
import com.PRISMA.Entity.Bloque;
import com.PRISMA.Entity.NotaActividad;
import com.PRISMA.Entity.NotaTrimestre;
import com.PRISMA.Entity.Trimestre;

import jakarta.annotation.PostConstruct;

import org.springframework.web.bind.annotation.RestController;
//ALTER TABLE alumnos ADD CONSTRAINT alumnos_nie_unique UNIQUE (nie);
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/actividades")
public class ActividadControlador {

        @PostConstruct
    public void init() {
        System.out.println("✅ ActividadControlador INICIALIZADO");
        System.out.println("✅ Rutas disponibles:");
        System.out.println("   - GET  /actividades/listar");
        System.out.println("   - POST /actividades/crear");
        System.out.println("   - PUT  /actividades/actualizar/{id}");
        System.out.println("   - DELETE /actividades/eliminar/{id}");
    }

    @Autowired
    private ActividadRepositorio actividadRepositorio;
    
    @Autowired
    private BloqueRepositorio bloqueRepositorio;
    
    @Autowired
    private TrimestreRepositorio trimestreRepositorio;
    
    @Autowired
    private AlumnoRepositorio alumnoRepositorio;
    
    @Autowired
    private NotaActividadRepositorio notaActividadRepositorio;

      @Autowired
    private NotaTrimestreRepositorio notaTrimestreRepositorio;

    // ========================================================
    // PANTALLA 5: Listar actividades por bloque y trimestre
    // GET /actividades/listar?idBloque=1&trimestre=1
    // ========================================================
    @GetMapping("/listar")
    public ResponseEntity<Map<String, Object>> listarActividades(
            @RequestParam Long idBloque,
            @RequestParam Integer trimestre) {
        
        Map<String, Object> respuesta = new HashMap<>();
        
        try {
            if (trimestre < 1 || trimestre > 3) {
                respuesta.put("error", "El trimestre debe ser 1, 2 o 3");
                return ResponseEntity.badRequest().body(respuesta);
            }
            
            Optional<Bloque> bloqueOpt = bloqueRepositorio.findById(idBloque);
            if (!bloqueOpt.isPresent()) {
                respuesta.put("error", "Bloque no encontrado");
                return ResponseEntity.notFound().build();
            }
            
            Bloque bloque = bloqueOpt.get();
            respuesta.put("materia", bloque.getMateria().getNombre_materia());
            respuesta.put("grado", bloque.getGrado().getNombre_grado());
            respuesta.put("seccion", bloque.getGrado().getSeccion());
            respuesta.put("trimestre", trimestre);
            
            List<Actividad> actividades = actividadRepositorio.buscarPorBloqueYTrimestre(idBloque, trimestre);
            
            List<Map<String, Object>> listaActividades = new ArrayList<>();
            
            for (Actividad actividad : actividades) {
                Map<String, Object> actDatos = new HashMap<>();
                actDatos.put("idActividad", actividad.getId_actividad());
                actDatos.put("nombreActividad", actividad.getNombre_actividad());
                actDatos.put("ponderacion", actividad.getPonderacion_actividad());
                actDatos.put("fechaActividad", actividad.getFecha_actividad());
                listaActividades.add(actDatos);
            }
            
            respuesta.put("actividades", listaActividades);
            
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            respuesta.put("error", "Error al listar actividades: " + e.getMessage());
            return ResponseEntity.badRequest().body(respuesta);
        }
    }

    // ========================================================
    // Crear nueva actividad
    // POST /actividades/crear
    // Body: { "idBloque": 1, "trimestre": 1, "nombreActividad": "Examen", "ponderacion": 30.0, "fechaActividad": "2025-11-15" }
    // ========================================================
    @PostMapping("/crear")
    public ResponseEntity<Map<String, Object>> crearActividad(@RequestBody Map<String, Object> datos) {
        Map<String, Object> respuesta = new HashMap<>();
        
        try {
            Long idBloque = Long.valueOf(datos.get("idBloque").toString());
            Integer trimestre = Integer.valueOf(datos.get("trimestre").toString());
            String nombreActividad = (String) datos.get("nombreActividad");
            Double ponderacion = Double.valueOf(datos.get("ponderacion").toString());
            String fechaStr = (String) datos.get("fechaActividad");
            
            // Validaciones
            if (trimestre < 1 || trimestre > 3) {
                respuesta.put("error", "El trimestre debe ser 1, 2 o 3");
                return ResponseEntity.badRequest().body(respuesta);
            }
            
            if (ponderacion <= 0 || ponderacion > 100) {
                respuesta.put("error", "La ponderación debe estar entre 0 y 100");
                return ResponseEntity.badRequest().body(respuesta);
            }
            
            // Verificar que el bloque existe
            Optional<Bloque> bloqueOpt = bloqueRepositorio.findById(idBloque);
            if (!bloqueOpt.isPresent()) {
                respuesta.put("error", "Bloque no encontrado");
                return ResponseEntity.notFound().build();
            }
            
            // Buscar el trimestre
            Optional<Trimestre> trimestreOpt = trimestreRepositorio.buscarPorNumeroPeriodo(trimestre);
            if (!trimestreOpt.isPresent()) {
                respuesta.put("error", "Trimestre no encontrado");
                return ResponseEntity.notFound().build();
            }
            
            // Crear actividad
            Actividad actividad = new Actividad();
            actividad.setBloque(bloqueOpt.get());
            actividad.setTrimestre(trimestreOpt.get());
            actividad.setNombre_actividad(nombreActividad);
            actividad.setPonderacion_actividad(ponderacion);
            actividad.setFecha_actividad(LocalDate.parse(fechaStr));
            
            Actividad actividadGuardada = actividadRepositorio.save(actividad);
            
            respuesta.put("mensaje", "Actividad creada exitosamente");
            respuesta.put("idActividad", actividadGuardada.getId_actividad());
            
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            respuesta.put("error", "Error al crear actividad: " + e.getMessage());
            return ResponseEntity.badRequest().body(respuesta);
        }
    }

    // ========================================================
    // Actualizar actividad
    // PUT /actividades/actualizar/{idActividad}
    // Body: { "nombreActividad": "Examen Final", "ponderacion": 35.0, "fechaActividad": "2025-11-20" }
    // ========================================================
    @PutMapping("/actualizar/{idActividad}")
    public ResponseEntity<Map<String, Object>> actualizarActividad(
            @PathVariable Long idActividad,
            @RequestBody Map<String, Object> datos) {
        
        Map<String, Object> respuesta = new HashMap<>();
        
        try {
            Optional<Actividad> actividadOpt = actividadRepositorio.findById(idActividad);
            if (!actividadOpt.isPresent()) {
                respuesta.put("error", "Actividad no encontrada");
                return ResponseEntity.notFound().build();
            }
            
            Actividad actividad = actividadOpt.get();
            
            if (datos.containsKey("nombreActividad")) {
                actividad.setNombre_actividad((String) datos.get("nombreActividad"));
            }
            
            if (datos.containsKey("ponderacion")) {
                Double ponderacion = Double.valueOf(datos.get("ponderacion").toString());
                if (ponderacion <= 0 || ponderacion > 100) {
                    respuesta.put("error", "La ponderación debe estar entre 0 y 100");
                    return ResponseEntity.badRequest().body(respuesta);
                }
                actividad.setPonderacion_actividad(ponderacion);
            }
            
            if (datos.containsKey("fechaActividad")) {
                String fechaStr = (String) datos.get("fechaActividad");
                actividad.setFecha_actividad(LocalDate.parse(fechaStr));
            }
            
            actividadRepositorio.save(actividad);
            
            respuesta.put("mensaje", "Actividad actualizada exitosamente");
            
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            respuesta.put("error", "Error al actualizar actividad: " + e.getMessage());
            return ResponseEntity.badRequest().body(respuesta);
        }
    }

    // ========================================================
    // Eliminar actividad
    // DELETE /actividades/eliminar/{idActividad}
    // ========================================================
    @DeleteMapping("/eliminar/{idActividad}")
    public ResponseEntity<Map<String, Object>> eliminarActividad(@PathVariable Long idActividad) {
        Map<String, Object> respuesta = new HashMap<>();
        
        try {
            Optional<Actividad> actividadOpt = actividadRepositorio.findById(idActividad);
            if (!actividadOpt.isPresent()) {
                respuesta.put("error", "Actividad no encontrada");
                return ResponseEntity.notFound().build();
            }
            
            // Eliminar todas las notas asociadas primero
            List<NotaActividad> notas = notaActividadRepositorio.buscarPorActividad(idActividad);
            notaActividadRepositorio.deleteAll(notas);
            
            // Eliminar la actividad
            actividadRepositorio.deleteById(idActividad);
            
            respuesta.put("mensaje", "Actividad eliminada exitosamente");
            
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            respuesta.put("error", "Error al eliminar actividad: " + e.getMessage());
            return ResponseEntity.badRequest().body(respuesta);
        }
    }

    // ========================================================
    // PANTALLA 5.1: Ver alumnos para asignar notas
    // GET /actividades/alumnos-para-notas/{idActividad}
    // ========================================================
    @GetMapping("/alumnos-para-notas/{idActividad}")
    public ResponseEntity<Map<String, Object>> obtenerAlumnosParaNotas(@PathVariable Long idActividad) {
        Map<String, Object> respuesta = new HashMap<>();
        
        try {
            Optional<Actividad> actividadOpt = actividadRepositorio.findById(idActividad);
            if (!actividadOpt.isPresent()) {
                respuesta.put("error", "Actividad no encontrada");
                return ResponseEntity.notFound().build();
            }
            
            Actividad actividad = actividadOpt.get();
            
            respuesta.put("nombreActividad", actividad.getNombre_actividad());
            respuesta.put("ponderacion", actividad.getPonderacion_actividad());
            
            // Obtener alumnos del grado
            List<Alumno> alumnos = alumnoRepositorio.buscarPorGrado(
                    actividad.getBloque().getGrado().getId_grado());
            
            List<Map<String, Object>> listaAlumnos = new ArrayList<>();
            
            for (Alumno alumno : alumnos) {
                Map<String, Object> alumnoData = new HashMap<>();
                alumnoData.put("nie", alumno.getNie());
                alumnoData.put("nombreCompleto", alumno.getNombre_alumno() + " " + alumno.getApellido_alumno());
                
                // Buscar si ya tiene nota
                Optional<NotaActividad> notaOpt = notaActividadRepositorio
                        .buscarPorActividadYAlumno(idActividad, alumno.getNie());
                
                if (notaOpt.isPresent()) {
                    alumnoData.put("nota", notaOpt.get().getNota_obtenida());
                    alumnoData.put("fechaModificacion", notaOpt.get().getFecha_modificacion());
                    alumnoData.put("idNotaActividad", notaOpt.get().getId_notaActividad());
                } else {
                    alumnoData.put("nota", null);
                    alumnoData.put("fechaModificacion", null);
                    alumnoData.put("idNotaActividad", null);
                }
                
                listaAlumnos.add(alumnoData);
            }
            
            respuesta.put("alumnos", listaAlumnos);
            
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            respuesta.put("error", "Error al obtener alumnos: " + e.getMessage());
            return ResponseEntity.badRequest().body(respuesta);
        }
    }

    // ========================================================
    // Asignar o actualizar nota de un alumno en una actividad
    // POST /actividades/asignar-nota
    // Body: { "idActividad": 1, "nie": 12345, "nota": 8.5 }
    // ========================================================
    
    @PostMapping("/asignar-nota")
public ResponseEntity<Map<String, Object>> asignarNota(@RequestBody Map<String, Object> datos) {
    Map<String, Object> respuesta = new HashMap<>();
    
    try {
        Long idActividad = Long.valueOf(datos.get("idActividad").toString());
        Integer nie = Integer.valueOf(datos.get("nie").toString());
        Double nota = Double.valueOf(datos.get("nota").toString());
        
        // Validar nota
        if (nota < 0 || nota > 10) {
            respuesta.put("error", "La nota debe estar entre 0 y 10");
            return ResponseEntity.badRequest().body(respuesta);
        }
        
        // Verificar actividad
        Optional<Actividad> actividadOpt = actividadRepositorio.findById(idActividad);
        if (!actividadOpt.isPresent()) {
            respuesta.put("error", "Actividad no encontrada");
            return ResponseEntity.notFound().build();
        }
        
        Actividad actividad = actividadOpt.get();
        
        // Verificar alumno
        Optional<Alumno> alumnoOpt = alumnoRepositorio.buscarPorNie(nie);
        if (!alumnoOpt.isPresent()) {
            respuesta.put("error", "Alumno no encontrado");
            return ResponseEntity.notFound().build();
        }
        
        // Buscar si ya existe una nota
        Optional<NotaActividad> notaExistenteOpt = notaActividadRepositorio
                .buscarPorActividadYAlumno(idActividad, nie);
        
        NotaActividad notaActividad;
        
        if (notaExistenteOpt.isPresent()) {
            // Actualizar nota existente
            notaActividad = notaExistenteOpt.get();
            notaActividad.setNota_obtenida(nota);
            notaActividad.setFecha_modificacion(LocalDate.now());
        } else {
            // Crear nueva nota
            notaActividad = new NotaActividad();
            notaActividad.setActividad(actividadOpt.get());
            notaActividad.setAlumno(alumnoOpt.get());
            notaActividad.setNota_obtenida(nota);
            notaActividad.setFecha_modificacion(LocalDate.now());
        }
        
        notaActividadRepositorio.save(notaActividad);
        
        //  CALCULAR Y GUARDAR NOTA DEL TRIMESTRE
        Integer numeroTrimestre = actividad.getTrimestre().getNumero_periodo();
        Long idBloque = actividad.getBloque().getId_bloque();
        calcularYGuardarNotaTrimestre(idBloque, nie, numeroTrimestre);
        
        respuesta.put("mensaje", "Nota asignada exitosamente");
        
        return ResponseEntity.ok(respuesta);
        
    } catch (Exception e) {
        respuesta.put("error", "Error al asignar nota: " + e.getMessage());
        return ResponseEntity.badRequest().body(respuesta);
    }
}    

    // ========================================================
    // Eliminar nota de un alumno
    // DELETE /actividades/eliminar-nota/{idNotaActividad}
    // ========================================================
    @DeleteMapping("/eliminar-nota/{idNotaActividad}")
public ResponseEntity<Map<String, Object>> eliminarNota(@PathVariable Long idNotaActividad) {
    Map<String, Object> respuesta = new HashMap<>();
    
    try {
        Optional<NotaActividad> notaOpt = notaActividadRepositorio.findById(idNotaActividad);
        if (!notaOpt.isPresent()) {
            respuesta.put("error", "Nota no encontrada");
            return ResponseEntity.notFound().build();
        }
        
        NotaActividad notaActividad = notaOpt.get();
        
        // Guardar datos antes de eliminar
        Integer nie = notaActividad.getAlumno().getNie();
        Long idBloque = notaActividad.getActividad().getBloque().getId_bloque();
        Integer numeroTrimestre = notaActividad.getActividad().getTrimestre().getNumero_periodo();
        
        // Eliminar la nota
        notaActividadRepositorio.deleteById(idNotaActividad);
        
        //  RECALCULAR Y ACTUALIZAR NOTA DEL TRIMESTRE
        calcularYGuardarNotaTrimestre(idBloque, nie, numeroTrimestre);
        
        respuesta.put("mensaje", "Nota eliminada exitosamente");
        
        return ResponseEntity.ok(respuesta);
        
    } catch (Exception e) {
        respuesta.put("error", "Error al eliminar nota: " + e.getMessage());
        return ResponseEntity.badRequest().body(respuesta);
    }
}

    // ========================================================
// MÉTODO AUXILIAR: Calcular y guardar nota del trimestre
// ========================================================
private void calcularYGuardarNotaTrimestre(Long idBloque, Integer nie, Integer numeroTrimestre) {
    try {
        // Buscar el trimestre
        Optional<Trimestre> trimestreOpt = trimestreRepositorio.buscarPorNumeroPeriodo(numeroTrimestre);
        if (!trimestreOpt.isPresent()) {
            return;
        }
        
        Trimestre trimestre = trimestreOpt.get();
        
        // Buscar el bloque
        Optional<Bloque> bloqueOpt = bloqueRepositorio.findById(idBloque);
        if (!bloqueOpt.isPresent()) {
            return;
        }
        
        // Buscar el alumno
        Optional<Alumno> alumnoOpt = alumnoRepositorio.buscarPorNie(nie);
        if (!alumnoOpt.isPresent()) {
            return;
        }
        
        // Obtener todas las actividades del trimestre
        List<Actividad> actividades = actividadRepositorio.buscarPorBloqueYTrimestre(idBloque, numeroTrimestre);
        
        if (actividades.isEmpty()) {
            return;
        }
        
        // Calcular nota del trimestre
        double notaTrimestre = 0.0;
        double ponderacionTotal = 0.0;
        
        for (Actividad actividad : actividades) {
            Optional<NotaActividad> notaActOpt = notaActividadRepositorio
                    .buscarPorActividadYAlumno(actividad.getId_actividad(), nie);
            
            if (notaActOpt.isPresent()) {
                double nota = notaActOpt.get().getNota_obtenida();
                double ponderacion = actividad.getPonderacion_actividad();
                notaTrimestre += (nota * ponderacion) / 100.0;
                ponderacionTotal += ponderacion;
            }
        }
        
        // Solo guardar si hay notas
        if (ponderacionTotal > 0) {
            double notaFinal = Math.round(notaTrimestre * 100.0) / 100.0;
            
            // Buscar si ya existe un registro de nota_trimestre
            Optional<NotaTrimestre> notaTrimestreExistente = notaTrimestreRepositorio.buscarPorAlumnoBloqueYTrimestre(nie, idBloque, numeroTrimestre);
            
            NotaTrimestre notaTrimestreEntity;
            
            if (notaTrimestreExistente.isPresent()) {
                // Actualizar
                notaTrimestreEntity = notaTrimestreExistente.get();
                notaTrimestreEntity.setNota_trimestre(notaFinal);
            } else {
                // Crear nuevo
                notaTrimestreEntity = new NotaTrimestre();
                notaTrimestreEntity.setAlumno(alumnoOpt.get());
                notaTrimestreEntity.setBloque(bloqueOpt.get());
                notaTrimestreEntity.setTrimestre(trimestre);
                notaTrimestreEntity.setNota_trimestre(notaFinal);
            }
            
            notaTrimestreRepositorio.save(notaTrimestreEntity);
            
            System.out.println(" Nota trimestre guardada: " + notaFinal + " para NIE " + nie);
        }
        
    } catch (Exception e) {
        System.err.println("Error al calcular nota trimestre: " + e.getMessage());
    }
}
}
