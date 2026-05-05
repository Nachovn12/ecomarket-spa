package com.ecomarket.reportes.dto;

import java.time.LocalDateTime;

public class ReporteVentasDTO {

    private Long id;
    private Double totalVentas;
    private Integer cantidadPedidos;
    private LocalDateTime fechaReporte;
    private String periodo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getTotalVentas() { return totalVentas; }
    public void setTotalVentas(Double totalVentas) { this.totalVentas = totalVentas; }

    public Integer getCantidadPedidos() { return cantidadPedidos; }
    public void setCantidadPedidos(Integer cantidadPedidos) { this.cantidadPedidos = cantidadPedidos; }

    public LocalDateTime getFechaReporte() { return fechaReporte; }
    public void setFechaReporte(LocalDateTime fechaReporte) { this.fechaReporte = fechaReporte; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }
}