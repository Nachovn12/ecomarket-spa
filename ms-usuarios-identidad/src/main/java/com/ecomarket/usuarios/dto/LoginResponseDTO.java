package com.ecomarket.usuarios.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LoginResponseDTO {

    private Long idUsuario;
    private String nombre;
    private String correo;
    private String rol;
    private String tokenSesion;
    private String mensaje;
    private List<String> funcionalidadesDisponibles;
}