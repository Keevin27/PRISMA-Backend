package com.PRISMA.PaqueteEscolar;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.Entity.PaqueteEscolar;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/PaqEsc/")
public class PaqueteEscolarControlador {

    @Autowired
    private PaqueteEscolarRepositorio repositorio;
    

    @GetMapping("/paquetes-escolares")
    public List<PaqueteEscolar> listarToddosLosPaquetesEscolares(){
        return repositorio.findAll();
    }
    @GetMapping("/paquetes-escolares-activos")
    public List<PaqueteEscolar> listarToddosLosPaquetesEscolaresActivos(){
        return repositorio.paquetesActivos();
    }

    @PreAuthorize("hasRole('DIRECTOR')")
    @PostMapping("/paquetes-escolares")
    public PaqueteEscolar guardarPaquete(@RequestBody PaqueteEscolar paqueteEscolar) {
        
        return repositorio.save(paqueteEscolar);
    }


    @PutMapping("/paquetes-escolares/{id}")
    public ResponseEntity<PaqueteEscolar> actualizarPaquete(@PathVariable int id, @RequestBody PaqueteEscolar paqueteEscolar) {
        PaqueteEscolar paqueteescolar = repositorio.findById(id).orElseThrow(()-> new RuntimeException("No se encontro"));
        
        paqueteescolar.setNombre_paquete(paqueteEscolar.getNombre_paquete());
        paqueteescolar.setPaquete_activo(paqueteEscolar.isPaquete_activo());
        PaqueteEscolar paqueteActualizado = repositorio.save(paqueteescolar);
        return ResponseEntity.ok(paqueteActualizado);
    }
    
}
