package com.PRISMA.AnioAcademico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.Entity.AnioAcademico;

@RestController
@RequestMapping("/AnioAcademico/")
@CrossOrigin(origins = "http://localhost:4200/")
public class AnioAcademicoControlador {

    @Autowired
    private AnioAcademicoRepositorio repositorioAnio;

    //Listar anios academicos registrados
    @GetMapping("/")
    public List<AnioAcademico> listarAniosAcademicos() {
        return repositorioAnio.findAll();
    }
}
