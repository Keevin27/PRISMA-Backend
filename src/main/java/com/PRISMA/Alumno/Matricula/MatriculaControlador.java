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

    /**
     * Lista todas las matrículas existentes.
     */
    @GetMapping("/")
    public List<Matricula> listarMatriculas() {
        return repositorioMatricula.findAll();
    }

    /**
     * Lista matrículas según año académico.
     */
    @GetMapping("/{anio}")
    public List<Matricula> listarMatriculasPorAnio(@PathVariable int anio) {
        return repositorioMatricula.buscarPorAnio(anio);
    }

    /**
     * Lista matrículas según año y grado (aunque sólo usa id_grado).
     */
    @GetMapping("/{anio}/{id_grado}")
    public List<Matricula> listarMatriculasPorAnioGrado(@PathVariable int id_grado) {
        return repositorioMatricula.buscarPorGrado(id_grado);
    }

    /**
     * Lista matrículas por grado específico.
     */
    @GetMapping("/grado/{idGrado}")
    public List<Matricula> listarMatriculasPorGrado(@PathVariable int idGrado) {
        return repositorioMatricula.buscarPorGrado(idGrado);
    }

    /**
     * Devuelve la matrícula más reciente del alumno.
     * Maneja casos donde el alumno tiene varias matrículas.
     */
    @GetMapping("/buscarAlumno/{idAlumno}")
    public ResponseEntity<Matricula> obtenerAlumnoPorId(@PathVariable int idAlumno) {

        // Obtiene todas las matrículas del alumno
        List<Matricula> matriculas = repositorioMatricula.buscarMatriculasPorIdAlumno(idAlumno); 
        
        if (matriculas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Ordena por año descendente y por id de matrícula
        matriculas.sort((m1, m2) -> {
            if (m1.getGrado() == null && m2.getGrado() != null) return 1;
            if (m1.getGrado() != null && m2.getGrado() == null) return -1;

            if (m1.getGrado() == null && m2.getGrado() == null) return 0;

            int anioCompare = Integer.compare(
                m2.getGrado().getAnioAcademico().getAnio(),
                m1.getGrado().getAnioAcademico().getAnio()
            );

            if (anioCompare != 0) return anioCompare;

            return Integer.compare(m2.getIdMatricula(), m1.getIdMatricula());
        });

        return ResponseEntity.ok(matriculas.get(0));
    }

    /**
     * Crea una nueva matrícula.
     * Lógica corregida para evitar NonUniqueResultException.
     */
    @PostMapping("/crear")
    public ResponseEntity<?> crearMatricula(@RequestBody Map<String, Object> payload) {
        Map<String, String> respuesta = new HashMap<>();

        try {
            Integer idAlumno = (Integer) payload.get("idAlumno");
            Integer idGrado = (Integer) payload.get("idGrado");

            // Validación básica
            if (idAlumno == null || idGrado == null) {
                respuesta.put("error", "Debe proporcionar el ID del alumno y del grado");
                return ResponseEntity.badRequest().body(respuesta);
            }

            // Validar existencia del alumno
            Optional<Alumno> alumnoOpt = alumnoRepositorio.findById(idAlumno);
            if (!alumnoOpt.isPresent()) {
                respuesta.put("error", "El alumno no existe");
                return ResponseEntity.badRequest().body(respuesta);
            }

            // Validar existencia del grado
            Optional<Grado> gradoOpt = gradoRepositorio.findById(idGrado);
            if (!gradoOpt.isPresent()) {
                respuesta.put("error", "El grado no existe");
                return ResponseEntity.badRequest().body(respuesta);
            }

            Alumno alumno = alumnoOpt.get();
            Grado grado = gradoOpt.get();
            int anioAcademicoNuevo = grado.getAnioAcademico().getAnio();

            // Obtener todas las matrículas del año
            List<Matricula> matriculasDelAnio = repositorioMatricula.buscarPorAnio(anioAcademicoNuevo);

            // Buscar matrícula del alumno en el mismo año
            Optional<Matricula> matriculaDelAnio = matriculasDelAnio.stream()
                .filter(m -> m.getAlumno().getIdAlumno() == idAlumno)
                .findFirst();
            
            // Si ya tiene matrícula en el año
            if (matriculaDelAnio.isPresent()) {
                Matricula matExist = matriculaDelAnio.get();

                // Caso: ya asignado a un grado
                if (matExist.getGrado() != null) {
                    respuesta.put("error", "El alumno ya está matriculado en " + matExist.getGrado().getNombre_grado() + " este año");
                    return ResponseEntity.badRequest().body(respuesta);
                }

                // Caso: matrícula del año sin grado (Caso Rocío)
                Long cantidadAlumnos = repositorioMatricula.contarAlumnosPorGrado(idGrado);
                if (cantidadAlumnos >= 45) {
                    respuesta.put("error", "El grado ha alcanzado su cupo máximo (45 alumnos)");
                    return ResponseEntity.badRequest().body(respuesta);
                }

                matExist.setGrado(grado);
                matExist.setEstadoMatricula("Matriculado");
                Matricula matriculaActualizada = repositorioMatricula.save(matExist);
                
                respuesta.put("mensaje", "Matrícula actualizada exitosamente");
                return ResponseEntity.ok(matriculaActualizada);

            } else {
                // Crear matrícula nueva

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
            }

        } catch (Exception e) {
            respuesta.put("error", "Error al crear la matrícula: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    /**
     * Verifica si existe matrícula válida (con grado asignado) en un año específico.
     */
    @GetMapping("/verificar/{idAlumno}/{anio}")
    public ResponseEntity<Boolean> verificarMatriculaExistente(
            @PathVariable Integer idAlumno,
            @PathVariable Integer anio) {

        List<Matricula> matriculasDelAnio = repositorioMatricula.buscarPorAnio(anio);

        Optional<Matricula> matriculaDelAnio = matriculasDelAnio.stream()
            .filter(m -> m.getAlumno().getIdAlumno() == idAlumno)
            .findFirst();

        if (matriculaDelAnio.isPresent()) {
            return ResponseEntity.ok(matriculaDelAnio.get().getGrado() != null);
        }

        return ResponseEntity.ok(false);
    }

    /**
     * Devuelve el total de alumnos matriculados en un grado.
     */
    @GetMapping("/contar/{idGrado}")
    public ResponseEntity<Long> contarAlumnosPorGrado(@PathVariable Integer idGrado) {
        Long cantidad = repositorioMatricula.contarAlumnosPorGrado(idGrado);
        return ResponseEntity.ok(cantidad);
    }

    /**
     * Elimina una matrícula por ID.
     */
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

    /**
     * Actualiza una matrícula existente.
     */
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

    // =======================================================
    //    MÉTODOS PARA PANTALLA "GESTIÓN DE MATRÍCULAS"
    // =======================================================

    /**
     * Devuelve alumnos matriculados y no matriculados en un grado para un año dado.
     * Se usa para cargar la tabla de gestión de matrículas.
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

            List<Matricula> matriculados = repositorioMatricula.buscarPorGrado(idGrado);

            List<Matricula> matriculasDelAnio = repositorioMatricula.buscarPorAnio(anio);

            Set<Integer> idsAlumnosYaMatriculadosEnElAnio = matriculasDelAnio.stream()
                .map(m -> m.getAlumno().getIdAlumno())
                .collect(Collectors.toSet());

            List<Alumno> todosAlumnosActivos = alumnoRepositorio.buscarAlumnosActivos();
            
            List<Matricula> noMatriculados = new ArrayList<>();
            for (Alumno alumno : todosAlumnosActivos) {
                if (!idsAlumnosYaMatriculadosEnElAnio.contains(alumno.getIdAlumno())) {
                    Matricula matriculaCandidato = new Matricula();
                    matriculaCandidato.setAlumno(alumno);
                    matriculaCandidato.setGrado(null);
                    matriculaCandidato.setEstadoMatricula("Disponible");
                    noMatriculados.add(matriculaCandidato);
                }
            }
            
            long totalMatriculados = matriculados.size();
            long cupoDisponible = 45 - totalMatriculados;

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("matriculados", matriculados);
            respuesta.put("noMatriculados", noMatriculados);
            respuesta.put("totalMatriculados", totalMatriculados);
            respuesta.put("totalNoMatriculados", noMatriculados.size());
            respuesta.put("cupoDisponible", cupoDisponible);

            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener alumnos: " + e.getMessage());
        }
    }

    /**
     * Matricula múltiples alumnos en un solo proceso.
     * Valida cupos, existencia de matriculas del año, y matriculación previa.
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

            Long alumnosActuales = repositorioMatricula.contarAlumnosPorGrado(idGrado);
            long cupoDisponible = 45 - alumnosActuales;
            
            if (idsAlumnos.size() > cupoDisponible) {
                return ResponseEntity.badRequest().body(Map.of("error", "No hay suficiente cupo. Disponible: " + cupoDisponible));
            }

            List<Matricula> matriculasCreadas = new java.util.ArrayList<>();
            List<String> errores = new java.util.ArrayList<>();
            int exitosas = 0;

            List<Matricula> matriculasDelAnio = repositorioMatricula.buscarPorAnio(anioAcademico);

            for (Integer idAlumno : idsAlumnos) {
                try {
                    Optional<Alumno> alumnoOpt = alumnoRepositorio.findById(idAlumno);
                    if (!alumnoOpt.isPresent()) {
                        errores.add("Alumno ID " + idAlumno + " no existe");
                        continue;
                    }
                    Alumno alumno = alumnoOpt.get();

                    Optional<Matricula> matriculaDelAnio = matriculasDelAnio.stream()
                        .filter(m -> m.getAlumno().getIdAlumno() == idAlumno)
                        .findFirst();

                    if (matriculaDelAnio.isPresent()) {
                        Matricula matExist = matriculaDelAnio.get();
                        
                        if (matExist.getGrado() != null) {
                            errores.add("Alumno " + alumno.getNombre_alumno() + " ya está matriculado en " + matExist.getGrado().getNombre_grado() + " este año");
                            continue;
                        }
                        
                        matExist.setGrado(grado);
                        matExist.setEstadoMatricula("Matriculado");
                        Matricula matriculaGuardada = repositorioMatricula.save(matExist);
                        matriculasCreadas.add(matriculaGuardada);
                        exitosas++;

                    } else {
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
     * Desmatricula múltiples alumnos de un grado.
     */
    @DeleteMapping("/desmatricular-multiples")
    public ResponseEntity<?> desmatricularMultiplesAlumnos(@RequestBody Map<String, Object> payload) {
        Map<String, Object> respuesta = new HashMap<>();
        
        try {
            @SuppressWarnings("unchecked")
            List<Integer> idsAlumnos = (List<Integer>) payload.get("idsAlumnos");
            Integer idGrado = (Integer) payload.get("idGrado"); 

            if (idsAlumnos == null || idsAlumnos.isEmpty() || idGrado == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Debe proporcionar la lista de IDs de alumnos y el idGrado"));
            }

            List<String> errores = new java.util.ArrayList<>();
            int exitosas = 0;

            List<Matricula> matriculasDelGrado = repositorioMatricula.buscarPorGrado(idGrado);

            for (Integer idAlumno : idsAlumnos) {
                try {
                    Optional<Matricula> matriculaOpt = matriculasDelGrado.stream()
                        .filter(m -> m.getAlumno().getIdAlumno() == idAlumno)
                        .findFirst();
                    
                    if (matriculaOpt.isPresent()) {
                        Matricula matricula = matriculaOpt.get();
                        matricula.setGrado(null);
                        matricula.setEstadoMatricula("Disponible");
                        repositorioMatricula.save(matricula);
                        exitosas++;
                    } else {
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
