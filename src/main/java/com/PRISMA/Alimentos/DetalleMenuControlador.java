package com.PRISMA.Alimentos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.Entity.DetalleMenu;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/DetalleMenu/")
@CrossOrigin(origins = "http://localhost:4200/")
public class DetalleMenuControlador {

    @Autowired
    private DetalleMenuRepositorio repositorioDetalleMenu;

    @GetMapping("/")
    public List<DetalleMenu> listarDetallesMenu() {
        return repositorioDetalleMenu.findAll();
    }

    @GetMapping("/menu/{id_menu}")
    public List<DetalleMenu> listarDetallesPorMenu(@PathVariable int id_menu) {
        return repositorioDetalleMenu.findbyIdMenu(id_menu);
    }

    @PostMapping("/detallemenu")
    public DetalleMenu guardarDetalleMenu(@RequestBody DetalleMenu detalleMenu) {
        return repositorioDetalleMenu.save(detalleMenu);
    }

    @PutMapping("/detallemenu/{id}")
    public ResponseEntity<DetalleMenu> actualizarDetalleMenu(@PathVariable int id,
            @RequestBody DetalleMenu detalleMenu) {
        DetalleMenu detalleMenuExistente = repositorioDetalleMenu.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontro"));

        detalleMenuExistente.setRacion_gramos(detalleMenu.getRacion_gramos());
        detalleMenuExistente.setAlimento(detalleMenu.getAlimento());
        detalleMenuExistente.setMenu(detalleMenu.getMenu());
        DetalleMenu detalleMenuActualizado = repositorioDetalleMenu.save(detalleMenuExistente);
        return ResponseEntity.ok(detalleMenuActualizado);
    }

    @DeleteMapping("/detallemenu/{id}")
    public ResponseEntity<Void> eliminarDetalleMenu(@PathVariable int id) {
        DetalleMenu detalleMenu = repositorioDetalleMenu.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));

        repositorioDetalleMenu.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
