package com.PRISMA.Horario;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.Bloque.BloqueRepositorio;
import com.PRISMA.Entity.AsignacionHorario;
import com.PRISMA.Entity.Bloque;
import com.PRISMA.Entity.Horario;
import com.PRISMA.Entity.Materia;
import com.PRISMA.SemanaDia.DiaRepositorio;

@RestController
@RequestMapping("/Horarios/")
@CrossOrigin(origins="http://localhost:4200/")
public class HorarioControlador {

    @Autowired private BloqueRepositorio bloqueRepo;
    @Autowired private HorarioRepositorio horarioRepo;
    @Autowired private HoraRepositorio horaRepo;
    @Autowired private DiaRepositorio diaRepo;
    @Autowired private AsignarHorarioRepositorio asignacionRepo;

    //🔹 1. Generar horarios automáticamente
        @PostMapping("/generar")
        public String generarHorariosAutomaticos() {
            List<Bloque> bloques = bloqueRepo.findAll();
            List<Horario> horarios = horarioRepo.findAll();

            if (horarios.isEmpty()) {
                return "Debe registrar los horarios base (día y hora) antes de generar.";
            }   

            int horasPorSemanaDocente = 0;
            asignacionRepo.deleteAll(); // limpiar previos (opcional)

            Random random = new Random();

            for (Bloque bloque : bloques) {
                Materia materia = bloque.getMateria();
                String tipo = materia.getTipo_materia();

                int horasAAsignar = switch (tipo.toLowerCase()) {
                    case "complementaria" -> 1;
                    case "secundaria" -> 2;
                    default -> 4; // basicas
                };

                Set<Horario> usados = new HashSet<>();

                for (int i = 0; i < horasAAsignar; i++) {
                    Horario horarioLibre = horarios.get(random.nextInt(horarios.size()));

                
                int intentos = 0;
                    int maxIntentos = horarios.size() * 3;  // margen razonable

                    while (
                        intentos < maxIntentos &&
                        (usados.contains(horarioLibre)
                        || asignacionRepo.docenteOcupado(horarioLibre.getId_Horario(), bloque.getDocente().getDuiDocente())
                        || asignacionRepo.gradoOcupado(horarioLibre.getId_Horario(), Long.valueOf(bloque.getGrado().getId_grado())))
                    ) {
                        horarioLibre = horarios.get(random.nextInt(horarios.size()));
                        intentos++;
                    }

                    if (intentos >= maxIntentos) {
                        System.out.println("No se encontró horario libre para bloque ID: " + bloque.getId_bloque());
                        continue; // opcional: saltar y no asignar esta hora
                    }



                    usados.add(horarioLibre);

                    AsignacionHorario asignacion = new AsignacionHorario();
                    asignacion.setBloque(bloque);
                    asignacion.setHorario(horarioLibre);
                    asignacionRepo.save(asignacion);
                }
            }

            return "Horarios generados automáticamente.";
        }
        

        // 🔹 2. Obtener horarios por bloque
        @GetMapping("/bloque/{idBloque}")
        public List<AsignacionHorario> obtenerPorBloque(@PathVariable Long idBloque) {
            return asignacionRepo.findByBloqueId(idBloque);
        }

        // 🔹 3. Obtener todos
        @GetMapping
        public List<AsignacionHorario> listarTodos() {
            return asignacionRepo.findAll();
        }

        // 🔹 4. Eliminar horario asignado
        @DeleteMapping("/{id}")
        public void eliminar(@PathVariable Long id) {
            asignacionRepo.deleteById(id);
        }

        // --- Listar horarios por grado ---
        @GetMapping("/porGrado/{idGrado}")
        public List<AsignacionHorario> obtenerPorGrado(@PathVariable Long idGrado) {
            return asignacionRepo.findByGrado(idGrado);
        }

        // --- Listar horarios por docente ---
        @GetMapping("/porDocente/{duiDocente}")
        public List<AsignacionHorario> obtenerPorDocente(@PathVariable String duiDocente) {
            return asignacionRepo.findByDocente(duiDocente);
        }

}