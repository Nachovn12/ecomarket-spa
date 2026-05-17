package com.ecomarket.usuarios.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VerificacionAccesoResponseDTO {

    private Long idUsuario;
    private String rol;
    private String nivelAcceso;
    private String modulo;
    private Boolean accesoPermitido;
    private String mensaje;
}