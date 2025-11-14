package com.PRISMA;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.PRISMA.Docente.DocenteControlador;
import com.PRISMA.Docente.DocenteRepositorio;
import com.PRISMA.Entity.Docente;
import com.PRISMA.Entity.Rol;
import com.PRISMA.Entity.Usuario;
import com.PRISMA.Seguridad.RolRepositorio;
import com.PRISMA.Seguridad.UsuarioRepositorio;

public class DocenteControladorTest {
    @Mock
    private DocenteRepositorio docenteRepositorio;

    @Mock
    private UsuarioRepositorio usuarioRepo;

    @Mock
    private RolRepositorio rolRepo;

    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private DocenteControlador docenteControlador;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGuardarDocente() {
        // Arrange
        Docente docente = new Docente();
        docente.setNombre_Docente("Juan Paco");
        docente.setCorreo_Docente("docente@ejemplo.com");

        Rol rol = new Rol();
        rol.setNombre("ROLE_DOCENTE");

        when(docenteRepositorio.save(any(Docente.class))).thenAnswer(inv -> inv.getArgument(0));
        when(rolRepo.findByNombre("ROLE_DOCENTE")).thenReturn(Optional.of(rol));
        when(encoder.encode("admin123")).thenReturn("encryptedPassword");

        // Act
        Docente resultado = docenteControlador.guardarDocente(docente);

        // Assert
        assertNotNull(resultado);
        assertEquals("docente@ejemplo.com", resultado.getCorreo_Docente());

        // Verifica que se haya guardado el usuario
        verify(usuarioRepo).save(any(Usuario.class));
    }


}
