package com.ecomarket.usuarios.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PerfilClienteResponseDTO {

    private Long id;
    private String nombre;
    private String correo;
    private String telefono;
    private String direccionEnvio;
    private String medioPago;
    private String rol;
    private Boolean activo;
    private LocalDateTime fechaRegistro;
}
