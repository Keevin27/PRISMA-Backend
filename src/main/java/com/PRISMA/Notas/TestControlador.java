package com.PRISMA.Notas;

import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
@CrossOrigin(origins = "*")
public class TestControlador {
    
    @GetMapping("/ping")
    public Map<String, String> ping() {
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "PONG - Spring Boot funciona!");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return response;
    }
    
    @PostMapping("/echo")
    public Map<String, Object> echo(@RequestBody Map<String, Object> datos) {
        System.out.println("📦 Datos recibidos: " + datos);
        Map<String, Object> response = new HashMap<>();
        response.put("recibido", datos);
        response.put("mensaje", "POST funciona correctamente");
        return response;
    }
}
