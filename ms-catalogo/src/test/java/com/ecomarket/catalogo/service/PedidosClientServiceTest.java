package com.ecomarket.catalogo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Pruebas unitarias de PedidosClientService.
 * Verifica la comunicación síncrona vía RestTemplate con el MS Pedidos y Ventas.
 */
@ExtendWith(MockitoExtension.class)
public class PedidosClientServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PedidosClientService pedidosClientService;

    @BeforeEach
    void setUp() {
        // Inyectar URL simulada del MS Pedidos y Ventas
        ReflectionTestUtils.setField(pedidosClientService, "msPedidosUrl", "http://localhost:8091");
    }

    // ─── PING: Healthcheck con MS Pedidos ──────────────────────────────────────

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    void ping_Exito() {
        // Escenario: Healthcheck síncrono para verificar que el MS Pedidos está levantado.
        Map<String, Object> mockResponse = Map.of("status", "UP");
        ResponseEntity<Map> entity = new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(entity);

        Map<String, Object> resp = pedidosClientService.ping();
        assertNotNull(resp);
        assertEquals("UP", resp.get("status"));
    }

    @Test
    void ping_NotFound() {
        // Escenario: El MS Pedidos responde 404 (endpoint no encontrado).
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenThrow(HttpClientErrorException.NotFound.class);

        assertNull(pedidosClientService.ping());
    }

    @Test
    void ping_ResourceAccessException_LanzaExcepcion() {
        // Escenario: El MS Pedidos está caído (connection refused).
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenThrow(ResourceAccessException.class);

        assertThrows(IllegalStateException.class, () -> pedidosClientService.ping());
    }

    @Test
    void ping_Exception_Generica_LanzaExcepcion() {
        // Escenario: Error interno genérico al hacer ping.
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenThrow(RuntimeException.class);

        assertThrows(IllegalStateException.class, () -> pedidosClientService.ping());
    }

    // ─── VERIFICAR COMPRA: Interacción Crítica (AC-5 HU-63) ────────────────────

    @Test
    void verificarCompra_True_ClienteSiCompro() {
        // Escenario: María Fernández (ID 12) compró el Aceite de lavanda (ID 15).
        // El MS Pedidos devuelve true (sí lo compró).
        ResponseEntity<Boolean> entity = new ResponseEntity<>(true, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8091/api/pedidos/verificar-compra?idCliente=12&idProducto=15"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Boolean.class)))
                .thenReturn(entity);

        assertTrue(pedidosClientService.verificarCompra(12L, 15L));
    }

    @Test
    void verificarCompra_False_ClienteNoCompro() {
        // Escenario: Carlos Vega (ID 20) no compró la Bolsa biodegradable (ID 1).
        // El MS Pedidos devuelve false (nunca completó un pedido con este producto).
        ResponseEntity<Boolean> entity = new ResponseEntity<>(false, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8091/api/pedidos/verificar-compra?idCliente=20&idProducto=1"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Boolean.class)))
                .thenReturn(entity);

        assertFalse(pedidosClientService.verificarCompra(20L, 1L));
    }

    @Test
    void verificarCompra_Exception_FallbackFalse() {
        // Escenario: El MS Pedidos está caído en pleno proceso de validación.
        // Fallback defensivo: se asume false para prevenir reseñas fraudulentas.
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Boolean.class)))
                .thenThrow(RuntimeException.class);

        assertFalse(pedidosClientService.verificarCompra(12L, 15L));
    }
}
