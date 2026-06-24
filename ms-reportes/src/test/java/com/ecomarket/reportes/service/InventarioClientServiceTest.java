package com.ecomarket.reportes.service;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventarioClientServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private InventarioClientService inventarioClientService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(inventarioClientService, "msInventarioUrl", "http://localhost:8085");
    }

    @Test
    void ping_respondeOk_retornaMapa() {
        Map<String, Object> responseMock = Map.of("status", "OK");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(new ResponseEntity<>(responseMock, HttpStatus.OK));

        Map<String, Object> resultado = inventarioClientService.ping();

        assertThat(resultado).isNotNull();
        assertThat(resultado.get("status")).isEqualTo("OK");
    }

    @Test
    void ping_responde404_retornaNull() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "Not Found", null, null, null));

        Map<String, Object> resultado = inventarioClientService.ping();

        assertThat(resultado).isNull();
    }

    @Test
    void ping_ResourceAccessException_lanzaIllegalStateException() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenThrow(new ResourceAccessException("Connection refused"));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> inventarioClientService.ping());
        assertThat(ex.getMessage()).contains("no esta disponible");
    }

    @Test
    void ping_ExceptionGenerica_lanzaIllegalStateException() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Map.class)))
                .thenThrow(new RuntimeException("Error inesperado"));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> inventarioClientService.ping());
        assertThat(ex.getMessage()).contains("No se pudo comunicar");
    }
}
