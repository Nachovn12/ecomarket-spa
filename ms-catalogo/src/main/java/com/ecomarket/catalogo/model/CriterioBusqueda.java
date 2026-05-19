package com.ecomarket.catalogo.model;

public class CriterioBusqueda {
    private String palabraClave;
    private Long idCategoria;
    private Double precioMinimo;
    private Double precioMaximo;
    private Boolean soloPublicados;

    public String getPalabraClave() {
        return palabraClave;
    }

    public void setPalabraClave(String palabraClave) {
        this.palabraClave = palabraClave;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Double getPrecioMinimo() {
        return precioMinimo;
    }

    public void setPrecioMinimo(Double precioMinimo) {
        this.precioMinimo = precioMinimo;
    }

    public Double getPrecioMaximo() {
        return precioMaximo;
    }

    public void setPrecioMaximo(Double precioMaximo) {
        this.precioMaximo = precioMaximo;
    }

    public Boolean getSoloPublicados() {
        return soloPublicados;
    }

    public void setSoloPublicados(Boolean soloPublicados) {
        this.soloPublicados = soloPublicados;
    }
}
