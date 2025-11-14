package com.PRISMA.Alumno.Grado;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.AnioAcademico.AnioAcademicoRepositorio;
import com.PRISMA.Entity.AnioAcademico;
import com.PRISMA.Entity.Grado;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/Grado")
@CrossOrigin(origins="http://localhost:4200")
public class GradoControlador {
    @Autowired
    private GradoRepositorio repositorio;

    @Autowired
    private AnioAcademicoRepositorio repositorioAnio;

    // Secciones matutinas: A, B, C
    private static final List<String> SECCIONES_MATUTINAS = Arrays.asList("A", "B", "C");
    // Secciones vespertinas: D, E, F
    private static final List<String> SECCIONES_VESPERTINAS = Arrays.asList("D", "E", "F");

    @GetMapping("/grados")
    public List<Grado> obtenerGrados() {
        return repositorio.findAll();
    }

@GetMapping("/grados/activos")
public List<Grado> obtenerGradosActivos() {
    return repositorio.findGradosPorAnioActivo();
}

    @GetMapping("/grados/{anio}")
    public List<Grado> obtenerGradosPorAnio(@PathVariable int anio) {
        return repositorio.findByAnioAcademico_anio(anio);
    }
    

    // Método auxiliar para determinar el turno según la sección
    private String obtenerTurnoPorSeccion(String seccion) {
        if (SECCIONES_MATUTINAS.contains(seccion.toUpperCase())) {
            return "Matutino";
        } else if (SECCIONES_VESPERTINAS.contains(seccion.toUpperCase())) {
            return "Vespertino";
        }
        return "Matutino"; // Por defecto
    }

    //Crear oferta de grados (múltiples grados con secciones y turnos personalizados o automáticos)
    @PostMapping("/crear-oferta")
    public ResponseEntity<?> crearOferta(@RequestBody Map<String, Object> request) {
        try {
            int idAnioAcademico = (int) request.get("idAnioAcademico");
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> grados = (List<Map<String, Object>>) request.get("grados");

            // Buscar el año académico
            AnioAcademico anioAcademico = repositorioAnio.findById(idAnioAcademico)
                .orElseThrow(() -> new RuntimeException("Año académico no encontrado"));

            List<Grado> gradosCreados = new ArrayList<>();

            // Crear cada grado con sus secciones
            for (Map<String, Object> gradoData : grados) {
                String nombreGrado = (String) gradoData.get("nombre");
                
                // Verificar si viene el formato antiguo (secciones: [...]) o nuevo (seccion: "A", turno: "Matutino")
                if (gradoData.containsKey("secciones")) {
                    // Formato antiguo: { nombre: "Primero", secciones: ["A", "B", "C"] }
                    @SuppressWarnings("unchecked")
                    List<String> secciones = (List<String>) gradoData.get("secciones");
                    
                    for (String seccion : secciones) {
                        String turno = obtenerTurnoPorSeccion(seccion);
                        
                        Grado nuevoGrado = new Grado();
                        nuevoGrado.setNombre_grado(nombreGrado);
                        nuevoGrado.setSeccion(seccion);
                        nuevoGrado.setTurno_grado(turno);
                        nuevoGrado.setEstadoGrado(true);
                        nuevoGrado.setAnioAcademico(anioAcademico);

                        Grado gradoGuardado = repositorio.save(nuevoGrado);
                        gradosCreados.add(gradoGuardado);
                    }
                } else {
                    // Formato nuevo con turno personalizado: { nombre: "Primero", seccion: "A", turno: "Matutino" }
                    String seccion = (String) gradoData.get("seccion");
                    String turno = (String) gradoData.get("turno");
                    
                    // Si no viene turno, usar el automático
                    if (turno == null || turno.isEmpty()) {
                        turno = obtenerTurnoPorSeccion(seccion);
                    }
                    
                    Grado nuevoGrado = new Grado();
                    nuevoGrado.setNombre_grado(nombreGrado);
                    nuevoGrado.setSeccion(seccion);
                    nuevoGrado.setTurno_grado(turno);
                    nuevoGrado.setEstadoGrado(true);
                    nuevoGrado.setAnioAcademico(anioAcademico);

                    Grado gradoGuardado = repositorio.save(nuevoGrado);
                    gradosCreados.add(gradoGuardado);
                }
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(gradosCreados);
        } catch (Exception e) {
            e.printStackTrace(); // Para ver el error completo en la consola del backend
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al crear la oferta: " + e.getMessage());
        }
    }

    // Eliminar grado
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarGrado(@PathVariable Integer id) {
        try {
            // Verificar si el grado existe
            if (!repositorio.existsById(id)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Grado no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
            
            // Eliminar el grado
            repositorio.deleteById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Grado eliminado correctamente");
            response.put("id", id);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al eliminar el grado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}