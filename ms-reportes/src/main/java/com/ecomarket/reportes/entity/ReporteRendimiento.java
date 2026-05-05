package com.ecomarket.reportes.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reportes_rendimiento")
public class ReporteRendimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tiendaId;
    private Double ingresos;
    private Double gastos;
    private Double margenGanancia;
    private LocalDateTime fechaReporte;

    public ReporteRendimiento() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTiendaId() { return tiendaId; }
    public void setTiendaId(Long tiendaId) { this.tiendaId = tiendaId; }

    public Double getIngresos() { return ingresos; }
    public void setIngresos(Double ingresos) { this.ingresos = ingresos; }

    public Double getGastos() { return gastos; }
    public void setGastos(Double gastos) { this.gastos = gastos; }

    public Double getMargenGanancia() { return margenGanancia; }
    public void setMargenGanancia(Double margenGanancia) { this.margenGanancia = margenGanancia; }

    public LocalDateTime getFechaReporte() { return fechaReporte; }
    public void setFechaReporte(LocalDateTime fechaReporte) { this.fechaReporte = fechaReporte; }
}