package com.PRISMA.Notas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.Alumno.AlumnoRepositorio;
import com.PRISMA.Bloque.BloqueRepositorio;
import com.PRISMA.Entity.Actividad;
import com.PRISMA.Entity.Alumno;
import com.PRISMA.Entity.Bloque;
import com.PRISMA.Entity.NotaActividad;
import com.PRISMA.Entity.NotaTrimestre;
import com.PRISMA.Entity.Trimestre;
import com.PRISMA.Entity.NotaMateria;

@RestController
@RequestMapping("/notas/")
@CrossOrigin(origins = "http://localhost:4200/")
public class NotasControlador {

    @Autowired
    private BloqueRepositorio bloqueRepositorio;
    
    @Autowired
    private AlumnoRepositorio alumnoRepositorio;
    
    @Autowired
    private ActividadRepositorio actividadRepositorio;
    
    @Autowired
    private NotaActividadRepositorio notaActividadRepositorio;
    
    @Autowired
    private NotaTrimestreRepositorio notaTrimestreRepositorio;
    
    @Autowired
    private NotaMateriaRepositorio notaMateriaRepositorio;
    
    @Autowired
    private TrimestreRepositorio trimestreRepositorio;

    // ========================================================
    // PANTALLA 1: Consultar notas por materia
    // GET /notas/por-materia?idBloque=1&trimestre=1
    // ========================================================
    @GetMapping("/por-materia")
    public ResponseEntity<Map<String, Object>> consultarNotasPorMateria(
            @RequestParam Long idBloque,
            @RequestParam Integer trimestre) {
        
        Map<String, Object> respuesta = new HashMap<>();
        
        try {
            // Validar trimestre
            if (trimestre < 1 || trimestre > 3) {
                respuesta.put("error", "El trimestre debe ser 1, 2 o 3");
                return ResponseEntity.badRequest().body(respuesta);
            }
            
            // Buscar bloque
            Optional<Bloque> bloqueOpt = bloqueRepositorio.findById(idBloque);
            if (!bloqueOpt.isPresent()) {
                respuesta.put("error", "Bloque no encontrado");
                return ResponseEntity.notFound().build();
            }
            
            Bloque bloque = bloqueOpt.get();
            
            // Información del encabezado
            respuesta.put("materia", bloque.getMateria().getNombre_materia());
            respuesta.put("grado", bloque.getGrado().getNombre_grado());
            respuesta.put("seccion", bloque.getGrado().getSeccion());
            respuesta.put("trimestre", trimestre);
            
            // Obtener actividades del trimestre
            List<Actividad> actividades = actividadRepositorio.buscarPorBloqueYTrimestre(idBloque, trimestre);
            
            // Información de actividades para el header
            List<Map<String, Object>> headerActividades = new ArrayList<>();
            for (Actividad act : actividades) {
                Map<String, Object> actInfo = new HashMap<>();
                actInfo.put("idActividad", act.getId_actividad());
                actInfo.put("nombreActividad", act.getNombre_actividad());
                actInfo.put("ponderacion", act.getPonderacion_actividad());
                headerActividades.add(actInfo);
            }
            respuesta.put("actividades", headerActividades);
            
            // Obtener alumnos del grado
            List<Alumno> alumnos = alumnoRepositorio.buscarPorGrado(bloque.getGrado().getId_grado());
            
            // Construir tabla de notas
            List<Map<String, Object>> tablaNotas = new ArrayList<>();
            
            for (Alumno alumno : alumnos) {
                Map<String, Object> filaAlumno = new HashMap<>();
                filaAlumno.put("nie", alumno.getNie());
                filaAlumno.put("nombreCompleto", alumno.getNombre_alumno() + " " + alumno.getApellido_alumno());
                
                List<Double> notasActividades = new ArrayList<>();
                double sumaNotasPonderadas = 0.0;
                
                for (Actividad actividad : actividades) {
                    Optional<NotaActividad> notaOpt = notaActividadRepositorio
                            .buscarPorActividadYAlumno(actividad.getId_actividad(), alumno.getNie());
                    
                    if (notaOpt.isPresent()) {
                        double nota = notaOpt.get().getNota_obtenida();
                        notasActividades.add(nota);
                        // Calcular nota ponderada
                        double notaPonderada = (nota * actividad.getPonderacion_actividad()) / 100.0;
                        sumaNotasPonderadas += notaPonderada;
                    } else {
                        notasActividades.add(null);
                    }
                }
                
                filaAlumno.put("notas", notasActividades);
                filaAlumno.put("notaFinalTrimestre", Math.round(sumaNotasPonderadas * 100.0) / 100.0);
                
                tablaNotas.add(filaAlumno);
            }
            
            respuesta.put("alumnos", tablaNotas);
            respuesta.put("totalActividades", actividades.size());
            
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            respuesta.put("error", "Error al consultar notas: " + e.getMessage());
            return ResponseEntity.badRequest().body(respuesta);
        }
    }

