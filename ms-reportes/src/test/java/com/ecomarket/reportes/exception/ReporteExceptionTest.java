package com.ecomarket.reportes.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReporteExceptionTest {

    @Test
    void reporteException_constructorConMensaje() {
        ReporteException ex = new ReporteException("Error de reporte");
        assertThat(ex.getMessage()).isEqualTo("Error de reporte");
    }

    @Test
    void reporteNotFoundException_constructorConMensaje() {
        ReporteNotFoundException ex = new ReporteNotFoundException("Reporte no encontrado");
        assertThat(ex.getMessage()).isEqualTo("Reporte no encontrado");
    }
}
