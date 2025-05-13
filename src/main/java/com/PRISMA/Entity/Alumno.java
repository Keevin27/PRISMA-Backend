package com.PRISMA.Entity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/test")
public class Alumno {
	@GetMapping
	public String holamundo() {
		return "hola mundo";
	}
}
