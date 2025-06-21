package com.PRISMA.Seguridad.Usuario;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.PRISMA.Entity.Rol;
import com.PRISMA.Entity.Usuario;
import com.PRISMA.Seguridad.RolRepositorio;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolRepositorio rolRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.obtenerTodos();
    }

    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody Usuario usuario) {
        if (usuario.getRoles().isEmpty()) {
            throw new RuntimeException("Debe asignar al menos un rol");
        }

        Rol rol = usuario.getRoles().iterator().next();

        Rol rolExistente = rolRepository.findById(rol.getId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        usuario.setRoles(Set.of(rolExistente));
        usuario.setFechaRegistro(new Date());
        usuario.setUsuarioActivo(true);
        usuario.setPasswordUsuario(encoder.encode(usuario.getPasswordUsuario()));

        Usuario nuevo = usuarioService.guardar(usuario);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody Usuario datos) {
        Usuario usuario = usuarioService.obtenerPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        usuario.setCorreoUsuario(datos.getCorreoUsuario());
        usuario.setUsuarioActivo(datos.getUsuarioActivo());

        if (datos.getPasswordUsuario() != null && !datos.getPasswordUsuario().isEmpty()) {
            usuario.setPasswordUsuario(encoder.encode(datos.getPasswordUsuario()));
        }

        if (datos.getRoles().isEmpty()) {
            throw new RuntimeException("Debe asignar al menos un rol");
        }

        Rol rol = datos.getRoles().iterator().next();

        Rol rolExistente = rolRepository.findById(rol.getId())
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        usuario.setRoles(Set.of(rolExistente));

        return ResponseEntity.ok(usuarioService.guardar(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
