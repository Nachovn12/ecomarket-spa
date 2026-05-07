package com.ecomarket.usuarios.service;

import com.ecomarket.usuarios.dto.LoginRequestDTO;
import com.ecomarket.usuarios.dto.LoginResponseDTO;
import com.ecomarket.usuarios.entity.Usuario;
import com.ecomarket.usuarios.exception.CredencialesInvalidasException;
import com.ecomarket.usuarios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    public LoginResponseDTO iniciarSesion(LoginRequestDTO request) {
        String correoNormalizado = request.getCorreo().trim().toLowerCase();

        Usuario usuario = usuarioRepository.findByCorreo(correoNormalizado)
                .orElseThrow(() -> new CredencialesInvalidasException("Correo o contraseña inválidos"));

        if (!usuario.getPassword().equals(request.getPassword())) {
            throw new CredencialesInvalidasException("Correo o contraseña inválidos");
        }

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new CredencialesInvalidasException("Correo o contraseña inválidos");
        }

        return LoginResponseDTO.builder()
                .idUsuario(usuario.getId())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .rol(usuario.getRol())
                .tokenSesion(UUID.randomUUID().toString())
                .mensaje("Inicio de sesión exitoso")
                .funcionalidadesDisponibles(List.of(
                        "gestionar perfil",
                        "gestionar pedidos",
                        "consultar historial de compras",
                        "gestionar carrito"
                ))
                .build();
    }
}