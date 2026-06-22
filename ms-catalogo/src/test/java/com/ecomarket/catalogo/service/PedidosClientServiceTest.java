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

@ExtendWith(MockitoExtension.class)
public class PedidosClientServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PedidosClientService pedidosClientService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(pedidosClientService, "msPedidosUrl", "http://localhost:8091");
    }

    @Test
    void ping_Exito() {
        Map<String, Object> response = Map.of("status", "UP");
        ResponseEntity<Map> entity = new ResponseEntity<>(response, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(entity);

        Map<String, Object> res = pedidosClientService.ping();
        assertNotNull(res);
        assertEquals("UP", res.get("status"));
    }

    @Test
    void ping_NotFound() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenThrow(HttpClientErrorException.NotFound.class);

        assertNull(pedidosClientService.ping());
    }

    @Test
    void ping_ResourceAccessException() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenThrow(ResourceAccessException.class);

        assertThrows(IllegalStateException.class, () -> pedidosClientService.ping());
    }

    @Test
    void ping_Exception() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenThrow(RuntimeException.class);

        assertThrows(IllegalStateException.class, () -> pedidosClientService.ping());
    }

    @Test
    void verificarCompra_True() {
        ResponseEntity<Boolean> entity = new ResponseEntity<>(true, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Boolean.class)))
                .thenReturn(entity);

        assertTrue(pedidosClientService.verificarCompra(1L, 1L));
    }

    @Test
    void verificarCompra_False() {
        ResponseEntity<Boolean> entity = new ResponseEntity<>(false, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Boolean.class)))
                .thenReturn(entity);

        assertFalse(pedidosClientService.verificarCompra(1L, 1L));
    }

    @Test
    void verificarCompra_Exception() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Boolean.class)))
                .thenThrow(RuntimeException.class);

        assertFalse(pedidosClientService.verificarCompra(1L, 1L));
    }
}