    // ========================================================
    // PANTALLA 2: Listar alumnos por grado
    // GET /notas/alumnos-por-grado?idGrado=1&trimestre=1
    // ========================================================
    @GetMapping("/alumnos-por-grado")
    public ResponseEntity<Map<String, Object>> obtenerAlumnosPorGrado(
            @RequestParam Integer idGrado,
            @RequestParam Integer trimestre) {
        
        Map<String, Object> respuesta = new HashMap<>();
        
        try {
            if (trimestre < 1 || trimestre > 3) {
                respuesta.put("error", "El trimestre debe ser 1, 2 o 3");
                return ResponseEntity.badRequest().body(respuesta);
            }
            
            List<Alumno> alumnos = alumnoRepositorio.buscarPorGrado(idGrado);
            
            List<Map<String, Object>> listaAlumnos = new ArrayList<>();
            
            for (Alumno alumno : alumnos) {
                Map<String, Object> datosAlumno = new HashMap<>();
                datosAlumno.put("idAlumno", alumno.getIdAlumno());
                datosAlumno.put("nie", alumno.getNie());
                datosAlumno.put("nombreCompleto", alumno.getNombre_alumno() + " " + alumno.getApellido_alumno());
                listaAlumnos.add(datosAlumno);
            }
            
            respuesta.put("alumnos", listaAlumnos);
            respuesta.put("trimestre", trimestre);
            respuesta.put("idGrado", idGrado);
            
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            respuesta.put("error", "Error al obtener alumnos: " + e.getMessage());
            return ResponseEntity.badRequest().body(respuesta);
        }
    }

    // ========================================================
    // PANTALLA 2.1: Detalle de notas por alumno
    // GET /notas/detalle-alumno/{nie}?idGrado=1&trimestre=1
    // ========================================================
    @GetMapping("/detalle-alumno/{nie}")
    public ResponseEntity<Map<String, Object>> obtenerDetalleNotasAlumno(
            @PathVariable Integer nie,
            @RequestParam Integer idGrado,
            @RequestParam Integer trimestre) {
        
        Map<String, Object> respuesta = new HashMap<>();
        
        try {
            Optional<Alumno> alumnoOpt = alumnoRepositorio.buscarPorNie(nie);
            if (!alumnoOpt.isPresent()) {
                respuesta.put("error", "Alumno no encontrado");
                return ResponseEntity.notFound().build();
            }
            
            Alumno alumno = alumnoOpt.get();
            respuesta.put("nie", alumno.getNie());
            respuesta.put("nombreCompleto", alumno.getNombre_alumno() + " " + alumno.getApellido_alumno());
            respuesta.put("trimestre", trimestre);
            
            // Obtener bloques del grado
            List<Bloque> bloques = bloqueRepositorio.findAll().stream()
                    .filter(b -> b.getGrado().getId_grado() == idGrado)
                    .toList();
            
            List<Map<String, Object>> materias = new ArrayList<>();
            
            for (Bloque bloque : bloques) {
                Map<String, Object> materia = new HashMap<>();
                materia.put("nombreMateria", bloque.getMateria().getNombre_materia());
                materia.put("idBloque", bloque.getId_bloque());
                
                // Obtener actividades del trimestre
                List<Actividad> actividades = actividadRepositorio
                        .buscarPorBloqueYTrimestre(bloque.getId_bloque(), trimestre);
                
                List<Map<String, Object>> actividadesConNotas = new ArrayList<>();
                double totalNota = 0.0;
                
                for (Actividad actividad : actividades) {
                    Map<String, Object> actDatos = new HashMap<>();
                    actDatos.put("nombreActividad", actividad.getNombre_actividad());
                    actDatos.put("ponderacion", actividad.getPonderacion_actividad());
                    
                    Optional<NotaActividad> notaOpt = notaActividadRepositorio
                            .buscarPorActividadYAlumno(actividad.getId_actividad(), nie);
                    
                    if (notaOpt.isPresent()) {
                        double nota = notaOpt.get().getNota_obtenida();
                        actDatos.put("nota", nota);
                        double notaPonderada = (nota * actividad.getPonderacion_actividad()) / 100.0;
                        totalNota += notaPonderada;
                    } else {
                        actDatos.put("nota", null);
                    }
                    
                    actividadesConNotas.add(actDatos);
                }
                
                materia.put("actividades", actividadesConNotas);
                materia.put("notaFinalTrimestre", Math.round(totalNota * 100.0) / 100.0);
                
                materias.add(materia);
            }
            
            respuesta.put("materias", materias);
            
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            respuesta.put("error", "Error al obtener detalle: " + e.getMessage());
            return ResponseEntity.badRequest().body(respuesta);
        }
    }

