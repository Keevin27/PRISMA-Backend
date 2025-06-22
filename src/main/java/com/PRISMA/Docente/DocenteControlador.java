package com.PRISMA.Docente;

import java.awt.Color;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.print.Doc;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.Docente.Excepciones.ResourceNotFoundException;
import com.PRISMA.Entity.Docente;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import com.lowagie.text.Chunk;
//
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.Element;
import java.awt.Color;





@RestController
@RequestMapping("/expedienteDocente/")
@CrossOrigin(origins = "http://localhost:4200")
public class DocenteControlador {
    @Autowired
    private DocenteRepositorio repositorio;

    //ListarDocente
    @GetMapping("/docentes")
    public List<Docente> listarDocentes() {
        return  repositorio.obtenerDocentesActivosOrdenados();
    }

    //GuardarDocente
    @PostMapping("/docentes")
    public Docente guardarDocente(@RequestBody Docente docente) {
        docente.setDocente_Activo(true); // activa por defecto
        docente.setFecha_Registro_D(Date.valueOf(LocalDate.now()));; // fecha del sistema
        return repositorio.save(docente);
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
            docente.setFecha_Nacimiento_D(detallesDocente.getFecha_Nacimiento_D());
            docente.setDuiDocente(detallesDocente.getDuiDocente());
            docente.setCorreo_Docente(detallesDocente.getCorreo_Docente());
            docente.setDireccion_D(detallesDocente.getDireccion_D());
            docente.setTelefono_Docente(detallesDocente.getTelefono_Docente());
            docente.setSexo_Docente(detallesDocente.getSexo_Docente());
            Docente docenteActualizado = repositorio.save(docente);
        return ResponseEntity.ok(docenteActualizado);
    } 
    //Actividad Docente
    @PutMapping("/docentes/{dui}/estado")
    public ResponseEntity<?> cambiarEstadoDocente(@PathVariable String dui, @RequestBody Map<String, Boolean> body) {
        Optional<Docente> docenteOpt = repositorio.findById(dui);
        if (!docenteOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Docente docente = docenteOpt.get();
        docente.setDocente_Activo(body.get("docente_Activo"));
        repositorio.save(docente);

        return ResponseEntity.ok().build();
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

    //GeneraPDF de los docentes activos
    @GetMapping("/docentes/imprimirTodos")
    public ResponseEntity<byte[]> imprimirTodosLosDocentesActivos() {
        //llamamos el metodo obtenerDocentesActivos() que esta en DocenteRepositorio
        List<Docente> docentesActivos = repositorio.obtenerDocentesActivos();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 36, 36); // horizontal
        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            Font tituloFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font encabezadoFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font textoFont = new Font(Font.HELVETICA, 10);

            Image logo = Image.getInstance("src/main/resources/static/logo_GVV.png");
            logo.scaleToFit(100, 100);
            logo.setAbsolutePosition(700, 470);
            document.add(logo);

            Paragraph titulo = new Paragraph("CENTRO ESCOLAR GUSTAVO VIDES VALDES\nLOURDES COLÓN\nLISTADO GENERAL: PERSONAL DOCENTE", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);

            PdfPTable table = new PdfPTable(6); // columnas: Nombre, Apellido, DUI, Teléfono, Correo, Edad
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setWidths(new float[]{2.5f, 2.5f, 2.5f, 2.5f, 3.5f, 1.5f});

            // Color de fondo del encabezado (azul) y texto blanco
            Color headerBackground = new Color(79, 129, 189); // Azul
            Color textColor = Color.WHITE;

            String[] titulos = { "Nombre", "Apellido", "DUI", "Teléfono", "Correo", "Edad" };
            for (String headerTitle : titulos) {
                PdfPCell headerCell = new PdfPCell(new Phrase(headerTitle, encabezadoFont));
                headerCell.setBackgroundColor(headerBackground);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                headerCell.setPadding(8);
                headerCell.setPhrase(new Phrase(headerTitle, new Font(Font.HELVETICA, 12, Font.BOLD, textColor)));
                table.addCell(headerCell);
            }


            for (Docente d : docentesActivos) {
                table.addCell(new Phrase(d.getNombre_Docente(), textoFont));
                table.addCell(new Phrase(d.getApellido_Docente(), textoFont));
                table.addCell(new Phrase(d.getDuiDocente(), textoFont));
                table.addCell(new Phrase(d.getTelefono_Docente(), textoFont));
                table.addCell(new Phrase(d.getCorreo_Docente(), textoFont));
                table.addCell(new Phrase(String.valueOf(calcularEdad(d.getFecha_Nacimiento_D())), textoFont));
            }

            document.add(table);
            document.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "docentes_activos.pdf");

            return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //compronar si ya se agrego el DUI
    @GetMapping("/docentes/existe/{duiDocente}")
    public ResponseEntity<Boolean> existeDocente(@PathVariable String duiDocente) {
        boolean existe = repositorio.existsById(duiDocente);
        return ResponseEntity.ok(existe);
    }

    private int calcularEdad(Date fecha_Nacimiento_D) {
        if (fecha_Nacimiento_D == null) {
            return 0; // o puedes retornar -1 o dejar vacío en el PDF
        }
        LocalDate nacimiento = fecha_Nacimiento_D.toLocalDate();
        return Period.between(nacimiento, LocalDate.now()).getYears();
    }
}
