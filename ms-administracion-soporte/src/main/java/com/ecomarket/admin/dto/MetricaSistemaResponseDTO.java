package com.ecomarket.admin.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MetricaSistemaResponseDTO {

    private Long idMetrica;
    private String microservicio;
    private Boolean disponible;
    private Long tiempoRespuestaMs;
    private Integer erroresDetectados;
    private LocalDateTime fechaRegistro;
}
