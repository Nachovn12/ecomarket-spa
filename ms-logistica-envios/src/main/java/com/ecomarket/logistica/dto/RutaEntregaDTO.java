package com.ecomarket.logistica.dto;

import com.ecomarket.logistica.model.enums.EstadoRuta;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class RutaEntregaDTO {

    @Positive(message = "El ID debe ser positivo")
    private Long id;

    private EstadoRuta estado;
    private LocalDateTime fechaCreacion;

    public RutaEntregaDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public EstadoRuta getEstado() { return estado; }
    public void setEstado(EstadoRuta estado) { this.estado = estado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
