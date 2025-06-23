package com.PRISMA.Alumno;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.PRISMA.Entity.Alumno;
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
@RequestMapping("/Alu/")
@CrossOrigin(origins = "http://localhost:4200/")
public class AlumnoControlador {

    @Autowired
    private AlumnoRepositorio repositorio;

    // Listar todos los alumnos
    @GetMapping("/alumnos")
    public List<Alumno> listarTodosLosAlumnos() {
        return repositorio.findAll();
    }

    // Listar alumnos por grado
    @GetMapping("/alumnos/{id_grado}")
    public List<Alumno> listarAlumnoPorGrado(@PathVariable int id_grado) {
        return repositorio.buscarPorGrado(id_grado);
    }

    // Buscar alumno por NIE
    @GetMapping("/alumnos/nie/{nie}")
    public ResponseEntity<Alumno> obtenerAlumnoPorNie(@PathVariable int nie) {
        Optional<Alumno> alumno = repositorio.buscarPorNie(nie);
        if (alumno.isPresent()) {
            return ResponseEntity.ok(alumno.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear nuevo alumno
    @PostMapping("/crearalumno")
    public ResponseEntity<Map<String, String>> guardarAlumno(@RequestBody Alumno alumno) {
        Map<String, String> respuesta = new HashMap<>();
        try {
            Optional<Alumno> existeNie = repositorio.buscarPorNie(alumno.getNie());
            if (existeNie.isPresent()) {
                respuesta.put("error", "El NIE " + alumno.getNie() + " ya está registrado.");
                return ResponseEntity.badRequest().body(respuesta);
            }

            if (!alumno.isEstado_alumno()) {
                alumno.setEstado_alumno(true);
            }

            if (alumno.getGrado() == null) {
                respuesta.put("error", "Debe seleccionar un grado válido.");
                return ResponseEntity.badRequest().body(respuesta);
            }

            Alumno nuevoAlumno = repositorio.save(alumno);
            respuesta.put("mensaje", "Alumno guardado exitosamente con ID: " + nuevoAlumno.getIdAlumno());
            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            respuesta.put("error", "Error al guardar el alumno: " + e.getMessage());
            return ResponseEntity.badRequest().body(respuesta);
        }
    }

    // Actualizar alumno existente
    @PutMapping("/alumnos/{id}")
    public ResponseEntity<Alumno> actualizarAlumno(@PathVariable int id, @RequestBody Alumno alumnoActualizado) {
        Optional<Alumno> alumnoExistente = repositorio.findById(id);

        if (alumnoExistente.isPresent()) {
            Alumno alumno = alumnoExistente.get();

            // Verificar si el NIE cambió y si ya existe en otro alumno
            if (alumno.getNie() != alumnoActualizado.getNie()) {
                Optional<Alumno> existeNie = repositorio.buscarPorNie(alumnoActualizado.getNie());
                if (existeNie.isPresent() && existeNie.get().getIdAlumno() != id) {
                    return ResponseEntity.badRequest().build();
                }
            }

            // Actualizar campos
            alumno.setNie(alumnoActualizado.getNie());
            alumno.setNombre_alumno(alumnoActualizado.getNombre_alumno());
            alumno.setApellido_alumno(alumnoActualizado.getApellido_alumno());
            alumno.setCorreo_alumno(alumnoActualizado.getCorreo_alumno());
            alumno.setFecha_nacimiento_alumno(alumnoActualizado.getFecha_nacimiento_alumno());
            alumno.setDireccion_a(alumnoActualizado.getDireccion_a());
            alumno.setSexo_a(alumnoActualizado.getSexo_a());
            alumno.setTelefono_alumno(alumnoActualizado.getTelefono_alumno());
            alumno.setEnfermedades(alumnoActualizado.getEnfermedades());
            alumno.setMedicamento(alumnoActualizado.getMedicamento());
            alumno.setVive_con(alumnoActualizado.getVive_con());
            alumno.setParentezco_encargado(alumnoActualizado.getParentezco_encargado());
            alumno.setTelefono_encargado(alumnoActualizado.getTelefono_encargado());
            alumno.setCorreo_encargado(alumnoActualizado.getCorreo_encargado());
            alumno.setDui_encargado(alumnoActualizado.getDui_encargado());
            alumno.setLugar_de_trabajo(alumnoActualizado.getLugar_de_trabajo());
            alumno.setEstado_alumno(alumnoActualizado.isEstado_alumno());

            // Validar y actualizar grado
            if (alumnoActualizado.getGrado() != null) {
                alumno.setGrado(alumnoActualizado.getGrado());
            }

            Alumno alumnoGuardado = repositorio.save(alumno);
            return ResponseEntity.ok(alumnoGuardado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar alumno
    @DeleteMapping("/alumnos/{id}")
    public ResponseEntity<String> eliminarAlumno(@PathVariable int id) {
        Optional<Alumno> alumno = repositorio.findById(id);

        if (alumno.isPresent()) {
            try {
                repositorio.deleteById(id); // Eliminar físicamente
                return ResponseEntity.ok("Alumno eliminado exitosamente");
            } catch (Exception e) {
                return ResponseEntity.badRequest()
                        .body("Error al eliminar el alumno: " + e.getMessage());
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Activar/Desactivar alumno
    @PutMapping("/alumnos/{id}/estado")
    public ResponseEntity<Alumno> cambiarEstadoAlumno(@PathVariable int id) {
        Optional<Alumno> alumno = repositorio.findById(id);

        if (alumno.isPresent()) {
            Alumno a = alumno.get();
            a.setEstado_alumno(!a.isEstado_alumno());
            Alumno alumnoGuardado = repositorio.save(a);
            return ResponseEntity.ok(alumnoGuardado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Listar solo alumnos activos
    @GetMapping("/alumnos/activos")
    public List<Alumno> listarAlumnosActivos() {
        return repositorio.buscarAlumnosActivos();
    }

    // Buscar por ID
    @GetMapping("/alumnos/id/{id}")
    public ResponseEntity<Alumno> obtenerAlumnoPorId(@PathVariable int id) {
        Optional<Alumno> alumno = repositorio.findById(id);
        if (alumno.isPresent()) {
            return ResponseEntity.ok(alumno.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Generar PDF del alumno
    @GetMapping("/alumnos/{id}/imprimir")
    public ResponseEntity<byte[]> imprimirExpedienteAlumno(@PathVariable int id) {
        Optional<Alumno> alumnoOpt = repositorio.findById(id);

        if (!alumnoOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Alumno alumno = alumnoOpt.get();

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 36, 36, 72, 36);
            PdfWriter.getInstance(document, baos);

            // Tabla para Imprimir Datos Alumno
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

            Paragraph header = new Paragraph(
                    "CENTRO ESCOLAR GUSTAVO VIDES VALDES\nLOURDES COLÓN\nEXPEDIENTE PERSONAL ALUMNO: DATOS PERSONALES",
                    tituloFont);
            header.setAlignment(Element.ALIGN_CENTER);
            header.setSpacingAfter(20);
            document.add(header);

            // Datos Personales
            PdfPCell datosPersonales = new PdfPCell(new Paragraph("Datos Personales", tituloFont));
            datosPersonales.setColspan(2);
            datosPersonales.setBackgroundColor(new Color(230, 230, 230));
            datosPersonales.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(datosPersonales);

            // NIE
            table.addCell(new PdfPCell(new Paragraph("NIE: ", subtituloFont)));
            table.addCell(new PdfPCell(new Paragraph(String.valueOf(alumno.getNie()), textoFont)));

            // Nombre y apellido
            table.addCell(new PdfPCell(new Paragraph("Nombre: ", subtituloFont)));
            table.addCell(new PdfPCell(
                    new Paragraph(alumno.getNombre_alumno() + " " + alumno.getApellido_alumno(), textoFont)));

            // Fecha de Nacimiento
            table.addCell(new PdfPCell(new Paragraph("Fecha de Nacimiento: ", subtituloFont)));
            table.addCell(new PdfPCell(new Paragraph(
                    alumno.getFecha_nacimiento_alumno() != null ? alumno.getFecha_nacimiento_alumno().toString()
                            : "No especificado",
                    textoFont)));

            // Sexo
            table.addCell(new PdfPCell(new Paragraph("Sexo: ", subtituloFont)));
            table.addCell(new PdfPCell(
                    new Paragraph(alumno.getSexo_a() != null ? alumno.getSexo_a() : "No especificado", textoFont)));

            // Grado
            table.addCell(new PdfPCell(new Paragraph("Grado: ", subtituloFont)));
            table.addCell(new PdfPCell(new Paragraph(
                    alumno.getGrado() != null ? alumno.getGrado().getNombre_grado() : "No especificado", textoFont)));

            // Información de Contacto
            PdfPCell contacto = new PdfPCell(new Paragraph("Información de Contacto", tituloFont));
            contacto.setColspan(2);
            contacto.setHorizontalAlignment(Element.ALIGN_CENTER);
            contacto.setBackgroundColor(new Color(230, 230, 230));
            table.addCell(contacto);

            // Teléfono
            table.addCell(new PdfPCell(new Paragraph("Teléfono: ", subtituloFont)));
            table.addCell(new PdfPCell(new Paragraph(
                    alumno.getTelefono_alumno() != null ? alumno.getTelefono_alumno() : "No especificado", textoFont)));

            // Correo
            table.addCell(new PdfPCell(new Paragraph("Correo Electrónico: ", subtituloFont)));
            table.addCell(new PdfPCell(new Paragraph(
                    alumno.getCorreo_alumno() != null ? alumno.getCorreo_alumno() : "No especificado", textoFont)));

            // Dirección
            PdfPCell ubicacion = new PdfPCell(new Paragraph("Dirección", subtituloFont));
            ubicacion.setColspan(2);
            ubicacion.setHorizontalAlignment(Element.ALIGN_CENTER);
            ubicacion.setBackgroundColor(new Color(230, 230, 230));
            table.addCell(ubicacion);

            PdfPCell direccion = new PdfPCell(new Paragraph(
                    alumno.getDireccion_a() != null ? alumno.getDireccion_a() : "No especificado", textoFont));
            direccion.setColspan(2);
            direccion.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(direccion);

            // Información del Encargado
            PdfPCell encargado = new PdfPCell(new Paragraph("Información del Encargado", tituloFont));
            encargado.setColspan(2);
            encargado.setHorizontalAlignment(Element.ALIGN_CENTER);
            encargado.setBackgroundColor(new Color(230, 230, 230));
            table.addCell(encargado);

            // Vive con
            table.addCell(new PdfPCell(new Paragraph("Vive con: ", subtituloFont)));
            table.addCell(new PdfPCell(
                    new Paragraph(alumno.getVive_con() != null ? alumno.getVive_con() : "No especificado", textoFont)));

            // Parentezco
            table.addCell(new PdfPCell(new Paragraph("Parentezco: ", subtituloFont)));
            table.addCell(new PdfPCell(new Paragraph(
                    alumno.getParentezco_encargado() != null ? alumno.getParentezco_encargado() : "No especificado",
                    textoFont)));

            // Teléfono encargado
            table.addCell(new PdfPCell(new Paragraph("Teléfono Encargado: ", subtituloFont)));
            table.addCell(new PdfPCell(new Paragraph(
                    alumno.getTelefono_encargado() != null ? alumno.getTelefono_encargado() : "No especificado",
                    textoFont)));

            // DUI encargado
            table.addCell(new PdfPCell(new Paragraph("DUI Encargado: ", subtituloFont)));
            table.addCell(new PdfPCell(new Paragraph(
                    alumno.getDui_encargado() != null ? alumno.getDui_encargado() : "No especificado", textoFont)));

            // Información Médica
            PdfPCell medica = new PdfPCell(new Paragraph("Información Médica", tituloFont));
            medica.setColspan(2);
            medica.setHorizontalAlignment(Element.ALIGN_CENTER);
            medica.setBackgroundColor(new Color(230, 230, 230));
            table.addCell(medica);

            // Enfermedades
            table.addCell(new PdfPCell(new Paragraph("Enfermedades: ", subtituloFont)));
            table.addCell(new PdfPCell(
                    new Paragraph(alumno.getEnfermedades() != null ? alumno.getEnfermedades() : "Ninguna", textoFont)));

            // Medicamentos
            table.addCell(new PdfPCell(new Paragraph("Medicamentos: ", subtituloFont)));
            table.addCell(new PdfPCell(
                    new Paragraph(alumno.getMedicamento() != null ? alumno.getMedicamento() : "Ninguno", textoFont)));

            // Guardamos la tabla en el documento
            document.add(table);
            document.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment",
                    "expediente_alumno_" + alumno.getNie() + "_" + alumno.getNombre_alumno() + ".pdf");

            return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/alumnos/imprimir-listado")
    public ResponseEntity<byte[]> imprimirListadoAlumnos() {
        List<Alumno> alumnos = repositorio.findAll();

        if (alumnos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 36, 36, 72, 36);
            PdfWriter.getInstance(document, baos);

            document.open();

            // Tipos de letra
            Font tituloFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font subtituloFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font textoFont = new Font(Font.HELVETICA, 10);
            Font headerTableFont = new Font(Font.HELVETICA, 11, Font.BOLD);

            Image logo = Image.getInstance("src/main/resources/static/logo_GVV.png");
            logo.scaleToFit(100, 100);
            logo.setAbsolutePosition(500, 700);
            document.add(logo);

            // Título principal
            Paragraph header = new Paragraph("CENTRO ESCOLAR GUSTAVO VIDES VALDES\nLOURDES COLÓN\nLISTADO DE ALUMNOS",
                    tituloFont);
            header.setAlignment(Element.ALIGN_CENTER);
            header.setSpacingAfter(20);
            document.add(header);

            // Información adicional
            Paragraph info = new Paragraph(
                    "Total de alumnos: " + alumnos.size() + "\nFecha de impresión: " + java.time.LocalDate.now(),
                    subtituloFont);
            info.setSpacingAfter(15);
            document.add(info);

            // Crear tabla con las columnas principales
            PdfPTable table = new PdfPTable(4); // 4 columnas: NIE, Nombre, Apellidos, Grado
            table.setWidthPercentage(100);

            // Establecer anchos de columnas
            float[] columnWidths = { 15f, 25f, 25f, 35f };
            table.setWidths(columnWidths);

            // Headers de la tabla
            String[] headerTexts = { "NIE", "NOMBRE", "APELLIDOS", "GRADO" };
            for (String headerText : headerTexts) {
                PdfPCell headerCell = new PdfPCell(new Paragraph(headerText, headerTableFont));
                headerCell.setBackgroundColor(new Color(220, 220, 220));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setPadding(8);
                table.addCell(headerCell);
            }

            // Llenar la tabla con los datos de los alumnos
            for (Alumno alumno : alumnos) {
                // NIE
                PdfPCell cellNie = new PdfPCell(new Paragraph(String.valueOf(alumno.getNie()), textoFont));
                cellNie.setPadding(5);
                cellNie.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellNie);

                // Nombre
                PdfPCell cellNombre = new PdfPCell(
                        new Paragraph(alumno.getNombre_alumno() != null ? alumno.getNombre_alumno() : "", textoFont));
                cellNombre.setPadding(5);
                table.addCell(cellNombre);

                // Apellidos
                PdfPCell cellApellidos = new PdfPCell(new Paragraph(
                        alumno.getApellido_alumno() != null ? alumno.getApellido_alumno() : "", textoFont));
                cellApellidos.setPadding(5);
                table.addCell(cellApellidos);

                // Grado
                String nombreGrado = "";
                if (alumno.getGrado() != null) {
                    nombreGrado = alumno.getGrado().getNombre_grado();
                    if (alumno.getGrado().getSeccion() != null) {
                        nombreGrado += " - " + alumno.getGrado().getSeccion();
                    }
                }
                PdfPCell cellGrado = new PdfPCell(new Paragraph(nombreGrado, textoFont));
                cellGrado.setPadding(5);
                table.addCell(cellGrado);
            }

            document.add(table);
            document.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "listado_alumnos_" + java.time.LocalDate.now() + ".pdf");

            return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Generar PDF filtrado por parámetros
    @GetMapping("/alumnos/imprimir-listado-filtrado")
    public ResponseEntity<byte[]> imprimirListadoFiltrado(
            @RequestParam(required = false) String anio,
            @RequestParam(required = false) String grado) {

        List<Alumno> alumnos = repositorio.findAll();

        // Aplicar filtros si se proporcionan
        if (anio != null && !anio.isEmpty()) {
            alumnos = alumnos.stream()
                    .filter(a -> a.getGrado() != null &&
                            a.getGrado().getAnioAcademico() != null &&
                            String.valueOf(a.getGrado().getAnioAcademico().getAnio()).equals(anio))
                    .collect(java.util.stream.Collectors.toList());
        }

        if (grado != null && !grado.isEmpty()) {
            alumnos = alumnos.stream()
                    .filter(a -> a.getGrado() != null &&
                            String.valueOf(a.getGrado().getId_grado()).equals(grado))
                    .collect(java.util.stream.Collectors.toList());
        }

        if (alumnos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 36, 36, 72, 36);
            PdfWriter.getInstance(document, baos);

            document.open();

            // Tipos de letra
            Font tituloFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font subtituloFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font textoFont = new Font(Font.HELVETICA, 10);
            Font headerTableFont = new Font(Font.HELVETICA, 11, Font.BOLD);

            Image logo = Image.getInstance("src/main/resources/static/logo_GVV.png");
            logo.scaleToFit(100, 100);
            logo.setAbsolutePosition(500, 700);
            document.add(logo);

            // Título principal
            String titulo = "CENTRO ESCOLAR GUSTAVO VIDES VALDES\nLOURDES COLÓN\nLISTADO DE ALUMNOS";
            if (anio != null || grado != null) {
                titulo += " - FILTRADO";
            }

            Paragraph header = new Paragraph(titulo, tituloFont);
            header.setAlignment(Element.ALIGN_CENTER);
            header.setSpacingAfter(15);
            document.add(header);

            // Información de filtros aplicados
            if (anio != null || grado != null) {
                String filtrosInfo = "Filtros aplicados: ";
                if (anio != null)
                    filtrosInfo += "Año: " + anio + " ";
                if (grado != null)
                    filtrosInfo += "Grado ID: " + grado + " ";

                Paragraph filtros = new Paragraph(filtrosInfo, subtituloFont);
                filtros.setSpacingAfter(10);
                document.add(filtros);
            }

            // Información adicional
            Paragraph info = new Paragraph(
                    "Total de alumnos: " + alumnos.size() + "\nFecha de impresión: " + java.time.LocalDate.now(),
                    subtituloFont);
            info.setSpacingAfter(15);
            document.add(info);

            // Crear tabla
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            float[] columnWidths = { 15f, 25f, 25f, 35f };
            table.setWidths(columnWidths);

            // Headers
            String[] headerTexts = { "NIE", "NOMBRE", "APELLIDOS", "GRADO" };
            for (String headerText : headerTexts) {
                PdfPCell headerCell = new PdfPCell(new Paragraph(headerText, headerTableFont));
                headerCell.setBackgroundColor(new Color(220, 220, 220));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setPadding(8);
                table.addCell(headerCell);
            }

            // Datos
            for (Alumno alumno : alumnos) {
                table.addCell(new PdfPCell(new Paragraph(String.valueOf(alumno.getNie()), textoFont)));
                table.addCell(new PdfPCell(
                        new Paragraph(alumno.getNombre_alumno() != null ? alumno.getNombre_alumno() : "", textoFont)));
                table.addCell(new PdfPCell(new Paragraph(
                        alumno.getApellido_alumno() != null ? alumno.getApellido_alumno() : "", textoFont)));

                String nombreGrado = "";
                if (alumno.getGrado() != null) {
                    nombreGrado = alumno.getGrado().getNombre_grado();
                    if (alumno.getGrado().getSeccion() != null) {
                        nombreGrado += " - " + alumno.getGrado().getSeccion();
                    }
                }
                table.addCell(new PdfPCell(new Paragraph(nombreGrado, textoFont)));
            }

            document.add(table);
            document.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline",
                    "listado_alumnos_filtrado_" + java.time.LocalDate.now() + ".pdf");

            return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}