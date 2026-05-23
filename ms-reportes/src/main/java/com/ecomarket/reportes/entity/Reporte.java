package com.ecomarket.reportes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reportes")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El tipo de reporte es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoReporte tipo;

    @Positive(message = "El id de tienda debe ser positivo")
    private Long idTienda;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    private FormatoExportacion formatoExportacion;

    @Size(max = 100, message = "El usuario generador no puede superar los 100 caracteres")
    private String generadoPor;

    private LocalDateTime fechaGeneracion;

    @PrePersist
    public void prePersist() {
        this.fechaGeneracion = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoReporte getTipo() { return tipo; }
    public void setTipo(TipoReporte tipo) { this.tipo = tipo; }

    public Long getIdTienda() { return idTienda; }
    public void setIdTienda(Long idTienda) { this.idTienda = idTienda; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public FormatoExportacion getFormatoExportacion() { return formatoExportacion; }
    public void setFormatoExportacion(FormatoExportacion formatoExportacion) { this.formatoExportacion = formatoExportacion; }

    public String getGeneradoPor() { return generadoPor; }
    public void setGeneradoPor(String generadoPor) { this.generadoPor = generadoPor; }

    public LocalDateTime getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(LocalDateTime fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }
}