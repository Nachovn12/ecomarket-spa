package com.ecomarket.reportes.dto;

public class ReporteInventarioDTO {

    private Long idTienda;
    private Integer productosDisponibles;
    private Integer productosBajoStock;
    private Integer productosSinStock;

    public Long getIdTienda() { return idTienda; }
    public void setIdTienda(Long idTienda) { this.idTienda = idTienda; }

    public Integer getProductosDisponibles() { return productosDisponibles; }
    public void setProductosDisponibles(Integer productosDisponibles) { this.productosDisponibles = productosDisponibles; }

    public Integer getProductosBajoStock() { return productosBajoStock; }
    public void setProductosBajoStock(Integer productosBajoStock) { this.productosBajoStock = productosBajoStock; }

    public Integer getProductosSinStock() { return productosSinStock; }
    public void setProductosSinStock(Integer productosSinStock) { this.productosSinStock = productosSinStock; }
}