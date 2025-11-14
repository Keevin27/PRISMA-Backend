package com.PRISMA.Bloque;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.Alumno.Grado.GradoRepositorio;
import com.PRISMA.Docente.DocenteRepositorio;
import com.PRISMA.Entity.Bloque;
import com.PRISMA.Entity.Docente;
import com.PRISMA.Entity.Grado;
import com.PRISMA.Entity.Materia;
import com.PRISMA.Materia.MateriaRepositorio;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/asignarDocenteAMateria/")
@CrossOrigin(origins="http://localhost:4200/")
public class BloqueControlador {
    @Autowired
    private BloqueRepositorio bloqueRepositorio;
    @Autowired
    private MateriaRepositorio materiaRepositorio;
    @Autowired
    private DocenteRepositorio docenteRepositorio;
    @Autowired
    private GradoRepositorio gradoRepositorio;


    
    @PostMapping("/bloques")
    public ResponseEntity<List<Bloque>> guardarBloques(@RequestBody Map <String,Object>datos) {
        
        String duiDocente = (String) datos.get("duiDocente");
        String codigoMateria = (String) datos.get("codigoMateria");
        List <Integer> grados =(List<Integer>) datos.get("grados");
        Integer anioAcademico = Integer.valueOf(datos.get("anioAcademico").toString());
        
        Optional<Docente> docenteOpt = docenteRepositorio.findById(duiDocente);
        Optional<Materia> materiaOpt = materiaRepositorio.findById(codigoMateria);
        
        if (docenteOpt.isEmpty() && materiaOpt.isEmpty()) {
            throw new RuntimeException("Valores no encontrados");
        }

        
        List<Bloque> bloques = new ArrayList<>();
        for (Integer idGrado : grados) {
            Optional<Grado> gradoOpt = gradoRepositorio.findById(idGrado);
            if (gradoOpt.isPresent()) {
                Bloque bloque = new Bloque();
                bloque.setDocente(docenteOpt.get());
                bloque.setMateria(materiaOpt.get());
                bloque.setGrado(gradoOpt.get());
                bloque.setAnioAcademico(anioAcademico);
                bloques.add(bloqueRepositorio.save(bloque));
            }
        }
        return ResponseEntity.ok(bloques);
    }

