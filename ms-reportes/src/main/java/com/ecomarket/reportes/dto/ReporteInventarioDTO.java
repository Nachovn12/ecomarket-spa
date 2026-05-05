package com.ecomarket.reportes.dto;

import java.time.LocalDateTime;

public class ReporteInventarioDTO {

    private Long id;
    private Integer totalProductos;
    private Integer productosBajoStock;
    private Double valorTotalInventario;
    private LocalDateTime fechaReporte;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getTotalProductos() { return totalProductos; }
    public void setTotalProductos(Integer totalProductos) { this.totalProductos = totalProductos; }

    public Integer getProductosBajoStock() { return productosBajoStock; }
    public void setProductosBajoStock(Integer productosBajoStock) { this.productosBajoStock = productosBajoStock; }

    public Double getValorTotalInventario() { return valorTotalInventario; }
    public void setValorTotalInventario(Double valorTotalInventario) { this.valorTotalInventario = valorTotalInventario; }

    public LocalDateTime getFechaReporte() { return fechaReporte; }
    public void setFechaReporte(LocalDateTime fechaReporte) { this.fechaReporte = fechaReporte; }
}