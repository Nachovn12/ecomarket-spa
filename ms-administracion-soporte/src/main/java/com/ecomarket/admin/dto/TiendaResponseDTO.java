package com.ecomarket.admin.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
public class TiendaResponseDTO {

    private Long idTienda;
    private String nombre;
    private String ciudad;
    private LocalTime horarioApertura;
    private LocalTime horarioCierre;
    private String politicasLocales;
    private Boolean activa;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
