package com.PRISMA.Materia;

import java.util.List;
import java.util.Optional;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.Docente.Excepciones.ResourceNotFoundException;
import com.PRISMA.Entity.Materia;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/gestionarMaterias/")
public class MateriaControlador {
    @Autowired
    private MateriaRepositorio repositorio;
    
    //Listar Materias
    @GetMapping("/materias")
    public List<Materia> listarMaterias(){
        return repositorio.obtenerMateriasActivasOrdenadas();
    }

    //GuardarMateria
    @PostMapping("/materias")
    public Materia guardarMateria(@RequestBody Materia materia){
        materia.setEstado_materia(true);
        Materia savedMateria = repositorio.save(materia);

        return savedMateria;
    }
    //ObtenerMateria x codigo
    @GetMapping("/materias/{codigoMateria}")
    public ResponseEntity<Materia> obtenerMateria(@PathVariable("codigoMateria") String id) {
        Materia materia = repositorio.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No existe la materia con codido: " + id));
            return ResponseEntity.ok(materia);
    }
    //Ya obtenido podemos actualizar
    @PutMapping("materias/{codigoMateria}")
    public ResponseEntity<Materia> actualizarMateria(@PathVariable("codigoMateria") String id, @RequestBody Materia detallesMateria) {
        Materia materia = repositorio.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la materia con codigo: "+id));
            materia.setNombre_materia(detallesMateria.getNombre_materia());
            materia.setTipo_materia(detallesMateria.getTipo_materia());
            Materia materiaActualizada = repositorio.save(materia);
        
        return ResponseEntity.ok(materiaActualizada);
    }
    
    //activa o desactiva las materias
    @PutMapping("/materias/{codigoMateria}/estado")
    public ResponseEntity<?> cambiarEstadoMateria(@PathVariable String codigoMateria, @RequestBody Map<String,Boolean> body){
        Optional<Materia> materiaOpt = repositorio.findById(codigoMateria);
        if (!materiaOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Materia materia = materiaOpt.get();
        materia.setEstado_materia(body.get("estado_materia"));
        repositorio.save(materia);

        return ResponseEntity.ok().build();
    }
    //comprueba si ya agregamos esta materia
    @GetMapping("/materias/existe/{codigoMateria}")
    public ResponseEntity<Boolean> existeMateria(@PathVariable String codigoMateria) {
        boolean existe = repositorio.existsById(codigoMateria);
        return ResponseEntity.ok(existe);
    }
    
}