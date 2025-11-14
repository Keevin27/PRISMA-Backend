package com.PRISMA.Seguridad.Usuario;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.Entity.Rol;
import com.PRISMA.Entity.Usuario;
import com.PRISMA.Seguridad.RolRepositorio;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins ="http://localhost:4200/")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmailService emailService;

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

        usuario.setRoles(new HashSet<>(Collections.singletonList(rolExistente)));
        usuario.setFechaRegistro(new Date());
        usuario.setUsuarioActivo(true);
        usuario.setPasswordUsuario(encoder.encode(usuario.getPasswordUsuario()));

        Usuario nuevo = usuarioService.guardar(usuario);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    
    @PutMapping("/{id}/rol")
    public ResponseEntity<Usuario> actualizarRol(@PathVariable Long id, @RequestBody Usuario usuario) {

        Usuario existente = usuarioService.obtenerPorId(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getRoles().isEmpty()) {
            throw new RuntimeException("Debe asignar al menos un rol");
        }

        Rol rol = usuario.getRoles().iterator().next();
        Rol rolExistente = rolRepository.findById(rol.getId())
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        existente.setRoles(new HashSet<>(Collections.singletonList(rolExistente)));

        return ResponseEntity.ok(usuarioService.guardar(existente));
    }

    @PutMapping("/{id}/activo")
    public ResponseEntity<Usuario> actualizarActivo(@PathVariable Long id, @RequestBody Boolean activo) {
        Usuario existente = usuarioService.obtenerPorId(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        existente.setUsuarioActivo(activo);
        Usuario actualizado = usuarioService.guardar(existente);

        return ResponseEntity.ok(actualizado);
    }

    @PostMapping("/recuperar")
    public ResponseEntity<Map<String, String>> recuperarPassword(@RequestBody Map<String, String> request) {
        String correo = request.get("correo");

        Usuario usuario = usuarioService.obtenerPorCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Correo no registrado"));

        // Generar contraseña temporal
        String nuevaPassword = UUID.randomUUID().toString().substring(0, 8);

        usuario.setPasswordUsuario(encoder.encode(nuevaPassword));
        usuarioService.guardar(usuario);

        // Enviar correo
        emailService.enviarCorreo(
                correo,
                "Recuperación de contraseña",
                "Tu nueva contraseña temporal es: " + nuevaPassword +
                        "\nPor favor cámbiala después de iniciar sesión."
        );

        Map<String, String> response = new HashMap<>();
        response.put("message", "Se envió un correo con la nueva contraseña");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Usuario> actualizarPassword(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String nuevaPassword = request.get("passwordUsuario");
        if (nuevaPassword == null || nuevaPassword.trim().isEmpty()) {
            throw new RuntimeException("La nueva contraseña no puede estar vacía");
        }

        Usuario existente = usuarioService.obtenerPorId(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        existente.setPasswordUsuario(encoder.encode(nuevaPassword));
        Usuario actualizado = usuarioService.guardar(existente);

        return ResponseEntity.ok(actualizado);
    }

    @GetMapping("/correo/{correo}")
    public ResponseEntity<Usuario> obtenerPorCorreo(@PathVariable String correo) {
        Usuario usuario = usuarioService.obtenerPorCorreo(correo)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(usuario);
    }

}
