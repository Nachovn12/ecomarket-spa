package com.ecomarket.usuarios.service;

import com.ecomarket.usuarios.dto.LoginRequestDTO;
import com.ecomarket.usuarios.dto.LoginResponseDTO;
import com.ecomarket.usuarios.exception.CredencialesInvalidasException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AuthService authService;

    // ─── login exitoso CLIENTE ────────────────────────────────────────────────
    @Test
    void login_Exito_Cliente() {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setCorreo("cliente@test.com");
        req.setPassword("Password1");

        Usuario u = Usuario.builder().id(1L).correo("cliente@test.com")
                .password("Password1").rol(Rol.CLIENTE).activo(true).eliminado(false).build();
        when(usuarioRepository.findByCorreo("cliente@test.com")).thenReturn(Optional.of(u));

        LoginResponseDTO resp = authService.iniciarSesion(req);
        assertEquals("CLIENTE", resp.getRol());
        assertNotNull(resp.getFuncionalidadesDisponibles());
        assertFalse(resp.getFuncionalidadesDisponibles().isEmpty());
    }

    // ─── login exitoso EMPLEADO (cubre rama switch EMPLEADO) ─────────────────
    @Test
    void login_Exito_Empleado() {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setCorreo("empleado@test.com");
        req.setPassword("Password1");

        Usuario u = Usuario.builder().id(2L).correo("empleado@test.com")
                .password("Password1").rol(Rol.EMPLEADO).activo(true).eliminado(false).build();
        when(usuarioRepository.findByCorreo("empleado@test.com")).thenReturn(Optional.of(u));

        LoginResponseDTO resp = authService.iniciarSesion(req);
        assertEquals("EMPLEADO", resp.getRol());
        assertTrue(resp.getFuncionalidadesDisponibles().contains("registrar ventas"));
    }

    // ─── login exitoso GERENTE (cubre rama switch GERENTE) ───────────────────
    @Test
    void login_Exito_Gerente() {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setCorreo("gerente@test.com");
        req.setPassword("Password1");

        Usuario u = Usuario.builder().id(3L).correo("gerente@test.com")
                .password("Password1").rol(Rol.GERENTE).activo(true).eliminado(false).build();
        when(usuarioRepository.findByCorreo("gerente@test.com")).thenReturn(Optional.of(u));

        LoginResponseDTO resp = authService.iniciarSesion(req);
        assertEquals("GERENTE", resp.getRol());
        assertTrue(resp.getFuncionalidadesDisponibles().contains("gestionar inventario"));
    }

    // ─── login exitoso ADMINISTRADOR (cubre rama switch ADMINISTRADOR) ────────
    @Test
    void login_Exito_Administrador() {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setCorreo("admin@test.com");
        req.setPassword("Password1");

        Usuario u = Usuario.builder().id(4L).correo("admin@test.com")
                .password("Password1").rol(Rol.ADMINISTRADOR).activo(true).eliminado(false).build();
        when(usuarioRepository.findByCorreo("admin@test.com")).thenReturn(Optional.of(u));

        LoginResponseDTO resp = authService.iniciarSesion(req);
        assertEquals("ADMINISTRADOR", resp.getRol());
        assertTrue(resp.getFuncionalidadesDisponibles().contains("gestionar usuarios"));
    }

    // ─── correo no existe ────────────────────────────────────────────────────
    @Test
    void login_Falla_CorreoNoExiste() {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setCorreo("noexiste@test.com");
        req.setPassword("Password1");
        when(usuarioRepository.findByCorreo("noexiste@test.com")).thenReturn(Optional.empty());

        assertThrows(CredencialesInvalidasException.class, () -> authService.iniciarSesion(req));
    }

    // ─── password incorrecto ─────────────────────────────────────────────────
    @Test
    void login_Falla_PasswordIncorrecto() {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setCorreo("cliente@test.com");
        req.setPassword("Wrongpass1");

        Usuario u = Usuario.builder().correo("cliente@test.com")
                .password("Password1").rol(Rol.CLIENTE).activo(true).eliminado(false).build();
        when(usuarioRepository.findByCorreo("cliente@test.com")).thenReturn(Optional.of(u));

        assertThrows(CredencialesInvalidasException.class, () -> authService.iniciarSesion(req));
    }

    // ─── cuenta inactiva (activo=false) ──────────────────────────────────────
    @Test
    void login_Falla_CuentaInactiva() {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setCorreo("cliente@test.com");
        req.setPassword("Password1");

        Usuario u = Usuario.builder().correo("cliente@test.com")
                .password("Password1").rol(Rol.CLIENTE).activo(false).eliminado(false).build();
        when(usuarioRepository.findByCorreo("cliente@test.com")).thenReturn(Optional.of(u));

        assertThrows(CredencialesInvalidasException.class, () -> authService.iniciarSesion(req));
    }

    // ─── cuenta eliminada (eliminado=true) ───────────────────────────────────
    @Test
    void login_Falla_CuentaEliminada() {
        LoginRequestDTO req = new LoginRequestDTO();
        req.setCorreo("cliente@test.com");
        req.setPassword("Password1");

        Usuario u = Usuario.builder().correo("cliente@test.com")
                .password("Password1").rol(Rol.CLIENTE).activo(true).eliminado(true).build();
        when(usuarioRepository.findByCorreo("cliente@test.com")).thenReturn(Optional.of(u));

        assertThrows(CredencialesInvalidasException.class, () -> authService.iniciarSesion(req));
    }
}
