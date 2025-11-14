package com.PRISMA.Administracion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.PRISMA.Alumno.Grado.GradoRepositorio;
import com.PRISMA.Docente.DocenteRepositorio;
import com.PRISMA.Entity.Coordinacion;
import com.PRISMA.Entity.Docente;
import com.PRISMA.Entity.Grado;

@Service
@Transactional
public class CoordinacionServicio {

    @Autowired
    private CoordinacionRepositorio coordinacionRepositorio;

    @Autowired
    private DocenteRepositorio docenteRepositorio;

    @Autowired
    private GradoRepositorio gradoRepositorio;

    /**
     * Listar todas las asignaciones de coordinadores
     * @param anioAcademico Filtro opcional por año académico
     * @return Lista de coordinaciones formateadas
     */
    public List<Map<String, Object>> listarAsignaciones(Integer anioAcademico) {
        List<Coordinacion> coordinaciones;
        
        if (anioAcademico != null) {
            coordinaciones = coordinacionRepositorio.findByAnioAcademico(anioAcademico);
        } else {
            coordinaciones = coordinacionRepositorio.findAll();
        }
        
        return coordinaciones.stream()
                .map(this::mapearCoordinacionAResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtener docentes disponibles (activos)
     * @return Lista de docentes activos
     */
    public List<Map<String, Object>> obtenerDocentesDisponibles() {
        return docenteRepositorio.obtenerDocentesActivos().stream()
                .map(this::mapearDocenteAResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtener grados disponibles (sin coordinador asignado)
     * @param anioAcademico Filtro opcional por año académico
     * @return Lista de grados sin coordinador
     */
    public List<Map<String, Object>> obtenerGradosDisponibles(Integer anioAcademico) {
        List<Grado> todosGrados;
        
        if (anioAcademico != null) {
            todosGrados = gradoRepositorio.findByAnioAcademico_anio(anioAcademico);
        } else {
            todosGrados = gradoRepositorio.findAll();
        }
        
        return todosGrados.stream()
                .filter(g -> !coordinacionRepositorio.existsByGradoId(g.getId_grado()))
                .filter(Grado::isEstadoGrado)
                .map(this::mapearGradoAResponse)
                .collect(Collectors.toList());
    }

    /**
     * Asignar un coordinador a un grado
     * @param duiDocente DUI del docente
     * @param idGrado ID del grado
     * @return Coordinación creada
     */
    public Map<String, Object> asignarCoordinador(String duiDocente, Integer idGrado) {
        // Validar docente
        Docente docente = docenteRepositorio.findById(duiDocente)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado con DUI: " + duiDocente));
        
        if (!docente.isDocente_Activo()) {
            throw new RuntimeException("El docente no está activo");
        }
        
        // Validar grado
        Grado grado = gradoRepositorio.findById(idGrado)
                .orElseThrow(() -> new RuntimeException("Grado no encontrado con ID: " + idGrado));
        
        if (!grado.isEstadoGrado()) {
            throw new RuntimeException("El grado no está activo");
        }
        
        // Validar que el grado no tenga coordinador
        if (coordinacionRepositorio.existsByGradoId(idGrado)) {
            throw new RuntimeException("Este grado ya tiene un coordinador asignado");
        }
        
        // Crear coordinación
        Coordinacion nuevaCoordinacion = new Coordinacion(grado, docente);
        Coordinacion guardada = coordinacionRepositorio.save(nuevaCoordinacion);
        
        return mapearCoordinacionAResponse(guardada);
    }

    /**
     * Actualizar el coordinador de un grado
     * @param idCoordinacion ID de la coordinación
     * @param duiDocente Nuevo DUI del docente
     * @return Coordinación actualizada
     */
    public Map<String, Object> actualizarCoordinador(Long idCoordinacion, String duiDocente) {
        // Buscar coordinación
        Coordinacion coordinacion = coordinacionRepositorio.findById(idCoordinacion)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada con ID: " + idCoordinacion));
        
        // Validar nuevo docente
        Docente nuevoDocente = docenteRepositorio.findById(duiDocente)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado con DUI: " + duiDocente));
        
        if (!nuevoDocente.isDocente_Activo()) {
            throw new RuntimeException("El docente no está activo");
        }
        
        // Actualizar
        coordinacion.setDocente(nuevoDocente);
        Coordinacion actualizada = coordinacionRepositorio.save(coordinacion);
        
        return mapearCoordinacionAResponse(actualizada);
    }

    /**
     * Eliminar una asignación de coordinador
     * @param idCoordinacion ID de la coordinación a eliminar
     */
    public void eliminarCoordinador(Long idCoordinacion) {
        if (!coordinacionRepositorio.existsById(idCoordinacion)) {
            throw new RuntimeException("Asignación no encontrada con ID: " + idCoordinacion);
        }
        coordinacionRepositorio.deleteById(idCoordinacion);
    }

    /**
     * Obtener detalle de una coordinación específica
     * @param idCoordinacion ID de la coordinación
     * @return Detalle de la coordinación
     */
    public Map<String, Object> obtenerDetalle(Long idCoordinacion) {
        Coordinacion coordinacion = coordinacionRepositorio.findById(idCoordinacion)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada con ID: " + idCoordinacion));
        
        return mapearCoordinacionAResponse(coordinacion);
    }

    /**
     * Verificar si un grado tiene coordinador
     * @param idGrado ID del grado
     * @return true si tiene coordinador, false si no
     */
    public boolean tieneCoordinador(Integer idGrado) {
        return coordinacionRepositorio.existsByGradoId(idGrado);
    }

    /**
     * Obtener coordinaciones de un docente específico
     * @param duiDocente DUI del docente
     * @return Lista de coordinaciones del docente
     */
    public List<Map<String, Object>> obtenerCoordinacionesPorDocente(String duiDocente) {
        return coordinacionRepositorio.findByDocenteDui(duiDocente).stream()
                .map(this::mapearCoordinacionAResponse)
                .collect(Collectors.toList());
    }

    // ========== MÉTODOS PRIVADOS DE MAPEO ==========

    private Map<String, Object> mapearCoordinacionAResponse(Coordinacion c) {
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
            datos.put("gradoCompleto", c.getGrado().getNombre_grado() + " - Sección " + c.getGrado().getSeccion());
            
            if (c.getGrado().getAnioAcademico() != null) {
                datos.put("anioAcademico", c.getGrado().getAnioAcademico().getAnio());
            }
        }
        
        return datos;
    }

    private Map<String, Object> mapearDocenteAResponse(Docente d) {
        Map<String, Object> datos = new HashMap<>();
        datos.put("duiDocente", d.getDuiDocente());
        datos.put("nombreCompleto", d.getNombre_Docente() + " " + d.getApellido_Docente());
        datos.put("correo", d.getCorreo_Docente());
        datos.put("telefono", d.getTelefono_Docente());
        return datos;
    }

    private Map<String, Object> mapearGradoAResponse(Grado g) {
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
    }
}