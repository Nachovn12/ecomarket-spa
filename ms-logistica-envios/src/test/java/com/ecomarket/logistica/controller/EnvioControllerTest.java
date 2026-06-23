package com.ecomarket.logistica.controller;

import com.ecomarket.logistica.dto.EnvioDTO;
import com.ecomarket.logistica.model.Envio;
import com.ecomarket.logistica.model.enums.EstadoEnvio;
import com.ecomarket.logistica.service.LogisticaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnvioController.class)
class EnvioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private LogisticaService logisticaService;

    @Test
    void crear_Exito() throws Exception {
        EnvioDTO dto = new EnvioDTO();
        dto.setIdPedido(100L);
        dto.setOrigen("Santiago");
        dto.setDestino("Valparaiso");

        Envio envio = new Envio();
        envio.setId(1L);
        envio.setIdPedido(100L);
        envio.setOrigen("Santiago");
        envio.setDestino("Valparaiso");
        envio.setEstado(EstadoEnvio.PREPARADO);

        when(logisticaService.crearEnvio(any(EnvioDTO.class))).thenReturn(envio);

        mockMvc.perform(post("/api/envios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.origen").value("Santiago"))
                .andExpect(jsonPath("$._links").doesNotExist());
    }

    @Test
    void obtenerTodos_Exito() throws Exception {
        Envio envio = new Envio();
        envio.setId(1L);
        envio.setEstado(EstadoEnvio.PREPARADO);

        when(logisticaService.obtenerEnvios()).thenReturn(List.of(envio));

        mockMvc.perform(get("/api/envios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0]._links").doesNotExist());
    }

    @Test
    void obtenerPorId_Exito() throws Exception {
        Envio envio = new Envio();
        envio.setId(1L);

        when(logisticaService.obtenerEnvioPorId(1L)).thenReturn(envio);

        mockMvc.perform(get("/api/envios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links").doesNotExist());
    }

    @Test
    void actualizar_Exito() throws Exception {
        EnvioDTO dto = new EnvioDTO();
        dto.setIdPedido(1L);
        dto.setOrigen("Nuevo");
        dto.setDestino("Destino");

        Envio envio = new Envio();
        envio.setId(1L);
        envio.setOrigen("Nuevo");

        when(logisticaService.actualizarEnvio(eq(1L), any(EnvioDTO.class))).thenReturn(envio);

        mockMvc.perform(put("/api/envios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void eliminar_Exito() throws Exception {
        mockMvc.perform(delete("/api/envios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void obtenerPorPedido_Exito() throws Exception {
        Envio envio = new Envio();
        envio.setId(1L);

        when(logisticaService.obtenerEnviosPorPedido(1L)).thenReturn(List.of(envio));

        mockMvc.perform(get("/api/envios/pedido/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void obtenerSeguimiento_Exito() throws Exception {
        when(logisticaService.obtenerSeguimiento(1L)).thenReturn(List.of());
        mockMvc.perform(get("/api/envios/1/seguimiento"))
                .andExpect(status().isOk());
    }

    @Test
    void registrarIncidencia_Exito() throws Exception {
        com.ecomarket.logistica.dto.IncidenciaRequestDTO dto = new com.ecomarket.logistica.dto.IncidenciaRequestDTO();
        dto.setMotivoIncidencia("Robo");
        dto.setActualizadoPor("Admin");
        Envio envio = new Envio();
        envio.setId(1L);
        when(logisticaService.registrarIncidencia(org.mockito.ArgumentMatchers.eq(1L), any(com.ecomarket.logistica.dto.IncidenciaRequestDTO.class))).thenReturn(envio);

        mockMvc.perform(patch("/api/envios/1/incidencia")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void cambiarEstado_Exito() throws Exception {
        com.ecomarket.logistica.dto.CambioEstadoRequestDTO dto = new com.ecomarket.logistica.dto.CambioEstadoRequestDTO();
        dto.setEstado(com.ecomarket.logistica.model.enums.EstadoEnvio.EN_CAMINO);
        dto.setActualizadoPor("Admin");
        Envio envio = new Envio();
        envio.setId(1L);
        when(logisticaService.cambiarEstadoEnvio(org.mockito.ArgumentMatchers.eq(1L), any(com.ecomarket.logistica.dto.CambioEstadoRequestDTO.class))).thenReturn(envio);

        mockMvc.perform(patch("/api/envios/1/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
}
