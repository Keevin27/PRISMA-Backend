package com.PRISMA.Alumno.Matricula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.PRISMA.Alumno.Grado.GradoRepositorio;
import com.PRISMA.Entity.Alumno;
import com.PRISMA.Entity.Grado;
import com.PRISMA.Entity.Matricula;

@RestController
@RequestMapping("/Matricula/")
@CrossOrigin(origins = "http://localhost:4200/")
public class MatriculaControlador {

    @Autowired
    private MatriculaRepositorio repositorioMatricula;

    @Autowired
    private AlumnoRepositorio alumnoRepositorio;

    @Autowired
    private GradoRepositorio gradoRepositorio;

    // ==================== ENDPOINTS EXISTENTES ====================

    // Listar todas las matriculas
    @GetMapping("/")
    public List<Matricula> listarMatriculas() {
        return repositorioMatricula.findAll();
    }

    // Matriculas por anio
    @GetMapping("/{anio}")
    public List<Matricula> listarMatriculasPorAnio(@PathVariable int anio) {
        return repositorioMatricula.buscarPorAnio(anio);
    }

    // Matriculas por grado
    @GetMapping("/{anio}/{id_grado}")
    public List<Matricula> listarMatriculasPorAnioGrado(@PathVariable int id_grado) {
        return repositorioMatricula.buscarPorGrado(id_grado);
    }

    // Matriculas por grado (nueva ruta más clara)
    @GetMapping("/grado/{idGrado}")
    public List<Matricula> listarMatriculasPorGrado(@PathVariable int idGrado) {
        return repositorioMatricula.buscarPorGrado(idGrado);
    }

