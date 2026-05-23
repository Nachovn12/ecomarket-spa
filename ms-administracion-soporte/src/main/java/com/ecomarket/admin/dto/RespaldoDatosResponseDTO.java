package com.ecomarket.admin.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RespaldoDatosResponseDTO {

    private Long idRespaldo;
    private String origenDatos;
    private String frecuencia;
    private String responsable;
    private String estado;
    private String resultado;
    private LocalDateTime fechaProgramada;
    private LocalDateTime fechaEjecucion;
    private LocalDateTime fechaRestauracion;
}
