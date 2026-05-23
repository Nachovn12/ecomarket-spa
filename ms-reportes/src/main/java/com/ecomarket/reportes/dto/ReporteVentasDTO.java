package com.ecomarket.reportes.dto;

import java.time.LocalDate;

public class ReporteVentasDTO {

    private Long idTienda;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Double ventasTotales;
    private Integer totalTransacciones;
    private Integer productosVendidos;

    public Long getIdTienda() { return idTienda; }
    public void setIdTienda(Long idTienda) { this.idTienda = idTienda; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public Double getVentasTotales() { return ventasTotales; }
    public void setVentasTotales(Double ventasTotales) { this.ventasTotales = ventasTotales; }

    public Integer getTotalTransacciones() { return totalTransacciones; }
    public void setTotalTransacciones(Integer totalTransacciones) { this.totalTransacciones = totalTransacciones; }

    public Integer getProductosVendidos() { return productosVendidos; }
    public void setProductosVendidos(Integer productosVendidos) { this.productosVendidos = productosVendidos; }
}