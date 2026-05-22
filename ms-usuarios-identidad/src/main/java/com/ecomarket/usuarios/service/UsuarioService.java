package com.ecomarket.usuarios.service;

import com.ecomarket.usuarios.dto.ActualizarPerfilClienteRequestDTO;
import com.ecomarket.usuarios.dto.PerfilClienteResponseDTO;
import com.ecomarket.usuarios.dto.UsuarioRequestDTO;
import com.ecomarket.usuarios.dto.UsuarioResponseDTO;
import com.ecomarket.usuarios.entity.Usuario;
import com.ecomarket.usuarios.exception.UsuarioNoEncontradoException;
import com.ecomarket.usuarios.exception.UsuarioYaExisteException;
import com.ecomarket.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;

    public UsuarioResponseDTO registrarCliente(UsuarioRequestDTO request) {
        String correoNormalizado = request.getCorreo().trim().toLowerCase();

        if (usuarioRepository.existsByCorreo(correoNormalizado)) {
            log.warn("Intento de registro con correo ya existente: {}", correoNormalizado);
            throw new UsuarioYaExisteException("Ya existe una cuenta registrada con este correo");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre().trim())
                .correo(correoNormalizado)
                .password(request.getPassword())
                .rol("CLIENTE")
                .activo(true)
                .eliminado(false)
                .fechaRegistro(LocalDateTime.now())
                .build();

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        log.info("Cliente registrado correctamente con id {}", usuarioGuardado.getId());

        return mapearRespuesta(usuarioGuardado);
    }

    public PerfilClienteResponseDTO obtenerPerfilCliente(Long idCliente) {
        Usuario usuario = buscarClienteActivoPorId(idCliente);

        log.info("Perfil de cliente consultado. idCliente={}", idCliente);

        return mapearPerfilCliente(usuario);
    }

    public PerfilClienteResponseDTO actualizarPerfilCliente(Long idCliente, ActualizarPerfilClienteRequestDTO request) {
        Usuario usuario = buscarClienteActivoPorId(idCliente);

        String correoNormalizado = request.getCorreo().trim().toLowerCase();

        usuarioRepository.findByCorreo(correoNormalizado)
                .filter(usuarioExistente -> !usuarioExistente.getId().equals(idCliente))
                .ifPresent(usuarioExistente -> {
                    log.warn("Intento de actualizar perfil con correo ya usado. idCliente={}, correo={}", idCliente, correoNormalizado);
                    throw new UsuarioYaExisteException("Ya existe otra cuenta registrada con este correo");
                });

        usuario.setNombre(request.getNombre().trim());
        usuario.setCorreo(correoNormalizado);
        usuario.setTelefono(request.getTelefono().trim());
        usuario.setDireccionEnvio(request.getDireccionEnvio().trim());

        if (request.getMedioPago() != null && !request.getMedioPago().isBlank()) {
            usuario.setMedioPago(request.getMedioPago().trim());
        } else {
            usuario.setMedioPago(null);
        }

        usuario.setModificadoPor("CLIENTE_WEB");
        usuario.setFechaModificacionAcceso(LocalDateTime.now());

        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        log.info("Perfil de cliente actualizado correctamente. idCliente={}", idCliente);

        return mapearPerfilCliente(usuarioActualizado);
    }

    private Usuario buscarClienteActivoPorId(Long idCliente) {
        Usuario usuario = usuarioRepository.findById(idCliente)
                .orElseThrow(() -> {
                    log.warn("Cliente no encontrado. idCliente={}", idCliente);
                    return new UsuarioNoEncontradoException("Cliente no encontrado con id: " + idCliente);
                });

        if (Boolean.TRUE.equals(usuario.getEliminado())) {
            log.warn("Intento de acceder a cliente eliminado. idCliente={}", idCliente);
            throw new UsuarioNoEncontradoException("Cliente no encontrado con id: " + idCliente);
        }

        if (!"CLIENTE".equalsIgnoreCase(usuario.getRol())) {
            log.warn("Usuario consultado no corresponde a rol CLIENTE. idUsuario={}, rol={}", idCliente, usuario.getRol());
            throw new UsuarioNoEncontradoException("Cliente no encontrado con id: " + idCliente);
        }

        return usuario;
    }

    private UsuarioResponseDTO mapearRespuesta(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol())
                .activo(usuario.getActivo())
                .fechaRegistro(usuario.getFechaRegistro())
                .build();
    }

    private PerfilClienteResponseDTO mapearPerfilCliente(Usuario usuario) {
        return PerfilClienteResponseDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .telefono(usuario.getTelefono())
                .direccionEnvio(usuario.getDireccionEnvio())
                .medioPago(usuario.getMedioPago())
                .rol(usuario.getRol())
                .activo(usuario.getActivo())
                .fechaRegistro(usuario.getFechaRegistro())
                .build();
    }
}
