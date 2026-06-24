package com.ecomarket.usuarios.service;

import com.ecomarket.usuarios.dto.RolPermisosRequestDTO;
import com.ecomarket.usuarios.dto.RolPermisosResponseDTO;
import com.ecomarket.usuarios.dto.VerificacionAccesoResponseDTO;
import com.ecomarket.usuarios.exception.AccesoNoAutorizadoException;
import com.ecomarket.usuarios.exception.UsuarioNoEncontradoException;
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
public class RolPermisoServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private RolPermisoService rolPermisoService;

    // ─── asignarRoles: éxito EMPLEADO ─────────────────────────────────────────
    @Test
    void asignarRolesPermisos_Empleado_Exito() {
        RolPermisosRequestDTO req = new RolPermisosRequestDTO();
        req.setRol("EMPLEADO");
        req.setPermisos(List.of("VENTAS", "SOPORTE"));
        req.setModificadoPor("Admin");

        Usuario u = Usuario.builder().id(1L).rol(Rol.EMPLEADO).activo(true).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));
        when(usuarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        RolPermisosResponseDTO resp = rolPermisoService.asignarRolesPermisos(1L, req, "ADMINISTRADOR");
        assertEquals("EMPLEADO", resp.getRol());
        assertEquals("OPERATIVO", resp.getNivelAcceso());
    }

    // ─── asignarRoles: éxito GERENTE (nivel GESTION) ─────────────────────────
    @Test
    void asignarRolesPermisos_Gerente_Exito() {
        RolPermisosRequestDTO req = new RolPermisosRequestDTO();
        req.setRol("GERENTE");
        req.setPermisos(List.of("REPORTES", "INVENTARIO"));
        req.setModificadoPor("Admin");

        Usuario u = Usuario.builder().id(1L).rol(Rol.EMPLEADO).activo(true).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));
        when(usuarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        RolPermisosResponseDTO resp = rolPermisoService.asignarRolesPermisos(1L, req, "ADMINISTRADOR");
        assertEquals("GERENTE", resp.getRol());
        assertEquals("GESTION", resp.getNivelAcceso());
    }

    // ─── asignarRoles: éxito ADMINISTRADOR (nivel TOTAL) ─────────────────────
    @Test
    void asignarRolesPermisos_Administrador_Exito() {
        RolPermisosRequestDTO req = new RolPermisosRequestDTO();
        req.setRol("ADMINISTRADOR");
        req.setPermisos(List.of("USUARIOS", "CONFIGURACION"));
        req.setModificadoPor("Admin");

        Usuario u = Usuario.builder().id(1L).rol(Rol.EMPLEADO).activo(true).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));
        when(usuarioRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        RolPermisosResponseDTO resp = rolPermisoService.asignarRolesPermisos(1L, req, "ADMINISTRADOR");
        assertEquals("ADMINISTRADOR", resp.getRol());
        assertEquals("TOTAL", resp.getNivelAcceso());
    }

    // ─── asignarRoles: sin permiso admin ─────────────────────────────────────
    @Test
    void asignarRolesPermisos_Falla_NoAdmin() {
        RolPermisosRequestDTO req = new RolPermisosRequestDTO();
        assertThrows(AccesoNoAutorizadoException.class, () ->
            rolPermisoService.asignarRolesPermisos(1L, req, "EMPLEADO"));
    }

    // ─── asignarRoles: rolSolicitante null ────────────────────────────────────
    @Test
    void asignarRolesPermisos_Falla_RolNull() {
        RolPermisosRequestDTO req = new RolPermisosRequestDTO();
        assertThrows(AccesoNoAutorizadoException.class, () ->
            rolPermisoService.asignarRolesPermisos(1L, req, null));
    }

    // ─── asignarRoles: usuario es CLIENTE ────────────────────────────────────
    @Test
    void asignarRolesPermisos_Falla_UsuarioCliente() {
        RolPermisosRequestDTO req = new RolPermisosRequestDTO();
        Usuario u = Usuario.builder().id(1L).rol(Rol.CLIENTE).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));
        assertThrows(UsuarioNoEncontradoException.class, () ->
            rolPermisoService.asignarRolesPermisos(1L, req, "ADMINISTRADOR"));
    }

    // ─── asignarRoles: usuario no encontrado ─────────────────────────────────
    @Test
    void asignarRolesPermisos_Falla_NoEncontrado() {
        RolPermisosRequestDTO req = new RolPermisosRequestDTO();
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(UsuarioNoEncontradoException.class, () ->
            rolPermisoService.asignarRolesPermisos(99L, req, "ADMINISTRADOR"));
    }

    // ─── asignarRoles: rol inválido ───────────────────────────────────────────
    @Test
    void asignarRolesPermisos_Falla_RolInvalido() {
        RolPermisosRequestDTO req = new RolPermisosRequestDTO();
        req.setRol("SUPERADMIN");
        req.setPermisos(List.of("VENTAS"));
        req.setModificadoPor("Admin");

        Usuario u = Usuario.builder().id(1L).rol(Rol.EMPLEADO).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));
        assertThrows(IllegalArgumentException.class, () ->
            rolPermisoService.asignarRolesPermisos(1L, req, "ADMINISTRADOR"));
    }

    // ─── asignarRoles: rol CLIENTE como destino ───────────────────────────────
    @Test
    void asignarRolesPermisos_Falla_RolDestinoCliente() {
        RolPermisosRequestDTO req = new RolPermisosRequestDTO();
        req.setRol("CLIENTE");
        req.setPermisos(List.of("VENTAS"));
        req.setModificadoPor("Admin");

        Usuario u = Usuario.builder().id(1L).rol(Rol.EMPLEADO).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));
        assertThrows(IllegalArgumentException.class, () ->
            rolPermisoService.asignarRolesPermisos(1L, req, "ADMINISTRADOR"));
    }

    // ─── asignarRoles: módulo/permiso inválido ────────────────────────────────
    @Test
    void asignarRolesPermisos_Falla_ModuloInvalido() {
        RolPermisosRequestDTO req = new RolPermisosRequestDTO();
        req.setRol("EMPLEADO");
        req.setPermisos(List.of("MODULO_INEXISTENTE"));
        req.setModificadoPor("Admin");

        Usuario u = Usuario.builder().id(1L).rol(Rol.EMPLEADO).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));
        assertThrows(IllegalArgumentException.class, () ->
            rolPermisoService.asignarRolesPermisos(1L, req, "ADMINISTRADOR"));
    }

    // ─── obtenerRoles: éxito ─────────────────────────────────────────────────
    @Test
    void obtenerRolesPermisos_Exito() {
        Usuario u = Usuario.builder().id(1L).rol(Rol.EMPLEADO)
                .permisos(List.of("VENTAS")).activo(true).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));

        RolPermisosResponseDTO resp = rolPermisoService.obtenerRolesPermisos(1L, "ADMINISTRADOR");
        assertTrue(resp.getPermisos().contains("VENTAS"));
    }

    // ─── obtenerRoles: sin permisos (permisos null → List.of()) ──────────────
    @Test
    void obtenerRolesPermisos_SinPermisos_Exito() {
        Usuario u = Usuario.builder().id(1L).rol(Rol.EMPLEADO)
                .permisos(null).activo(true).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));

        RolPermisosResponseDTO resp = rolPermisoService.obtenerRolesPermisos(1L, "ADMINISTRADOR");
        assertTrue(resp.getPermisos().isEmpty());
    }

    // ─── verificarAcceso: tiene acceso ────────────────────────────────────────
    @Test
    void verificarAcceso_TieneAcceso() {
        Usuario u = Usuario.builder().id(1L).rol(Rol.EMPLEADO)
                .permisos(List.of("VENTAS")).activo(true).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));

        VerificacionAccesoResponseDTO resp = rolPermisoService.verificarAcceso(1L, "VENTAS");
        assertTrue(resp.getAccesoPermitido());
        assertEquals("Acceso permitido al módulo solicitado", resp.getMensaje());
    }

    // ─── verificarAcceso: no tiene el permiso ─────────────────────────────────
    @Test
    void verificarAcceso_NoTienePermiso() {
        Usuario u = Usuario.builder().id(1L).rol(Rol.EMPLEADO)
                .permisos(List.of("VENTAS")).activo(true).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));

        VerificacionAccesoResponseDTO resp = rolPermisoService.verificarAcceso(1L, "USUARIOS");
        assertFalse(resp.getAccesoPermitido());
        assertEquals("Acceso bloqueado: el usuario no tiene permiso para este módulo", resp.getMensaje());
    }

    // ─── verificarAcceso: usuario inactivo ────────────────────────────────────
    @Test
    void verificarAcceso_UsuarioInactivo() {
        Usuario u = Usuario.builder().id(1L).rol(Rol.EMPLEADO)
                .permisos(List.of("VENTAS")).activo(false).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));

        VerificacionAccesoResponseDTO resp = rolPermisoService.verificarAcceso(1L, "VENTAS");
        assertFalse(resp.getAccesoPermitido());
    }

    // ─── verificarAcceso: permisos null (rama null) ───────────────────────────
    @Test
    void verificarAcceso_PermisosNull() {
        Usuario u = Usuario.builder().id(1L).rol(Rol.EMPLEADO)
                .permisos(null).activo(true).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));

        VerificacionAccesoResponseDTO resp = rolPermisoService.verificarAcceso(1L, "VENTAS");
        assertFalse(resp.getAccesoPermitido());
    }

    // ─── verificarAcceso: módulo null/blank ───────────────────────────────────
    @Test
    void verificarAcceso_Falla_ModuloBlank() {
        Usuario u = Usuario.builder().id(1L).rol(Rol.EMPLEADO).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));
        assertThrows(IllegalArgumentException.class, () ->
            rolPermisoService.verificarAcceso(1L, ""));
    }

    // ─── verificarAcceso: módulo inválido ─────────────────────────────────────
    @Test
    void verificarAcceso_Falla_ModuloInvalido() {
        Usuario u = Usuario.builder().id(1L).rol(Rol.EMPLEADO).eliminado(false).build();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));
        assertThrows(IllegalArgumentException.class, () ->
            rolPermisoService.verificarAcceso(1L, "MODULO_FALSO"));
    }
}
