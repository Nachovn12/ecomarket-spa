package com.ecomarket.admin.dto;

import com.ecomarket.admin.entity.EstadoTicket;
import com.ecomarket.admin.entity.PrioridadTicket;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TicketSoporteResponseDTO {

    private Long idTicket;
    private String asunto;
    private String descripcion;
    private String nombreContacto;
    private String correoContacto;
    private PrioridadTicket prioridad;
    private EstadoTicket estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
