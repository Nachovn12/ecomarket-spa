package com.ecomarket.usuarios.service;

import com.ecomarket.usuarios.dto.UsuarioInternoRequestDTO;
import com.ecomarket.usuarios.dto.UsuarioInternoResponseDTO;
import com.ecomarket.usuarios.dto.UsuarioInternoUpdateDTO;
import com.ecomarket.usuarios.model.Rol;
import com.ecomarket.usuarios.model.Usuario;
import com.ecomarket.usuarios.exception.AccesoNoAutorizadoException;
import com.ecomarket.usuarios.exception.UsuarioNoEncontradoException;
import com.ecomarket.usuarios.exception.UsuarioYaExisteException;
import com.ecomarket.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioInternoService {

    private static final String ROL_ADMINISTRADOR = "ADMINISTRADOR";
    private static final Set<Rol> ROLES_INTERNOS = Set.of(Rol.EMPLEADO, Rol.GERENTE, Rol.ADMINISTRADOR);

    private final UsuarioRepository usuarioRepository;

    public UsuarioInternoResponseDTO crearUsuarioInterno(UsuarioInternoRequestDTO request, String rolSolicitante) {
        validarPermisoAdministrador(rolSolicitante);

        String correoNormalizado = request.getCorreo().trim().toLowerCase();
        Rol rolNormalizado = normalizarRolInterno(request.getRol());

        if (usuarioRepository.existsByCorreo(correoNormalizado)) {
            throw new UsuarioYaExisteException("Ya existe una cuenta registrada con este correo");
        }

        com.ecomarket.usuarios.util.PasswordValidador.validar(request.getPassword());

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre().trim())
                .correo(correoNormalizado)
                .password(request.getPassword())
                .rol(rolNormalizado)
                .activo(true)
                .eliminado(false)
                .fechaRegistro(LocalDateTime.now())
                .build();

        return mapearRespuesta(usuarioRepository.save(usuario));
    }

    public UsuarioInternoResponseDTO obtenerUsuarioInternoPorId(Long id, String rolSolicitante) {
        validarPermisoAdministrador(rolSolicitante);
        return mapearRespuesta(buscarUsuarioInternoVigente(id));
    }

    public List<UsuarioInternoResponseDTO> listarUsuariosInternos(String rolSolicitante) {
        validarPermisoAdministrador(rolSolicitante);

        return usuarioRepository.findAll()
                .stream()
                .filter(usuario -> !Rol.CLIENTE.equals(usuario.getRol()))
                .filter(usuario -> !Boolean.TRUE.equals(usuario.getEliminado()))
                .map(this::mapearRespuesta)
                .toList();
    }

    public UsuarioInternoResponseDTO actualizarUsuarioInterno(Long id, UsuarioInternoUpdateDTO request, String rolSolicitante) {
        validarPermisoAdministrador(rolSolicitante);

        Usuario usuario = buscarUsuarioInternoVigente(id);

        if (request.getNombre() != null && !request.getNombre().isBlank()) {
            usuario.setNombre(request.getNombre().trim());
        }

        if (request.getCorreo() != null && !request.getCorreo().isBlank()) {
            String correoNormalizado = request.getCorreo().trim().toLowerCase();

            if (!usuario.getCorreo().equals(correoNormalizado) && usuarioRepository.existsByCorreo(correoNormalizado)) {
                throw new UsuarioYaExisteException("Ya existe una cuenta registrada con este correo");
            }

            usuario.setCorreo(correoNormalizado);
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            com.ecomarket.usuarios.util.PasswordValidador.validar(request.getPassword());
            usuario.setPassword(request.getPassword());
        }

        if (request.getRol() != null && !request.getRol().isBlank()) {
            usuario.setRol(normalizarRolInterno(request.getRol()));
        }

        return mapearRespuesta(usuarioRepository.save(usuario));
    }

    public UsuarioInternoResponseDTO desactivarUsuarioInterno(Long id, String rolSolicitante) {
        validarPermisoAdministrador(rolSolicitante);

        Usuario usuario = buscarUsuarioInternoVigente(id);
        usuario.setActivo(false);

        return mapearRespuesta(usuarioRepository.save(usuario));
    }

    public UsuarioInternoResponseDTO eliminarUsuarioInterno(Long id, String rolSolicitante) {
        validarPermisoAdministrador(rolSolicitante);

        Usuario usuario = buscarUsuarioInternoVigente(id);
        usuario.setActivo(false);
        usuario.setEliminado(true);

        return mapearRespuesta(usuarioRepository.save(usuario));
    }

    private Usuario buscarUsuarioInternoVigente(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario interno no encontrado"));

        if (Rol.CLIENTE.equals(usuario.getRol()) || Boolean.TRUE.equals(usuario.getEliminado())) {
            throw new UsuarioNoEncontradoException("Usuario interno no encontrado");
        }

        return usuario;
    }

    private void validarPermisoAdministrador(String rolSolicitante) {
        if (rolSolicitante == null || !ROL_ADMINISTRADOR.equals(rolSolicitante.trim().toUpperCase())) {
            throw new AccesoNoAutorizadoException("No tiene permisos para gestionar usuarios internos");
        }
    }

    private Rol normalizarRolInterno(String rol) {
        String rolNormalizado = rol.trim().toUpperCase();
        Rol rolEnum;
        try {
            rolEnum = Rol.valueOf(rolNormalizado);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol interno no válido: " + rol);
        }
        if (!ROLES_INTERNOS.contains(rolEnum)) {
            throw new IllegalArgumentException("Rol interno no válido: " + rol);
        }
        return rolEnum;
    }

    private UsuarioInternoResponseDTO mapearRespuesta(Usuario usuario) {
        return UsuarioInternoResponseDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol().name())
                .activo(usuario.getActivo())
                .eliminado(usuario.getEliminado())
                .fechaRegistro(usuario.getFechaRegistro())
                .build();
    }
}
