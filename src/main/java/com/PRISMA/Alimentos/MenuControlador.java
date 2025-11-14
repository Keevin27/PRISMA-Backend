package com.PRISMA.Alimentos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.Docente.Excepciones.ResourceNotFoundException;
import com.PRISMA.Entity.Dia;
import com.PRISMA.Entity.Docente;
import com.PRISMA.Entity.Menu;
import com.PRISMA.Entity.Semana;
import com.PRISMA.SemanaDia.DiaRepositorio;
import com.PRISMA.SemanaDia.SemanaRepositorio;

@RestController
@RequestMapping("/Menu/")
@CrossOrigin(origins = "http://localhost:4200/")
public class MenuControlador {

    @Autowired
    private MenuRepositorio repositorioMenu;
    @Autowired
    private SemanaRepositorio semanaRepository;
    @Autowired
    private DiaRepositorio diaRepository;

    @GetMapping("/")
    public List<Menu> listarMenus() {
        return repositorioMenu.findAll();
    }

    @GetMapping("/activos")
    public List<Menu> listarMenusActivos() {
        return repositorioMenu.listarMenusActivos(true);
    }

    @PostMapping("/")
    public ResponseEntity<Menu> guardarMenu(@RequestBody Menu menu) {

        Semana semana = semanaRepository.findById(menu.getSemana().getId_semana())
                .orElseThrow(() -> new RuntimeException("Semana no encontrada"));
        Dia dia = diaRepository.findById(menu.getDia().getId_dia())
                .orElseThrow(() -> new RuntimeException("Día no encontrado"));

        menu.setSemana(semana);
        menu.setDia(dia);

        Menu nuevo = repositorioMenu.save(menu);
        return ResponseEntity.ok(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Menu> actualizarMenu(@PathVariable int id, @RequestBody Menu menu) {
        Menu menuExistente = repositorioMenu.findById(id).orElseThrow(() -> new RuntimeException("No se encontro"));

        menuExistente.setDia(menu.getDia());
        menuExistente.setSemana(menu.getSemana());
        menuExistente.setEstado_menu(menu.isEstado_menu());
        menuExistente.setNombre_menu(menu.getNombre_menu());
        Menu menuActualizado = repositorioMenu.save(menuExistente);
        return ResponseEntity.ok(menuActualizado);
    }
    
    @GetMapping("/menu/{id_menu}")
    public ResponseEntity<Menu> obtenerMenu(@PathVariable("id_menu") int id) {
        Menu menu = repositorioMenu.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No existe el menu con ID: " + id));
        return ResponseEntity.ok(menu);
    } 
}
