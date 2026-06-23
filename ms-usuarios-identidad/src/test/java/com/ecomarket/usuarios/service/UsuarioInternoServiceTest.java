package com.ecomarket.usuarios.service;

import com.ecomarket.usuarios.dto.UsuarioInternoRequestDTO;
import com.ecomarket.usuarios.dto.UsuarioInternoResponseDTO;
import com.ecomarket.usuarios.dto.UsuarioInternoUpdateDTO;
import com.ecomarket.usuarios.exception.AccesoNoAutorizadoException;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioInternoServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioInternoService usuarioInternoService;

    // ─── crear usuario interno: éxito ─────────────────────────────────────────
    @Test
    void crearUsuarioInterno_Exito() {
        UsuarioInternoRequestDTO req = new UsuarioInternoRequestDTO();
        req.setNombre("Interno 1");
        req.setCorreo("interno@test.com");
        req.setPassword("P@ssw0rd1");
        req.setRol("EMPLEADO");

        when(usuarioRepository.existsByCorreo("interno@test.com")).thenReturn(false);
        when(usuarioRepository.save(any())).thenReturn(
            Usuario.builder().id(1L).nombre("Interno 1").correo("interno@test.com")
                .rol(Rol.EMPLEADO).activo(true).eliminado(false).build());

        UsuarioInternoResponseDTO resp = usuarioInternoService.crearUsuarioInterno(req, "ADMINISTRADOR");
        assertNotNull(resp);
        assertEquals("interno@test.com", resp.getCorreo());
    }

    // ─── crear: sin permiso admin ─────────────────────────────────────────────
    @Test
    void crearUsuarioInterno_Falla_NoAdmin() {
        UsuarioInternoRequestDTO req = new UsuarioInternoRequestDTO();
        assertThrows(AccesoNoAutorizadoException.class, () ->
            usuarioInternoService.crearUsuarioInterno(req, "EMPLEADO"));
    }

    // ─── crear: rolSolicitante null (rama null del validar) ───────────────────
    @Test
    void crearUsuarioInterno_Falla_RolNull() {
        UsuarioInternoRequestDTO req = new UsuarioInternoRequestDTO();
        assertThrows(AccesoNoAutorizadoException.class, () ->
            usuarioInternoService.crearUsuarioInterno(req, null));
    }

    // ─── crear: correo duplicado ──────────────────────────────────────────────
    @Test
    void crearUsuarioInterno_Falla_CorreoDuplicado() {
        UsuarioInternoRequestDTO req = new UsuarioInternoRequestDTO();
        req.setNombre("Interno");
        req.setCorreo("dup@test.com");
        req.setPassword("P@ssw0rd1");
        req.setRol("EMPLEADO");

        when(usuarioRepository.existsByCorreo("dup@test.com")).thenReturn(true);
        assertThrows(UsuarioYaExisteException.class, () ->
            usuarioInternoService.crearUsuarioInterno(req, "ADMINISTRADOR"));
    }

    // ─── crear: password inválida ─────────────────────────────────────────────
    @Test
    void crearUsuarioInterno_Falla_PasswordInvalida() {
        UsuarioInternoRequestDTO req = new UsuarioInternoRequestDTO();
        req.setNombre("Interno");
        req.setCorreo("interno@test.com");
        req.setPassword("1234");
        req.setRol("EMPLEADO");

        when(usuarioRepository.existsByCorreo("interno@test.com")).thenReturn(false);
        assertThrows(CredencialesInvalidasException.class, () ->
            usuarioInternoService.crearUsuarioInterno(req, "ADMINISTRADOR"));
    }

    // ─── crear: rol inválido (no es EMPLEADO/GERENTE/ADMINISTRADOR) ───────────
    @Test
    void crearUsuarioInterno_Falla_RolCliente() {
        UsuarioInternoRequestDTO req = new UsuarioInternoRequestDTO();
        req.setNombre("Interno");
        req.setCorreo("interno@test.com");
        req.setPassword("P@ssw0rd1");
        req.setRol("CLIENTE"); // rol no interno

        assertThrows(IllegalArgumentException.class, () ->
            usuarioInternoService.crearUsuarioInterno(req, "ADMINISTRADOR"));
    }

    // ─── crear: rol inexistente (catch IllegalArgumentException) ─────────────
    @Test
    void crearUsuarioInterno_Falla_RolInexistente() {
        UsuarioInternoRequestDTO req = new UsuarioInternoRequestDTO();
        req.setNombre("Interno");
        req.setCorreo("interno@test.com");
        req.setPassword("P@ssw0rd1");
        req.setRol("SUPERUSUARIO");

        assertThrows(IllegalArgumentException.class, () ->
            usuarioInternoService.crearUsuarioInterno(req, "ADMINISTRADOR"));
    }

    // ─── obtener por id: éxito ────────────────────────────────────────────────
    @Test
    void obtenerUsuarioInternoPorId_Exito() {
        Usuario u = Usuario.builder().id(1L).rol(Rol.EMPLEADO).activo(true).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));

        UsuarioInternoResponseDTO resp = usuarioInternoService.obtenerUsuarioInternoPorId(1L, "ADMINISTRADOR");
        assertNotNull(resp);
    }

    // ─── obtener por id: no encontrado ───────────────────────────────────────
    @Test
    void obtenerUsuarioInternoPorId_Falla_NoEncontrado() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(UsuarioNoEncontradoException.class, () ->
            usuarioInternoService.obtenerUsuarioInternoPorId(99L, "ADMINISTRADOR"));
    }

    // ─── obtener por id: usuario es CLIENTE (buscarVigente lo rechaza) ────────
    @Test
    void obtenerUsuarioInternoPorId_Falla_EsCliente() {
        Usuario u = Usuario.builder().id(1L).rol(Rol.CLIENTE).activo(true).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));
        assertThrows(UsuarioNoEncontradoException.class, () ->
            usuarioInternoService.obtenerUsuarioInternoPorId(1L, "ADMINISTRADOR"));
    }

    // ─── obtener por id: usuario eliminado ───────────────────────────────────
    @Test
    void obtenerUsuarioInternoPorId_Falla_Eliminado() {
        Usuario u = Usuario.builder().id(1L).rol(Rol.EMPLEADO).activo(false).eliminado(true).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));
        assertThrows(UsuarioNoEncontradoException.class, () ->
            usuarioInternoService.obtenerUsuarioInternoPorId(1L, "ADMINISTRADOR"));
    }

    // ─── listar: filtra clientes y eliminados ─────────────────────────────────
    @Test
    void listarUsuariosInternos_FiltraClientesYEliminados() {
        Usuario emp = Usuario.builder().id(1L).rol(Rol.EMPLEADO).eliminado(false).build();
        Usuario cli = Usuario.builder().id(2L).rol(Rol.CLIENTE).eliminado(false).build(); // filtrado
        Usuario del = Usuario.builder().id(3L).rol(Rol.GERENTE).eliminado(true).build();  // filtrado
        when(usuarioRepository.findAll()).thenReturn(List.of(emp, cli, del));

        List<UsuarioInternoResponseDTO> result = usuarioInternoService.listarUsuariosInternos("ADMINISTRADOR");
        assertEquals(1, result.size());
    }

    // ─── actualizar: todos los campos presentes ───────────────────────────────
    @Test
    void actualizarUsuarioInterno_TodosCampos_Exito() {
        Usuario u = Usuario.builder().id(1L).nombre("Viejo").correo("viejo@test.com")
                .rol(Rol.EMPLEADO).activo(true).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));
        when(usuarioRepository.existsByCorreo("nuevo@test.com")).thenReturn(false);
        when(usuarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        UsuarioInternoUpdateDTO req = new UsuarioInternoUpdateDTO();
        req.setNombre("Nuevo");
        req.setCorreo("nuevo@test.com");
        req.setPassword("P@ssw0rd2");
        req.setRol("GERENTE");

        UsuarioInternoResponseDTO resp = usuarioInternoService.actualizarUsuarioInterno(1L, req, "ADMINISTRADOR");
        assertEquals("Nuevo", resp.getNombre());
    }

    // ─── actualizar: ningún campo (todos null/blank) ──────────────────────────
    @Test
    void actualizarUsuarioInterno_SinCambios_Exito() {
        Usuario u = Usuario.builder().id(1L).nombre("Viejo").correo("viejo@test.com")
                .rol(Rol.EMPLEADO).activo(true).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));
        when(usuarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        UsuarioInternoUpdateDTO req = new UsuarioInternoUpdateDTO();
        // todos null → ramas false de todos los if

        UsuarioInternoResponseDTO resp = usuarioInternoService.actualizarUsuarioInterno(1L, req, "ADMINISTRADOR");
        assertNotNull(resp);
    }

    // ─── actualizar: correo duplicado de otro usuario ─────────────────────────
    @Test
    void actualizarUsuarioInterno_Falla_CorreoDuplicado() {
        Usuario u = Usuario.builder().id(1L).correo("viejo@test.com")
                .rol(Rol.EMPLEADO).activo(true).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));
        when(usuarioRepository.existsByCorreo("ocupado@test.com")).thenReturn(true);

        UsuarioInternoUpdateDTO req = new UsuarioInternoUpdateDTO();
        req.setCorreo("ocupado@test.com");

        assertThrows(UsuarioYaExisteException.class, () ->
            usuarioInternoService.actualizarUsuarioInterno(1L, req, "ADMINISTRADOR"));
    }

    // ─── actualizar: password inválida ────────────────────────────────────────
    @Test
    void actualizarUsuarioInterno_Falla_PasswordInvalida() {
        Usuario u = Usuario.builder().id(1L).correo("interno@test.com")
                .rol(Rol.EMPLEADO).activo(true).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));

        UsuarioInternoUpdateDTO req = new UsuarioInternoUpdateDTO();
        req.setPassword("1234"); // inválida

        assertThrows(CredencialesInvalidasException.class, () ->
            usuarioInternoService.actualizarUsuarioInterno(1L, req, "ADMINISTRADOR"));
    }

    // ─── desactivar: éxito ───────────────────────────────────────────────────
    @Test
    void desactivarUsuarioInterno_Exito() {
        Usuario u = Usuario.builder().id(1L).rol(Rol.EMPLEADO).activo(true).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));
        when(usuarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        UsuarioInternoResponseDTO resp = usuarioInternoService.desactivarUsuarioInterno(1L, "ADMINISTRADOR");
        assertFalse(resp.getActivo());
    }

    // ─── eliminar: éxito ─────────────────────────────────────────────────────
    @Test
    void eliminarUsuarioInterno_Exito() {
        Usuario u = Usuario.builder().id(1L).rol(Rol.EMPLEADO).activo(true).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));
        when(usuarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        UsuarioInternoResponseDTO resp = usuarioInternoService.eliminarUsuarioInterno(1L, "ADMINISTRADOR");
        assertFalse(resp.getActivo());
        assertTrue(resp.getEliminado());
    }
}
