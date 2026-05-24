package com.ecomarket.logistica.dto;

import com.ecomarket.logistica.model.enums.EstadoRuta;
import jakarta.validation.constraints.NotNull;

public class CambioEstadoRutaRequestDTO {

    @NotNull(message = "El estado de ruta es obligatorio")
    private EstadoRuta estado;

    public CambioEstadoRutaRequestDTO() {}

    public EstadoRuta getEstado() {
        return estado;
    }

    public void setEstado(EstadoRuta estado) {
        this.estado = estado;
    }
}
