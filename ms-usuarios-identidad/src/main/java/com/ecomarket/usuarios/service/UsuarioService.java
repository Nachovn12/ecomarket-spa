package com.ecomarket.usuarios.service;

import com.ecomarket.usuarios.dto.UsuarioRequestDTO;
import com.ecomarket.usuarios.dto.UsuarioResponseDTO;
import com.ecomarket.usuarios.entity.Usuario;
import com.ecomarket.usuarios.exception.UsuarioYaExisteException;
import com.ecomarket.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioResponseDTO registrarCliente(UsuarioRequestDTO request) {
        String correoNormalizado = request.getCorreo().trim().toLowerCase();

        if (usuarioRepository.existsByCorreo(correoNormalizado)) {
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

        return mapearRespuesta(usuarioGuardado);
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
}