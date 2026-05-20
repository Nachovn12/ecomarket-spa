package com.ecomarket.usuarios.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UsuarioInternoResponseDTO {

    private Long id;
    private String nombre;
    private String correo;
    private String rol;
    private Boolean activo;
    private Boolean eliminado;
    private LocalDateTime fechaRegistro;
}