    @GetMapping("/bloques/materia/{codigo_materia}")
    public ResponseEntity<List<Map<String, Object>>> obtenerAsignacionesPorMateria(@PathVariable String codigo_materia) {
        List<Bloque> bloques = bloqueRepositorio.findByCodigoMateria(codigo_materia);

        if (bloques.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<Map<String, Object>> resultado = bloques.stream().map(b -> {
        Map<String, Object> datos = new HashMap<>();
        datos.put("idBloque", b.getId_bloque());
        datos.put("gradoId", b.getGrado() != null ? b.getGrado().getId_grado() : null);
        datos.put("nombreGrado", b.getGrado() != null ? b.getGrado().getNombre_grado() : null);
        datos.put("duiDocente", b.getDocente() != null ? b.getDocente().getDuiDocente() : null);
        datos.put("nombreDocente", b.getDocente() != null ? (b.getDocente().getNombre_Docente() + " " + b.getDocente().getApellido_Docente()) : null);
        datos.put("anioAcademico", b.getAnioAcademico());
        return datos;
    }).collect(Collectors.toList());
        return ResponseEntity.ok(resultado);
    }
    
    @DeleteMapping("/bloques/{duiDocente}/{codigoMateria}/{idGrado}")
    public ResponseEntity<Void> eliminarBloque(
            @PathVariable String duiDocente,
            @PathVariable String codigoMateria,
            @PathVariable Integer idGrado) {

        Optional<Bloque> bloqueOpt = bloqueRepositorio.findByDocenteAndMateriaAndGrado(
            duiDocente, codigoMateria, idGrado);

        if (bloqueOpt.isPresent()) {
            bloqueRepositorio.delete(bloqueOpt.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/bloques/grados-disponibles/{codigoMateria}")
    public ResponseEntity<List<Grado>> obtenerGradosDisponibles(@PathVariable String codigoMateria) {
        List<Grado> grados = gradoRepositorio.findGradosDisponiblesPorMateria(codigoMateria);
        return ResponseEntity.ok(grados);
    }


//Para modulo de notas
@GetMapping("/bloques")
public ResponseEntity<List<Map<String, Object>>> obtenerTodosBloques(
        @RequestParam(required = false) Integer anioAcademico) {
    
    List<Bloque> bloques;
    
    // FILTRAR POR AÑO SI SE PROPORCIONA
    if (anioAcademico != null) {
        bloques = bloqueRepositorio.findByAnioAcademico(anioAcademico);
    } else {
        bloques = bloqueRepositorio.findAll();
    }
    
    // Formatear respuesta con la estructura completa
    List<Map<String, Object>> resultado = bloques.stream().map(b -> {
        Map<String, Object> datos = new HashMap<>();
        datos.put("id_bloque", b.getId_bloque());
        datos.put("anioAcademico", b.getAnioAcademico());
        
        // Datos del grado
        if (b.getGrado() != null) {
            Map<String, Object> grado = new HashMap<>();
            grado.put("id_grado", b.getGrado().getId_grado());
            grado.put("nombre_grado", b.getGrado().getNombre_grado());
            grado.put("seccion", b.getGrado().getSeccion());
            datos.put("grado", grado);
        }
        
        // Datos de la materia
        if (b.getMateria() != null) {
            Map<String, Object> materia = new HashMap<>();
            materia.put("codigo_materia", b.getMateria().getCodigo_materia());
            materia.put("nombre_materia", b.getMateria().getNombre_materia());
            materia.put("tipo_materia", b.getMateria().getTipo_materia());
            datos.put("materia", materia);
        }
        
        // Datos del docente
        if (b.getDocente() != null) {
            Map<String, Object> docente = new HashMap<>();
            docente.put("duiDocente", b.getDocente().getDuiDocente());
            docente.put("nombre_Docente", b.getDocente().getNombre_Docente());
            docente.put("apellido_Docente", b.getDocente().getApellido_Docente());
            docente.put("nombreCompleto", b.getDocente().getNombre_Docente() + " " + b.getDocente().getApellido_Docente());
            datos.put("docente", docente);
        }
        
        return datos;
    }).collect(Collectors.toList());
    
    return ResponseEntity.ok(resultado);
}

//Notas
@GetMapping("/mis-bloques")
public ResponseEntity<List<Map<String, Object>>> obtenerMisBloquesDocente(
        @RequestParam(required = false) Integer anioAcademico) {
    try {
        // Obtener el correo del usuario autenticado desde el SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correoDocente = authentication.getName();
        
        System.out.println("Obteniendo bloques para docente: " + correoDocente);
        
        // Buscar el docente por correo
        Optional<Docente> docenteOpt = docenteRepositorio.findByCorreo(correoDocente);
        
        if (!docenteOpt.isPresent()) {
            System.out.println("Docente no encontrado con correo: " + correoDocente);
            return ResponseEntity.ok(Collections.emptyList());
        }
        
        Docente docente = docenteOpt.get();
        System.out.println("Docente encontrado: " + docente.getNombre_Docente() + " " + docente.getApellido_Docente());
        
        // OBTENER BLOQUES FILTRANDO POR AÑO SI SE PROPORCIONA
        List<Bloque> bloques;
        if (anioAcademico != null) {
            System.out.println("Filtrando por año académico: " + anioAcademico);
            bloques = bloqueRepositorio.findByDuiDocenteAndAnioAcademico(
                docente.getDuiDocente(), anioAcademico);
        } else {
            System.out.println("Obteniendo todos los bloques del docente (sin filtro de año)");
            bloques = bloqueRepositorio.findByDuiDocente(docente.getDuiDocente());
        }
        
        System.out.println("Bloques encontrados: " + bloques.size());
        
        // Formatear respuesta con la MISMA estructura que obtenerTodosBloques()
        List<Map<String, Object>> resultado = bloques.stream().map(b -> {
            Map<String, Object> datos = new HashMap<>();
            datos.put("id_bloque", b.getId_bloque());
            datos.put("anioAcademico", b.getAnioAcademico());
            
            // Datos del grado como objeto
            if (b.getGrado() != null) {
                Map<String, Object> grado = new HashMap<>();
                grado.put("id_grado", b.getGrado().getId_grado());
                grado.put("nombre_grado", b.getGrado().getNombre_grado());
                grado.put("seccion", b.getGrado().getSeccion());
                datos.put("grado", grado);
            }
            
            // Datos de la materia como objeto
            if (b.getMateria() != null) {
                Map<String, Object> materia = new HashMap<>();
                materia.put("codigo_materia", b.getMateria().getCodigo_materia());
                materia.put("nombre_materia", b.getMateria().getNombre_materia());
                materia.put("tipo_materia", b.getMateria().getTipo_materia());
                datos.put("materia", materia);
            }
            
            // Datos del docente (opcional, por si lo necesitas)
            if (b.getDocente() != null) {
                Map<String, Object> docenteData = new HashMap<>();
                docenteData.put("duiDocente", b.getDocente().getDuiDocente());
                docenteData.put("nombre_Docente", b.getDocente().getNombre_Docente());
                docenteData.put("apellido_Docente", b.getDocente().getApellido_Docente());
                docenteData.put("nombreCompleto", b.getDocente().getNombre_Docente() + " " + b.getDocente().getApellido_Docente());
                datos.put("docente", docenteData);
            }
            
            return datos;
        }).collect(Collectors.toList());
        
        System.out.println("Bloques formateados: " + resultado.size());
        
        return ResponseEntity.ok(resultado);
        
    } catch (Exception e) {
        System.out.println("Error al obtener bloques del docente: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.ok(Collections.emptyList());
    }
}

}
