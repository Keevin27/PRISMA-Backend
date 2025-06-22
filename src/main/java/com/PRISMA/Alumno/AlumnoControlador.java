package com.PRISMA.Alumno;

import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.Entity.Alumno;

@RestController
@RequestMapping("/Alu/")
@CrossOrigin(origins = "http://localhost:4200/")
public class AlumnoControlador {

    @Autowired
    private AlumnoRepositorio repositorio;

    // Listar todos los alumnos
    @GetMapping("/alumnos")
    public List<Alumno> listarTodosLosAlumnos() {
        return repositorio.findAll();
    }

    // Listar alumnos por grado
    @GetMapping("/alumnos/{id_grado}")
    public List<Alumno> listarAlumnoPorGrado(@PathVariable int id_grado) {
        return repositorio.buscarPorGrado(id_grado);
    }

    // Buscar alumno por NIE
    @GetMapping("/alumnos/nie/{nie}")
    public ResponseEntity<Alumno> obtenerAlumnoPorNie(@PathVariable int nie) {
        Optional<Alumno> alumno = repositorio.buscarPorNie(nie);
        if (alumno.isPresent()) {
            return ResponseEntity.ok(alumno.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear nuevo alumno
    @PostMapping("/crearalumno")
    public ResponseEntity<String> guardarAlumno(@RequestBody Alumno alumno) {
        try {
            // Verificar si el NIE ya existe
            Optional<Alumno> existeNie = repositorio.buscarPorNie(alumno.getNie());
            if (existeNie.isPresent()) {
                return ResponseEntity.badRequest()
                    .body("Error: El NIE " + alumno.getNie() + " ya está registrado.");
            }
            
            // Establecer el estado como activo por defecto si no viene definido
            if (!alumno.isEstado_alumno()) {
                alumno.setEstado_alumno(true);
            }
            
            // Validar que el grado no sea nulo
            if (alumno.getGrado() == null) {
                return ResponseEntity.badRequest()
                    .body("Error: Debe seleccionar un grado válido.");
            }
            
            Alumno nuevoAlumno = repositorio.save(alumno);
            return ResponseEntity.ok("Alumno guardado exitosamente con ID: " + nuevoAlumno.getIdAlumno());
            
        } catch (Exception e) {
            System.err.println("Error al guardar alumno: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                .body("Error al guardar el alumno: " + e.getMessage());
        }
    }

    // Actualizar alumno existente
    @PutMapping("/alumnos/{id}")
    public ResponseEntity<Alumno> actualizarAlumno(@PathVariable int id, @RequestBody Alumno alumnoActualizado) {
        Optional<Alumno> alumnoExistente = repositorio.findById(id);
        
        if (alumnoExistente.isPresent()) {
            Alumno alumno = alumnoExistente.get();
            
            // Verificar si el NIE cambió y si ya existe en otro alumno
            if (alumno.getNie() != alumnoActualizado.getNie()) {
                Optional<Alumno> existeNie = repositorio.buscarPorNie(alumnoActualizado.getNie());
                if (existeNie.isPresent() && existeNie.get().getIdAlumno() != id) {
                    return ResponseEntity.badRequest().build();
                }
            }
            
            // Actualizar campos
            alumno.setNie(alumnoActualizado.getNie());
            alumno.setNombre_alumno(alumnoActualizado.getNombre_alumno());
            alumno.setApellido_alumno(alumnoActualizado.getApellido_alumno());
            alumno.setCorreo_alumno(alumnoActualizado.getCorreo_alumno());
            alumno.setFecha_nacimiento_alumno(alumnoActualizado.getFecha_nacimiento_alumno());
            alumno.setDireccion_a(alumnoActualizado.getDireccion_a());
            alumno.setSexo_a(alumnoActualizado.getSexo_a());
            alumno.setTelefono_alumno(alumnoActualizado.getTelefono_alumno());
            alumno.setEnfermedades(alumnoActualizado.getEnfermedades());
            alumno.setMedicamento(alumnoActualizado.getMedicamento());
            alumno.setVive_con(alumnoActualizado.getVive_con());
            alumno.setParentezco_encargado(alumnoActualizado.getParentezco_encargado());
            alumno.setTelefono_encargado(alumnoActualizado.getTelefono_encargado());
            alumno.setCorreo_encargado(alumnoActualizado.getCorreo_encargado());
            alumno.setDui_encargado(alumnoActualizado.getDui_encargado());
            alumno.setLugar_de_trabajo(alumnoActualizado.getLugar_de_trabajo());
            alumno.setEstado_alumno(alumnoActualizado.isEstado_alumno());
            
            // Validar y actualizar grado
            if (alumnoActualizado.getGrado() != null) {
                alumno.setGrado(alumnoActualizado.getGrado());
            }
            
            Alumno alumnoGuardado = repositorio.save(alumno);
            return ResponseEntity.ok(alumnoGuardado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar alumno (cambiar estado)
    @DeleteMapping("/alumnos/{id}")
    public ResponseEntity<Void> eliminarAlumno(@PathVariable int id) {
        Optional<Alumno> alumno = repositorio.findById(id);
        
        if (alumno.isPresent()) {
            Alumno a = alumno.get();
            a.setEstado_alumno(false);
            repositorio.save(a);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Activar/Desactivar alumno
    @PutMapping("/alumnos/{id}/estado")
    public ResponseEntity<Alumno> cambiarEstadoAlumno(@PathVariable int id) {
        Optional<Alumno> alumno = repositorio.findById(id);
        
        if (alumno.isPresent()) {
            Alumno a = alumno.get();
            a.setEstado_alumno(!a.isEstado_alumno());
            Alumno alumnoGuardado = repositorio.save(a);
            return ResponseEntity.ok(alumnoGuardado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Listar solo alumnos activos
    @GetMapping("/alumnos/activos")
    public List<Alumno> listarAlumnosActivos() {
        return repositorio.buscarAlumnosActivos();
    }

}