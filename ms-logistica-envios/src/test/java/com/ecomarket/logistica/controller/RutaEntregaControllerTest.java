package com.ecomarket.logistica.controller;

import com.ecomarket.logistica.dto.RutaEntregaDTO;
import com.ecomarket.logistica.model.RutaEntrega;
import com.ecomarket.logistica.model.enums.EstadoRuta;
import com.ecomarket.logistica.service.LogisticaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RutaEntregaController.class)
class RutaEntregaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private LogisticaService logisticaService;

    @Test
    void crear_Exito() throws Exception {
        RutaEntregaDTO dto = new RutaEntregaDTO();

        RutaEntrega ruta = new RutaEntrega();
        ruta.setId(1L);
        ruta.setEstado(EstadoRuta.PLANIFICADA);

        when(logisticaService.crearRuta(any(RutaEntregaDTO.class))).thenReturn(ruta);

        mockMvc.perform(post("/api/rutas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("PLANIFICADA"))
                .andExpect(jsonPath("$._links").doesNotExist());
    }

    @Test
    void obtenerTodos_Exito() throws Exception {
        RutaEntrega ruta = new RutaEntrega();
        ruta.setId(1L);

        when(logisticaService.obtenerRutas()).thenReturn(List.of(ruta));

        mockMvc.perform(get("/api/rutas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0]._links").doesNotExist());
    }

    @Test
    void obtenerPorId_Exito() throws Exception {
        RutaEntrega ruta = new RutaEntrega();
        ruta.setId(1L);
        when(logisticaService.obtenerRutaPorId(1L)).thenReturn(ruta);

        mockMvc.perform(get("/api/rutas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void actualizar_Exito() throws Exception {
        RutaEntregaDTO dto = new RutaEntregaDTO();
        RutaEntrega ruta = new RutaEntrega();
        ruta.setId(1L);
        when(logisticaService.actualizarRuta(org.mockito.ArgumentMatchers.eq(1L), any(RutaEntregaDTO.class))).thenReturn(ruta);

        mockMvc.perform(put("/api/rutas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void eliminar_Exito() throws Exception {
        mockMvc.perform(delete("/api/rutas/1"))
                .andExpect(status().isNoContent());
    }


    @Test
    void cambiarEstado_Exito() throws Exception {
        com.ecomarket.logistica.dto.CambioEstadoRutaRequestDTO dto = new com.ecomarket.logistica.dto.CambioEstadoRutaRequestDTO();
        dto.setEstado(com.ecomarket.logistica.model.enums.EstadoRuta.FINALIZADA);
        RutaEntrega ruta = new RutaEntrega();
        ruta.setId(1L);
        when(logisticaService.cambiarEstadoRuta(org.mockito.ArgumentMatchers.eq(1L), any(com.ecomarket.logistica.model.enums.EstadoRuta.class))).thenReturn(ruta);

        mockMvc.perform(patch("/api/rutas/1/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
}
