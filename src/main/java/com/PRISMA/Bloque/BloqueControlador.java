package com.PRISMA.Bloque;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.Alumno.Grado.GradoRepositorio;
import com.PRISMA.Docente.DocenteRepositorio;
import com.PRISMA.Entity.Bloque;
import com.PRISMA.Entity.Docente;
import com.PRISMA.Entity.Grado;
import com.PRISMA.Entity.Materia;
import com.PRISMA.Materia.MateriaRepositorio;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/asignarDocenteAMateria/")
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
}
