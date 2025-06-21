package com.PRISMA.Seguridad;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.Entity.Rol;

@RestController
@RequestMapping("/api/roles")
public class RolController {
    @Autowired
    private RolRepositorio rolRepositorio;

    @GetMapping
    public List<Rol> listarRoles() {
        return rolRepositorio.findAll();
    }
}
