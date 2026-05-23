package com.ecomarket.reportes.dto;

import java.time.LocalDate;

public class ReporteRendimientoDTO {

    private Long idTienda;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Double ventasPorTienda;
    private Integer pedidosEntregados;
    private Integer stockBajo;
    private Double rendimientoOperativo;

    public Long getIdTienda() { return idTienda; }
    public void setIdTienda(Long idTienda) { this.idTienda = idTienda; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public Double getVentasPorTienda() { return ventasPorTienda; }
    public void setVentasPorTienda(Double ventasPorTienda) { this.ventasPorTienda = ventasPorTienda; }

    public Integer getPedidosEntregados() { return pedidosEntregados; }
    public void setPedidosEntregados(Integer pedidosEntregados) { this.pedidosEntregados = pedidosEntregados; }

    public Integer getStockBajo() { return stockBajo; }
    public void setStockBajo(Integer stockBajo) { this.stockBajo = stockBajo; }

    public Double getRendimientoOperativo() { return rendimientoOperativo; }
    public void setRendimientoOperativo(Double rendimientoOperativo) { this.rendimientoOperativo = rendimientoOperativo; }
}