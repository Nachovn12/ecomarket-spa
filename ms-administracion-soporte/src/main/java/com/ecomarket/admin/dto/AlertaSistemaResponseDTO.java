package com.ecomarket.admin.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AlertaSistemaResponseDTO {

    private Long idAlerta;
    private String microservicio;
    private String tipoAlerta;
    private String descripcion;
    private Boolean resuelta;
    private LocalDateTime fechaGeneracion;
    private LocalDateTime fechaResolucion;
}
