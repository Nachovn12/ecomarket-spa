package com.ecomarket.usuarios.service;

import com.ecomarket.usuarios.dto.RolPermisosRequestDTO;
import com.ecomarket.usuarios.dto.RolPermisosResponseDTO;
import com.ecomarket.usuarios.dto.VerificacionAccesoResponseDTO;
import com.ecomarket.usuarios.entity.Usuario;
import com.ecomarket.usuarios.exception.AccesoNoAutorizadoException;
import com.ecomarket.usuarios.exception.UsuarioNoEncontradoException;
import com.ecomarket.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RolPermisoService {

    private static final String ROL_ADMINISTRADOR = "ADMINISTRADOR";

    private static final Set<String> ROLES_INTERNOS = Set.of(
            "EMPLEADO",
            "GERENTE",
            "ADMINISTRADOR"
    );

    private static final Set<String> MODULOS_VALIDOS = Set.of(
            "USUARIOS",
            "ROLES_PERMISOS",
            "INVENTARIO",
            "INVENTARIO_CONSULTA",
            "PEDIDOS",
            "VENTAS",
            "REPORTES",
            "LOGISTICA",
            "SOPORTE",
            "FACTURACION",
            "DEVOLUCIONES",
            "TIENDAS",
            "ABASTECIMIENTO",
            "CONFIGURACION"
    );

    private static final Map<String, String> NIVELES_ACCESO = Map.of(
            "EMPLEADO", "OPERATIVO",
            "GERENTE", "GESTION",
            "ADMINISTRADOR", "TOTAL"
    );

    private final UsuarioRepository usuarioRepository;

    public RolPermisosResponseDTO asignarRolesPermisos(Long id, RolPermisosRequestDTO request, String rolSolicitante) {
        validarPermisoAdministrador(rolSolicitante);

        Usuario usuario = buscarUsuarioInternoVigente(id);

        String rolNormalizado = normalizarRol(request.getRol());
        List<String> permisosNormalizados = normalizarPermisos(request.getPermisos());

        usuario.setRol(rolNormalizado);
        usuario.setNivelAcceso(NIVELES_ACCESO.get(rolNormalizado));
        usuario.setPermisos(String.join(",", permisosNormalizados));
        usuario.setModificadoPor(request.getModificadoPor().trim());
        usuario.setFechaModificacionAcceso(LocalDateTime.now());

        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        return mapearRespuesta(usuarioActualizado);
    }

    public RolPermisosResponseDTO obtenerRolesPermisos(Long id, String rolSolicitante) {
        validarPermisoAdministrador(rolSolicitante);

        Usuario usuario = buscarUsuarioInternoVigente(id);

        return mapearRespuesta(usuario);
    }

    public VerificacionAccesoResponseDTO verificarAcceso(Long id, String modulo) {
        Usuario usuario = buscarUsuarioInternoVigente(id);
        String moduloNormalizado = normalizarModulo(modulo);

        boolean tieneAcceso = Boolean.TRUE.equals(usuario.getActivo())
                && !Boolean.TRUE.equals(usuario.getEliminado())
                && obtenerPermisos(usuario).contains(moduloNormalizado);

        String mensaje = tieneAcceso
                ? "Acceso permitido al módulo solicitado"
                : "Acceso bloqueado: el usuario no tiene permiso para este módulo";

        return VerificacionAccesoResponseDTO.builder()
                .idUsuario(usuario.getId())
                .rol(usuario.getRol())
                .nivelAcceso(usuario.getNivelAcceso())
                .modulo(moduloNormalizado)
                .accesoPermitido(tieneAcceso)
                .mensaje(mensaje)
                .build();
    }

    private Usuario buscarUsuarioInternoVigente(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario interno no encontrado"));

        if ("CLIENTE".equalsIgnoreCase(usuario.getRol()) || Boolean.TRUE.equals(usuario.getEliminado())) {
            throw new UsuarioNoEncontradoException("Usuario interno no encontrado");
        }

        return usuario;
    }

    private void validarPermisoAdministrador(String rolSolicitante) {
        if (rolSolicitante == null || !ROL_ADMINISTRADOR.equals(rolSolicitante.trim().toUpperCase())) {
            throw new AccesoNoAutorizadoException("No tiene permisos para asignar roles y permisos");
        }
    }

    private String normalizarRol(String rol) {
        String rolNormalizado = rol.trim().toUpperCase();

        if (!ROLES_INTERNOS.contains(rolNormalizado)) {
            throw new IllegalArgumentException("Rol interno no válido");
        }

        return rolNormalizado;
    }

    private List<String> normalizarPermisos(List<String> permisos) {
        return permisos.stream()
                .map(permiso -> permiso.trim().toUpperCase())
                .peek(this::validarModulo)
                .distinct()
                .toList();
    }

    private String normalizarModulo(String modulo) {
        if (modulo == null || modulo.isBlank()) {
            throw new IllegalArgumentException("Debe indicar el módulo a validar");
        }

        String moduloNormalizado = modulo.trim().toUpperCase();
        validarModulo(moduloNormalizado);

        return moduloNormalizado;
    }

    private void validarModulo(String modulo) {
        if (!MODULOS_VALIDOS.contains(modulo)) {
            throw new IllegalArgumentException("Módulo o permiso no válido: " + modulo);
        }
    }

    private List<String> obtenerPermisos(Usuario usuario) {
        if (usuario.getPermisos() == null || usuario.getPermisos().isBlank()) {
            return List.of();
        }

        return Arrays.stream(usuario.getPermisos().split(","))
                .map(String::trim)
                .filter(permiso -> !permiso.isBlank())
                .toList();
    }

    private RolPermisosResponseDTO mapearRespuesta(Usuario usuario) {
        return RolPermisosResponseDTO.builder()
                .idUsuario(usuario.getId())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol())
                .nivelAcceso(usuario.getNivelAcceso())
                .permisos(obtenerPermisos(usuario))
                .modificadoPor(usuario.getModificadoPor())
                .fechaModificacionAcceso(usuario.getFechaModificacionAcceso())
                .build();
    }
}