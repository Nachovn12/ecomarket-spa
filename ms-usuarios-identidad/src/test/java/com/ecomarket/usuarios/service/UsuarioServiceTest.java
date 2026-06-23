package com.ecomarket.usuarios.service;

import com.ecomarket.usuarios.dto.UsuarioRequestDTO;
import com.ecomarket.usuarios.dto.UsuarioResponseDTO;
import com.ecomarket.usuarios.dto.ActualizarPerfilClienteRequestDTO;
import com.ecomarket.usuarios.dto.PerfilClienteResponseDTO;
import com.ecomarket.usuarios.exception.CredencialesInvalidasException;
import com.ecomarket.usuarios.exception.UsuarioNoEncontradoException;
import com.ecomarket.usuarios.exception.UsuarioYaExisteException;
import com.ecomarket.usuarios.model.Rol;
import com.ecomarket.usuarios.model.Usuario;
import com.ecomarket.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    // ─── registrar cliente sin RUN ────────────────────────────────────────────
    @Test
    void registrarCliente_SinRun_Exito() {
        UsuarioRequestDTO req = new UsuarioRequestDTO();
        req.setNombre("Test User");
        req.setCorreo("test@test.com");
        req.setPassword("Password1");
        // run = null → rama null del if(run != null)

        when(usuarioRepository.existsByCorreo("test@test.com")).thenReturn(false);
        when(usuarioRepository.save(any())).thenAnswer(i -> {
            Usuario u = i.getArgument(0);
            u.setId(1L);
            return u;
        });

        UsuarioResponseDTO resp = usuarioService.registrarCliente(req);
        assertNotNull(resp);
        assertEquals("test@test.com", resp.getCorreo());
    }

    // ─── registrar cliente con RUN válido ─────────────────────────────────────
    @Test
    void registrarCliente_ConRun_Exito() {
        UsuarioRequestDTO req = new UsuarioRequestDTO();
        req.setNombre("Test Run");
        req.setCorreo("runuser@test.com");
        req.setPassword("Password1");
        req.setRun("12345678-5");

        when(usuarioRepository.existsByCorreo("runuser@test.com")).thenReturn(false);
        when(usuarioRepository.save(any())).thenAnswer(i -> {
            Usuario u = i.getArgument(0);
            u.setId(2L);
            return u;
        });

        UsuarioResponseDTO resp = usuarioService.registrarCliente(req);
        assertNotNull(resp);
    }

    // ─── registrar con RUN blank (rama blank del if) ──────────────────────────
    @Test
    void registrarCliente_ConRunBlank_Exito() {
        UsuarioRequestDTO req = new UsuarioRequestDTO();
        req.setNombre("Test Blank");
        req.setCorreo("blank@test.com");
        req.setPassword("Password1");
        req.setRun("  "); // blank → no se valida

        when(usuarioRepository.existsByCorreo("blank@test.com")).thenReturn(false);
        when(usuarioRepository.save(any())).thenAnswer(i -> {
            Usuario u = i.getArgument(0);
            u.setId(3L);
            return u;
        });

        UsuarioResponseDTO resp = usuarioService.registrarCliente(req);
        assertNotNull(resp);
    }

    // ─── password inválida ────────────────────────────────────────────────────
    @Test
    void registrarCliente_Falla_PasswordInvalida() {
        UsuarioRequestDTO req = new UsuarioRequestDTO();
        req.setNombre("Test");
        req.setCorreo("test@test.com");
        req.setPassword("1234"); // no cumple política
        assertThrows(CredencialesInvalidasException.class, () -> usuarioService.registrarCliente(req));
    }

    // ─── correo duplicado ─────────────────────────────────────────────────────
    @Test
    void registrarCliente_Falla_CorreoDuplicado() {
        UsuarioRequestDTO req = new UsuarioRequestDTO();
        req.setNombre("Test");
        req.setCorreo("test@test.com");
        req.setPassword("Password1");

        when(usuarioRepository.existsByCorreo("test@test.com")).thenReturn(true);
        assertThrows(UsuarioYaExisteException.class, () -> usuarioService.registrarCliente(req));
    }

    // ─── obtener perfil: cliente activo ───────────────────────────────────────
    @Test
    void obtenerPerfil_Exito() {
        Usuario u = Usuario.builder().id(1L).rol(Rol.CLIENTE).activo(true).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));

        PerfilClienteResponseDTO resp = usuarioService.obtenerPerfilCliente(1L);
        assertNotNull(resp);
    }

    // ─── obtener perfil: no encontrado ────────────────────────────────────────
    @Test
    void obtenerPerfil_Falla_NoEncontrado() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(UsuarioNoEncontradoException.class, () -> usuarioService.obtenerPerfilCliente(99L));
    }

    // ─── obtener perfil: eliminado ────────────────────────────────────────────
    @Test
    void obtenerPerfil_Falla_Eliminado() {
        Usuario u = Usuario.builder().id(1L).rol(Rol.CLIENTE).activo(true).eliminado(true).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));
        assertThrows(UsuarioNoEncontradoException.class, () -> usuarioService.obtenerPerfilCliente(1L));
    }

    // ─── obtener perfil: no es CLIENTE ───────────────────────────────────────
    @Test
    void obtenerPerfil_Falla_NoEsCliente() {
        Usuario u = Usuario.builder().id(1L).rol(Rol.EMPLEADO).activo(true).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));
        assertThrows(UsuarioNoEncontradoException.class, () -> usuarioService.obtenerPerfilCliente(1L));
    }

    // ─── actualizar perfil: éxito con medioPago ───────────────────────────────
    @Test
    void actualizarPerfil_Exito_ConMedioPago() {
        Usuario u = Usuario.builder().id(1L).correo("test@test.com").rol(Rol.CLIENTE).activo(true).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));
        when(usuarioRepository.findByCorreo("nuevo@test.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        ActualizarPerfilClienteRequestDTO req = new ActualizarPerfilClienteRequestDTO();
        req.setNombre("Nuevo");
        req.setCorreo("nuevo@test.com");
        req.setTelefono("+56987654321");
        req.setDireccionEnvio("Av. Siempre Viva 742");
        req.setMedioPago("TARJETA_CREDITO"); // rama con medioPago != null

        PerfilClienteResponseDTO resp = usuarioService.actualizarPerfilCliente(1L, req);
        assertNotNull(resp);
    }

    // ─── actualizar perfil: sin medioPago (rama else) ─────────────────────────
    @Test
    void actualizarPerfil_Exito_SinMedioPago() {
        Usuario u = Usuario.builder().id(1L).correo("test@test.com").rol(Rol.CLIENTE).activo(true).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));
        when(usuarioRepository.findByCorreo("test@test.com")).thenReturn(Optional.of(u));
        when(usuarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        ActualizarPerfilClienteRequestDTO req = new ActualizarPerfilClienteRequestDTO();
        req.setNombre("Nuevo");
        req.setCorreo("test@test.com"); // mismo correo → no lanza excepción
        req.setTelefono("+56987654321");
        req.setDireccionEnvio("Av. Siempre Viva 742");
        req.setMedioPago(null); // rama else → medioPago = null

        PerfilClienteResponseDTO resp = usuarioService.actualizarPerfilCliente(1L, req);
        assertNotNull(resp);
    }

    // ─── actualizar perfil: correo duplicado de otro usuario ─────────────────
    @Test
    void actualizarPerfil_Falla_CorreoDuplicado() {
        Usuario u = Usuario.builder().id(1L).correo("test@test.com").rol(Rol.CLIENTE).activo(true).eliminado(false).build();
        Usuario otro = Usuario.builder().id(2L).correo("ocupado@test.com").build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));
        when(usuarioRepository.findByCorreo("ocupado@test.com")).thenReturn(Optional.of(otro));

        ActualizarPerfilClienteRequestDTO req = new ActualizarPerfilClienteRequestDTO();
        req.setNombre("Nuevo");
        req.setCorreo("ocupado@test.com");
        req.setTelefono("+56987654321");
        req.setDireccionEnvio("Calle 123");

        assertThrows(UsuarioYaExisteException.class, () -> usuarioService.actualizarPerfilCliente(1L, req));
    }
}
