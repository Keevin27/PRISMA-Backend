package com.PRISMA.Anexo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.DTO.AnexoDTO;
import com.PRISMA.Docente.DocenteRepositorio;
import com.PRISMA.Entity.Anexo;
import com.PRISMA.Entity.Docente;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/anexos/docente")
@CrossOrigin(origins = "http://localhost:4200")
public class AnexoControlador {
     @Autowired
    private AnexoRepositorio anexoRepositorio;

    @Autowired
    private DocenteRepositorio docenteRepositorio;

    @GetMapping("/{duiDocente}")
    public ResponseEntity<List<AnexoDTO>> obtenerAnexosPorDocente(@PathVariable String duiDocente) {
        List<AnexoDTO> dtoList = anexoRepositorio.findAnexosDTOByDocente(duiDocente);
    return ResponseEntity.ok(dtoList);
    }
    @GetMapping("/{id_Anexo_D}/archivo")
    public ResponseEntity<byte[]> descargarAnexo(@PathVariable Long id_Anexo_D) {
        Anexo anexo = anexoRepositorio.findById(id_Anexo_D).orElseThrow(() -> new RuntimeException("No existe"));

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + anexo.getNombre_Anexo_D() +".pdf"+ "\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(anexo.getDatos_Anexo_D());
    }

    //Eliminar Anexo
    @DeleteMapping("/{id_Anexo_D}")
    public ResponseEntity<?> eliminarAnexo(@PathVariable("id_Anexo_D") Long id) {
        if (!anexoRepositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        anexoRepositorio.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // Agregar anexo a un docente
    @PostMapping("/{duiDocente}")
    public Anexo agregarAnexo(@PathVariable String duiDocente, @RequestBody Anexo anexo) {
        Optional<Docente> docenteOpt = docenteRepositorio.findById(duiDocente);
        if (docenteOpt.isPresent()) {
            anexo.setDocente(docenteOpt.get());
            anexo.setFecha_Anexo_D(Date.valueOf(LocalDate.now()));
            return anexoRepositorio.save(anexo);
        } else {
            throw new RuntimeException("Docente no encontrado con DUI: " + duiDocente);
        }
    }
}
