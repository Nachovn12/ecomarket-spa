package com.ecomarket.admin.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AsignacionPersonalResponseDTO {

    private Long idAsignacion;
    private Long idUsuarioInterno;
    private Long idTienda;
    private String cargo;
    private Boolean activa;
    private LocalDateTime fechaAsignacion;
}
