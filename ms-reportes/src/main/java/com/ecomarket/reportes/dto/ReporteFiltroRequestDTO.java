package com.ecomarket.reportes.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class ReporteFiltroRequestDTO {

    @NotNull(message = "El id de tienda es obligatorio")
    @Positive(message = "El id de tienda debe ser positivo")
    private Long idTienda;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fechaFin;

    public Long getIdTienda() { return idTienda; }
    public void setIdTienda(Long idTienda) { this.idTienda = idTienda; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
}