    // Buscar matricula por id de alumno
    @GetMapping("/buscarAlumno/{idAlumno}")
    public ResponseEntity<Matricula> obtenerAlumnoPorId(@PathVariable int idAlumno) {
        Optional<Matricula> matricula = repositorioMatricula.buscarMatriculaPorIdAlumno(idAlumno);
        if (matricula.isPresent()) {
            return ResponseEntity.ok(matricula.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear matrícula
    @PostMapping("/crear")
    public ResponseEntity<?> crearMatricula(@RequestBody Map<String, Object> payload) {
        Map<String, String> respuesta = new HashMap<>();

        try {
            Integer idAlumno = (Integer) payload.get("idAlumno");
            Integer idGrado = (Integer) payload.get("idGrado");

            if (idAlumno == null || idGrado == null) {
                respuesta.put("error", "Debe proporcionar el ID del alumno y del grado");
                return ResponseEntity.badRequest().body(respuesta);
            }
            Optional<Alumno> alumnoOpt = alumnoRepositorio.findById(idAlumno);
            if (!alumnoOpt.isPresent()) {
                respuesta.put("error", "El alumno no existe");
                return ResponseEntity.badRequest().body(respuesta);
            }
            Optional<Grado> gradoOpt = gradoRepositorio.findById(idGrado);
            if (!gradoOpt.isPresent()) {
                respuesta.put("error", "El grado no existe");
                return ResponseEntity.badRequest().body(respuesta);
            }

            Alumno alumno = alumnoOpt.get();
            Grado grado = gradoOpt.get();

            Optional<Matricula> matriculaExistente = repositorioMatricula.buscarMatriculaPorIdAlumno(idAlumno);
            if (matriculaExistente.isPresent()) {
                Matricula matExist = matriculaExistente.get();
                if (matExist.getGrado().getAnioAcademico().getAnio() == grado.getAnioAcademico().getAnio()) {
                    respuesta.put("error", "El alumno ya está matriculado en este año académico");
                    return ResponseEntity.badRequest().body(respuesta);
                }
            }
            Long cantidadAlumnos = repositorioMatricula.contarAlumnosPorGrado(idGrado);
            if (cantidadAlumnos >= 45) {
                respuesta.put("error", "El grado ha alcanzado su cupo máximo (45 alumnos)");
                return ResponseEntity.badRequest().body(respuesta);
            }

            Matricula nuevaMatricula = new Matricula();
            nuevaMatricula.setAlumno(alumno);
            nuevaMatricula.setGrado(grado);
            nuevaMatricula.setEstadoMatricula("Matriculado");

            Matricula matriculaGuardada = repositorioMatricula.save(nuevaMatricula);

            respuesta.put("mensaje", "Matrícula creada exitosamente");
            return ResponseEntity.ok(matriculaGuardada);

        } catch (Exception e) {
            respuesta.put("error", "Error al crear la matrícula: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    // Verificar si alumno ya está matriculado en un año
    @GetMapping("/verificar/{idAlumno}/{anio}")
    public ResponseEntity<Boolean> verificarMatriculaExistente(
            @PathVariable Integer idAlumno,
            @PathVariable Integer anio) {

        Optional<Matricula> matricula = repositorioMatricula.buscarMatriculaPorIdAlumno(idAlumno);

        if (matricula.isPresent()) {
            int anioMatricula = matricula.get().getGrado().getAnioAcademico().getAnio();
            return ResponseEntity.ok(anioMatricula == anio);
        }
        return ResponseEntity.ok(false);
    }

    // Contar alumnos en un grado
    @GetMapping("/contar/{idGrado}")
    public ResponseEntity<Long> contarAlumnosPorGrado(@PathVariable Integer idGrado) {
        Long cantidad = repositorioMatricula.contarAlumnosPorGrado(idGrado);
        return ResponseEntity.ok(cantidad);
    }

    // Eliminar matricula
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarMatricula(@PathVariable int id) {
        Optional<Matricula> matricula = repositorioMatricula.findById(id);

        if (matricula.isPresent()) {
            try {
                repositorioMatricula.deleteById(id);
                return ResponseEntity.ok("matricula eliminadas exitosamente");
            } catch (Exception e) {
                return ResponseEntity.badRequest()
                        .body("Error al eliminar las matriculas: " + e.getMessage());
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar matricula existente
    @PutMapping("/{id}")
    public ResponseEntity<Matricula> actualizarMatricula(@PathVariable int id,
            @RequestBody Matricula matriculaActualizada) {
        Optional<Matricula> matriculaExistente = repositorioMatricula.findById(id);

        if (matriculaExistente.isPresent()) {
            Matricula matricula = matriculaExistente.get();
            matricula.setEstadoMatricula("Matriculado");
            matricula.setGrado(matriculaActualizada.getGrado());

            Matricula matriculaGuardada = repositorioMatricula.save(matricula);
            return ResponseEntity.ok(matriculaGuardada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ==================== LÓGICA PRINCIPAL DEL MÓDULO ====================

    /**
     * Obtener alumnos matriculados y NO matriculados (candidatos) para un grado específico.
     */
    @GetMapping("/alumnos-por-grado")
    public ResponseEntity<?> obtenerAlumnosPorGrado(
            @RequestParam Integer idGrado,
            @RequestParam Integer anio) {
        
        try {
            Optional<Grado> gradoOpt = gradoRepositorio.findById(idGrado);
            if (!gradoOpt.isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El grado no existe"));
            }

            // 1. Obtener ALUMNOS MATRICULADOS (Solo los de ESE grado)
            List<Matricula> matriculados = repositorioMatricula.buscarPorGrado(idGrado);

            // 2. Obtener ALUMNOS NO MATRICULADOS (Candidatos)
            // 2a. Obtener TODOS los alumnos ya matriculados en CUALQUIER grado de ESTE AÑO
            List<Matricula> matriculasDelAnio = repositorioMatricula.buscarPorAnio(anio);
            
            // 2b. Crear un Set (lista rápida) de los IDs de alumnos que ya tienen matrícula este año
            Set<Integer> idsAlumnosYaMatriculadosEnElAnio = matriculasDelAnio.stream()
                .map(m -> m.getAlumno().getIdAlumno())
                .collect(Collectors.toSet());

            // 2c. Obtener TODOS los alumnos activos del sistema
            List<Alumno> todosAlumnosActivos = alumnoRepositorio.buscarAlumnosActivos();
            
            // 2d. Filtrar: un "candidato" es un alumno activo que NO esté en la lista de matriculados del año
            List<Matricula> noMatriculados = new ArrayList<>();
            for (Alumno alumno : todosAlumnosActivos) {
                if (!idsAlumnosYaMatriculadosEnElAnio.contains(alumno.getIdAlumno())) {
                    // Este alumno está disponible (es nuevo o sin matrícula este año)
                    // Creamos una "Matricula Falsa" para que el frontend la entienda
                    Matricula matriculaCandidato = new Matricula();
                    matriculaCandidato.setAlumno(alumno);
                    matriculaCandidato.setGrado(null); // No tiene grado
                    matriculaCandidato.setEstadoMatricula("Disponible");
                    noMatriculados.add(matriculaCandidato);
                }
            }
            
            // 3. Calcular cupo
            long totalMatriculados = matriculados.size();
            long cupoDisponible = 45 - totalMatriculados;

            // 4. Preparar respuesta
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("matriculados", matriculados);
            respuesta.put("noMatriculados", noMatriculados);
            respuesta.put("totalMatriculados", totalMatriculados);
            respuesta.put("totalNoMatriculados", noMatriculados.size());
            respuesta.put("cupoDisponible", cupoDisponible);

            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            e.printStackTrace(); // Importante para ver errores en consola
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener alumnos: " + e.getMessage());
        }
    }

    /**
     * Matricular múltiples alumnos a la vez
     */
    @PostMapping("/matricular-multiples")
    public ResponseEntity<?> matricularMultiplesAlumnos(@RequestBody Map<String, Object> payload) {
        Map<String, Object> respuesta = new HashMap<>();
        
        try {
            Integer idGrado = (Integer) payload.get("idGrado");
            @SuppressWarnings("unchecked")
            List<Integer> idsAlumnos = (List<Integer>) payload.get("idsAlumnos");

            if (idGrado == null || idsAlumnos == null || idsAlumnos.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Debe proporcionar el ID del grado y la lista de alumnos"));
            }

            Optional<Grado> gradoOpt = gradoRepositorio.findById(idGrado);
            if (!gradoOpt.isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El grado no existe"));
            }

            Grado grado = gradoOpt.get();
            int anioAcademico = grado.getAnioAcademico().getAnio();

            // Verificar cupo
            Long alumnosActuales = repositorioMatricula.contarAlumnosPorGrado(idGrado);
            long cupoDisponible = 45 - alumnosActuales;
            
            if (idsAlumnos.size() > cupoDisponible) {
                return ResponseEntity.badRequest().body(Map.of("error", "No hay suficiente cupo. Disponible: " + cupoDisponible));
            }

            List<Matricula> matriculasCreadas = new java.util.ArrayList<>();
            List<String> errores = new java.util.ArrayList<>();
            int exitosas = 0;

            // Obtener todas las matrículas de ESE año UNA SOLA VEZ
            List<Matricula> matriculasDelAnio = repositorioMatricula.buscarPorAnio(anioAcademico);

            for (Integer idAlumno : idsAlumnos) {
                try {
                    Optional<Alumno> alumnoOpt = alumnoRepositorio.findById(idAlumno);
                    if (!alumnoOpt.isPresent()) {
                        errores.add("Alumno ID " + idAlumno + " no existe");
                        continue;
                    }
                    Alumno alumno = alumnoOpt.get();

                    // Buscar si el alumno ya tiene una matrícula (en CUALQUIER grado) en ESTE año
                    Optional<Matricula> matriculaDelAnio = matriculasDelAnio.stream()
                        .filter(m -> m.getAlumno().getIdAlumno() == idAlumno)
                        .findFirst();

                    if (matriculaDelAnio.isPresent()) {
                        Matricula matExist = matriculaDelAnio.get();
                        
                        // Caso 1: Ya está matriculado en un grado este año
                        if (matExist.getGrado() != null) {
                            errores.add("Alumno " + alumno.getNombre_alumno() + " ya está matriculado en " + matExist.getGrado().getNombre_grado() + " este año");
                            continue;
                        }
                        
                        // Caso 2: Es "Rocío" (grado es null PARA ESTE AÑO)
                        // Actualizamos este registro
                        matExist.setGrado(grado);
                        matExist.setEstadoMatricula("Matriculado");
                        Matricula matriculaGuardada = repositorioMatricula.save(matExist);
                        matriculasCreadas.add(matriculaGuardada);
                        exitosas++;

                    } else {
                        // Caso 3: No tiene NINGÚN registro este año (nuevo, o promovido)
                        // Creamos un registro nuevo
                        Matricula nuevaMatricula = new Matricula();
                        nuevaMatricula.setAlumno(alumno);
                        nuevaMatricula.setGrado(grado);
                        nuevaMatricula.setEstadoMatricula("Matriculado");
                        
                        Matricula matriculaGuardada = repositorioMatricula.save(nuevaMatricula);
                        matriculasCreadas.add(matriculaGuardada);
                        exitosas++;
                    }

                } catch (Exception e) {
                    errores.add("Error al matricular alumno ID " + idAlumno + ": " + e.getMessage());
                }
            }

            respuesta.put("mensaje", "Proceso completado");
            respuesta.put("matriculasCreadas", exitosas);
            respuesta.put("totalProcesados", idsAlumnos.size());
            if (!errores.isEmpty()) respuesta.put("errores", errores);
            
            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error al matricular alumnos: " + e.getMessage()));
        }
    }

    /**
     * Desmatricular múltiples alumnos (de un grado específico)
     */
    @DeleteMapping("/desmatricular-multiples")
    public ResponseEntity<?> desmatricularMultiplesAlumnos(@RequestBody Map<String, Object> payload) {
        Map<String, Object> respuesta = new HashMap<>();
        
        try {
            @SuppressWarnings("unchecked")
            List<Integer> idsAlumnos = (List<Integer>) payload.get("idsAlumnos");
            Integer idGrado = (Integer) payload.get("idGrado"); // Leemos el idGrado

            if (idsAlumnos == null || idsAlumnos.isEmpty() || idGrado == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Debe proporcionar la lista de IDs de alumnos y el idGrado"));
            }

            List<String> errores = new java.util.ArrayList<>();
            int exitosas = 0;

            // Obtenemos las matrículas de ESE grado
            List<Matricula> matriculasDelGrado = repositorioMatricula.buscarPorGrado(idGrado);

            for (Integer idAlumno : idsAlumnos) {
                try {
                    // Buscamos al alumno DENTRO de las matrículas de ESE grado
                    Optional<Matricula> matriculaOpt = matriculasDelGrado.stream()
                        .filter(m -> m.getAlumno().getIdAlumno() == idAlumno)
                        .findFirst();
                    
                    if (matriculaOpt.isPresent()) {
                        // Lógica "Rocío": Desmatricular = quitar el grado
                        Matricula matricula = matriculaOpt.get();
                        matricula.setGrado(null);
                        matricula.setEstadoMatricula("Disponible");
                        repositorioMatricula.save(matricula);
                        exitosas++;
                    } else {
                        // Si intentan desmatricular a alguien que no está en este grado
                        errores.add("Alumno ID " + idAlumno + " no tiene matrícula activa en este grado");
                    }

                } catch (Exception e) {
                    errores.add("Error al desmatricular alumno ID " + idAlumno + ": " + e.getMessage());
                }
            }

            respuesta.put("mensaje", "Proceso completado");
            respuesta.put("desmatriculasExitosas", exitosas);
            respuesta.put("totalProcesados", idsAlumnos.size());
            if (!errores.isEmpty()) respuesta.put("errores", errores);

            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error al desmatricular alumnos: " + e.getMessage()));
        }
    }
}