package com.ecomarket.admin.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RespuestaSoporteResponseDTO {

    private Long idRespuesta;
    private Long idTicket;
    private String mensaje;
    private String respondidoPor;
    private LocalDateTime fechaRespuesta;
}
