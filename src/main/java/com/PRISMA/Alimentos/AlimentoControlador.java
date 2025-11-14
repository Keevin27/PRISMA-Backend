package com.PRISMA.Alimentos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.Entity.Alimento;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/Alimento/")
@CrossOrigin(origins = "http://localhost:4200/")
public class AlimentoControlador {

    @Autowired
    private AlimentoRepositorio repositorioAlimento;

    //Listar todos los alimentos
    @GetMapping("/")
    public List<Alimento> listarAlimentos() {
        return repositorioAlimento.findAll();
    }

    //Listar todos los alimentos activos
    @GetMapping("/activos")
    public List<Alimento> listarAlimentosActivos() {
        return repositorioAlimento.alimentosActivos();
    }

    //Guardar un alimento
    @PostMapping("/alimento")
    public Alimento guardarAlimento(@RequestBody Alimento alimento) {
        
        return repositorioAlimento.save(alimento);
    }

    //Actualiza un alimento
    @PutMapping("/alimento/{id}")
    public ResponseEntity<Alimento> actualizarAlimento(@PathVariable int id, @RequestBody Alimento alimento) {
        Alimento alimentoDos = repositorioAlimento.findById(id).orElseThrow(()-> new RuntimeException("No se encontro"));
        
        alimentoDos.setNombre_alimento(alimento.getNombre_alimento());
        alimentoDos.setEstado_alimento(alimento.isEstado_alimento());
        Alimento alimentoActualizado = repositorioAlimento.save(alimentoDos);
        return ResponseEntity.ok(alimentoActualizado);
    }
    
}