    // ========================================================
// MÉTODO CORREGIDO: Reporte anual de un alumno
// Reemplaza el método existente en NotasControlador.java
// ========================================================

@GetMapping("/reporte-anual/{nie}/{idGrado}/{anio}")
public ResponseEntity<Map<String, Object>> obtenerReporteAnual(
        @PathVariable Integer nie,
        @PathVariable Integer idGrado,
        @PathVariable Integer anio) {
    
    Map<String, Object> respuesta = new HashMap<>();
    
    try {
        Optional<Alumno> alumnoOpt = alumnoRepositorio.buscarPorNie(nie);
        if (!alumnoOpt.isPresent()) {
            respuesta.put("error", "Alumno no encontrado");
            return ResponseEntity.notFound().build();
        }
        
        Alumno alumno = alumnoOpt.get();
        respuesta.put("nie", alumno.getNie());
        respuesta.put("nombreCompleto", alumno.getNombre_alumno() + " " + alumno.getApellido_alumno());
        
        List<Bloque> bloques = bloqueRepositorio.findAll().stream()
                .filter(b -> b.getGrado().getId_grado() == idGrado)
                .toList();
        
        List<Map<String, Object>> materias = new ArrayList<>();
        
        for (Bloque bloque : bloques) {
            Map<String, Object> materia = new HashMap<>();
            materia.put("nombreMateria", bloque.getMateria().getNombre_materia());
            materia.put("idBloque", bloque.getId_bloque());
            
            List<Double> notasTrimestres = new ArrayList<>();
            double sumaNotasTrimestres = 0.0;
            int trimestresConNota = 0;
            
            // Calcular nota de cada trimestre (1, 2, 3)
            for (int numeroTrimestre = 1; numeroTrimestre <= 3; numeroTrimestre++) {
                // Buscar trimestre
                Optional<Trimestre> trimestreOpt = trimestreRepositorio.buscarPorNumeroPeriodo(numeroTrimestre);
                
                if (trimestreOpt.isPresent()) {
                    // Obtener actividades del trimestre
                    List<Actividad> actividades = actividadRepositorio
                            .buscarPorBloqueYTrimestre(bloque.getId_bloque(), numeroTrimestre);
                    
                    if (actividades.isEmpty()) {
                        notasTrimestres.add(null);
                        continue;
                    }
                    
                    // Calcular nota del trimestre basada en actividades
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
                    
                    // Solo agregar si tiene notas
                    if (ponderacionTotal > 0) {
                        double notaFinalTrimestre = Math.round(notaTrimestre * 100.0) / 100.0;
                        notasTrimestres.add(notaFinalTrimestre);
                        sumaNotasTrimestres += notaFinalTrimestre;
                        trimestresConNota++;
                    } else {
                        notasTrimestres.add(null);
                    }
                } else {
                    notasTrimestres.add(null);
                }
            }
            
            materia.put("notasTrimestres", notasTrimestres);
            
            // Calcular nota final de la materia (promedio de trimestres)
            if (trimestresConNota > 0) {
                double notaFinalMateria = Math.round((sumaNotasTrimestres / trimestresConNota) * 100.0) / 100.0;
                materia.put("notaFinal", notaFinalMateria);
                materia.put("aprobado", notaFinalMateria >= 6.0);
            } else {
                materia.put("notaFinal", null);
                materia.put("aprobado", false);
            }
            
            materias.add(materia);
        }
        
        respuesta.put("materias", materias);
        
        // Calcular estadísticas generales
        long materiasAprobadas = materias.stream()
                .filter(m -> m.get("notaFinal") != null && (Double) m.get("notaFinal") >= 6.0)
                .count();
        
        long materiasReprobadas = materias.stream()
                .filter(m -> m.get("notaFinal") != null && (Double) m.get("notaFinal") < 6.0)
                .count();
        
        respuesta.put("totalMaterias", materias.size());
        respuesta.put("materiasAprobadas", materiasAprobadas);
        respuesta.put("materiasReprobadas", materiasReprobadas);
        
        return ResponseEntity.ok(respuesta);
        
    } catch (Exception e) {
        respuesta.put("error", "Error al generar reporte: " + e.getMessage());
        return ResponseEntity.badRequest().body(respuesta);
    }
}
}
