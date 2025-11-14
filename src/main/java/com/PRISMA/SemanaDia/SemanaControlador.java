package com.PRISMA.SemanaDia;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.Entity.Semana;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/Semana/")
@CrossOrigin(origins = "http://localhost:4200/")
public class SemanaControlador {

    @Autowired
    private SemanaRepositorio repositorioSemana;

    @GetMapping("/")
    public List<Semana> listarSemanas() {
        return repositorioSemana.findAll();
    }
    
}
