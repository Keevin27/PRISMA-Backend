package com.PRISMA.Docente;

import java.awt.Color;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.Docente.Excepciones.ResourceNotFoundException;
import com.PRISMA.Entity.Docente;
import com.PRISMA.Entity.Rol;
import com.PRISMA.Entity.Usuario;
import com.PRISMA.Seguridad.RolRepositorio;
import com.PRISMA.Seguridad.UsuarioRepositorio;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import com.lowagie.text.Chunk;
//
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.Element;



@RestController
@RequestMapping("/expedienteDocente/")
public class DocenteControlador {
    @Autowired
    private DocenteRepositorio repositorio;
    @Autowired
    private RolRepositorio rolRepo;
    @Autowired
    private UsuarioRepositorio usuarioRepo;
    @Autowired
    private BCryptPasswordEncoder encoder;

    //ListarDocente
    @GetMapping("/docentes")
    public List<Docente> listarDocentes() {
        return  repositorio.findAll();
    }

    //GuardarDocente
    @PostMapping("/docentes")
    public Docente guardarDocente(@RequestBody Docente docente) {
        docente.setDocente_Activo(true); // activa por defecto
        docente.setFecha_Registro_D(Date.valueOf(LocalDate.now()));; // fecha del sistema
        
        Docente savedDocente = repositorio.save(docente);

        Usuario user = new Usuario();
        user.setCorreoUsuario(docente.getCorreo_Docente());
        user.setPasswordUsuario(encoder.encode("123456")); // contraseña por defecto cifrada
        user.setFechaRegistro(Date.valueOf(LocalDate.now()));
        user.setUsuarioActivo(true);

        Rol rolDocente = rolRepo.findByNombre("ROLE_DOCENTE").orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        user.getRoles().add(rolDocente);

        usuarioRepo.save(user);

        return savedDocente;
    }
    //BuscarDocente
    @GetMapping("/docentes/{duiDocente}")
    public ResponseEntity<Docente> obtenerDocente(@PathVariable("duiDocente") String id) {
        Docente docente = repositorio.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No existe el docente con DUI: " + id));
        return ResponseEntity.ok(docente);
    } 

    @PutMapping("/docentes/{duiDocente}")
    public ResponseEntity<Docente> actualizarDocente(@PathVariable("duiDocente") String id,@RequestBody Docente detallesDocente) {
        Docente docente = repositorio.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No existe el docente con DUI: " + id));

            docente.setNombre_Docente(detallesDocente.getNombre_Docente());
            docente.setApellido_Docente(detallesDocente.getApellido_Docente());
            docente.setDuiDocente(detallesDocente.getDuiDocente());
            docente.setCorreo_Docente(detallesDocente.getCorreo_Docente());
            docente.setDireccion_D(detallesDocente.getDireccion_D());
            docente.setTelefono_Docente(detallesDocente.getTelefono_Docente());
            docente.setSexo_Docente(detallesDocente.getSexo_Docente());
            Docente docenteActualizado = repositorio.save(docente);
        return ResponseEntity.ok(docenteActualizado);
    } 

    //GeneraPDF individual de cada Docente
    @GetMapping("/docentes/{duiDocente}/imprimir")
    public ResponseEntity<byte[]> imprimirExpedienteDocente(@PathVariable String duiDocente) {
        Optional<Docente> docenteOpt = repositorio.findById(duiDocente);

        if (!docenteOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Docente docente = docenteOpt.get();

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 36, 36, 72, 36);
            PdfWriter.getInstance(document, baos);

            //Tabala para Imprimir Datos Docente
            PdfPTable table = new PdfPTable(2); // 2 columnas
            table.setWidthPercentage(100);

            document.open();

            // Título principal y tipos de letra para los elementos
            Font tituloFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font subtituloFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font textoFont = new Font(Font.HELVETICA, 11);

            Image logo = Image.getInstance("src/main/resources/static/logo_GVV.png");
            logo.scaleToFit(100, 100);
            logo.setAbsolutePosition(500, 700);
            document.add(logo);

            Paragraph header = new Paragraph("CENTRO ESCOLAR GUSTAVO VIDES VALDES\nLOURDES COLÓN\nEXPEDIENTE PERSONAL DOCENTE: DATOS PERSONALES", tituloFont);
            header.setAlignment(Element.ALIGN_CENTER);
            header.setSpacingAfter(20);
            document.add(header);
        
            PdfPCell DatosPersonales = new PdfPCell(new Paragraph("Datos Personales",tituloFont));
            //se ocupa las dos columnas y se centra
            DatosPersonales.setColspan(2);
            DatosPersonales.setBackgroundColor(new Color(230, 230, 230));
            DatosPersonales.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(DatosPersonales);

            //nombre y apellido
            table.addCell(new PdfPCell(new Paragraph("Nombre: ", subtituloFont)));
            table.addCell(new PdfPCell(new Paragraph(docente.getNombre_Docente() +" "+ docente.getApellido_Docente(), textoFont)));
            //DUI
            table.addCell(new PdfPCell(new Paragraph("N° de DUI: ", subtituloFont)));
            table.addCell(new PdfPCell(new Paragraph(docente.getDuiDocente(), textoFont)));
            //Fecha de Nacimiento
            table.addCell(new PdfPCell(new Paragraph("Fecha de Nacimiento", subtituloFont)));
            table.addCell(new PdfPCell(new Paragraph(""+docente.getFecha_Nacimiento_D(), textoFont)));
            //Sexo
            table.addCell(new PdfPCell(new Paragraph("Sexo: ", subtituloFont)));
            table.addCell(new PdfPCell(new Paragraph(docente.getSexo_Docente(), textoFont)));
            document.add(Chunk.NEWLINE);

            PdfPCell Contacto = new PdfPCell(new Paragraph("Informacion de Contacto",tituloFont));
            //se ocupa las dos columnas y se centra
            Contacto.setColspan(2);
            Contacto.setHorizontalAlignment(Element.ALIGN_CENTER);
            Contacto.setBackgroundColor(new Color(230, 230, 230));
            table.addCell(Contacto);

            table.addCell(new PdfPCell(new Paragraph("Telefono: ", subtituloFont)));
            table.addCell(new PdfPCell(new Paragraph(docente.getTelefono_Docente(), textoFont)));

            table.addCell(new PdfPCell(new Paragraph("Correo Electronico: ", subtituloFont)));
            table.addCell(new PdfPCell(new Paragraph(docente.getCorreo_Docente(), textoFont)));
            
            PdfPCell Ubicacion = new PdfPCell(new Paragraph("Direccion",subtituloFont));
            Ubicacion.setColspan(2);
            Ubicacion.setHorizontalAlignment(Element.ALIGN_CENTER);
            Ubicacion.setBackgroundColor(new Color(230, 230, 230));
            table.addCell(Ubicacion);
            PdfPCell Direccion = new PdfPCell(new Paragraph(docente.getDireccion_D(),textoFont));
            Direccion.setColspan(2);
            Direccion.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(Direccion);

            //guardamos la tabla en el documento
            document.add(table);

            document.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "expediente_" + docente.getNombre_Docente()+"_"+ docente.getDuiDocente() + ".pdf");

            return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    
}
