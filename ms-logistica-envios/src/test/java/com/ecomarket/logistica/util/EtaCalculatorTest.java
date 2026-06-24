package com.ecomarket.logistica.util;

import com.ecomarket.logistica.model.Proveedor;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EtaCalculatorTest {

    @Test
    void calcular_SinProveedor_UsaPorDefecto() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime eta = EtaCalculator.calcular(null, null, null, ahora);
        assertTrue(eta.isAfter(ahora));
        assertEquals(ahora.plusDays(3).getDayOfYear(), eta.getDayOfYear());
    }

    @Test
    void calcular_ConProveedorRapido() {
        Proveedor prov = new Proveedor();
        prov.setPlazoDespachoHoras(2);
        LocalDateTime ahora = LocalDateTime.now();
        
        // Mismo origen y destino = 0km -> 0 horas + 2 horas prov = 2 horas ETA
        LocalDateTime eta = EtaCalculator.calcular("Santiago", "Santiago", prov, ahora);
        assertEquals(ahora.plusHours(2), eta);
    }

    @Test
    void calcular_ConDistanciaLarga() {
        Proveedor prov = new Proveedor();
        prov.setPlazoDespachoHoras(5);
        LocalDateTime ahora = LocalDateTime.now();
        
        // Distinto origen = 300km -> 300/60 = 5 horas + 5 horas prov = 10 horas ETA
        LocalDateTime eta = EtaCalculator.calcular("Santiago", "Concepcion", prov, ahora);
        assertEquals(ahora.plusHours(10), eta);
    }

    @Test
    void calcular_DistanciaMedia() {
        Proveedor prov = new Proveedor();
        prov.setPlazoDespachoHoras(2);
        LocalDateTime ahora = LocalDateTime.now();
        // Mismo prefijo "San" -> 50km -> 50/60 = 0.833h + 2h = 2.833h -> ceil = 3h
        LocalDateTime eta = EtaCalculator.calcular("San Bernardo", "San Miguel", prov, ahora);
        assertEquals(ahora.plusHours(3), eta);
    }

    @Test
    void calcularRutaOptima_ParadasVacias_LanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> EtaCalculator.calcularRutaOptima(new ArrayList<>()));
    }

    @Test
    void calcularRutaOptima_UnaParada() {
        List<String> paradas = List.of("Santiago");
        EtaCalculator.RutaResult result = EtaCalculator.calcularRutaOptima(paradas);
        assertEquals(0.0, result.distanciaTotalKm);
        assertEquals(0.0, result.tiempoTotalHoras);
        assertEquals(1, result.ordenParadas.size());
    }

    @Test
    void calcularRutaOptima_MultiplesParadas() {
        List<String> paradas = new ArrayList<>();
        paradas.add("Arica");
        paradas.add("Santiago");
        paradas.add("Antofagasta");
        
        EtaCalculator.RutaResult result = EtaCalculator.calcularRutaOptima(paradas);
        assertTrue(result.distanciaTotalKm > 0);
        assertTrue(result.tiempoTotalHoras > 0);
        assertEquals(3, result.ordenParadas.size());
    }
}
