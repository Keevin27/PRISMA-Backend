package com.PRISMA.SemanaDia;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.Entity.Dia;

@RestController
@RequestMapping("/Dia/")
@CrossOrigin(origins = "http://localhost:4200/")
public class DiaControlador {

    @Autowired
    private DiaRepositorio repositorioDia;

    @GetMapping("/")
    public List<Dia> listarDias() {
        return repositorioDia.findAll();
    }
}
