package com.ecomarket.reportes.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReporteExceptionTest {

    @Test
    void reporteException_constructorConMensaje() {
        ReporteException ex = new ReporteException("Las fechas de inicio y fin son obligatorias para generar el reporte");
        assertThat(ex.getMessage()).isEqualTo("Las fechas de inicio y fin son obligatorias para generar el reporte");
    }

    @Test
    void reporteNotFoundException_constructorConMensaje() {
        ReporteNotFoundException ex = new ReporteNotFoundException("Reporte no encontrado con id: 999");
        assertThat(ex.getMessage()).isEqualTo("Reporte no encontrado con id: 999");
    }
}
