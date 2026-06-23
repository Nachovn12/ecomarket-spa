package com.ecomarket.catalogo.controller;

import com.ecomarket.catalogo.dto.ResenaRequestDTO;
import com.ecomarket.catalogo.dto.ResenaResponseDTO;
import com.ecomarket.catalogo.service.CatalogoService;
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

@WebMvcTest(ResenaController.class)
public class ResenaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private CatalogoService catalogoService;

    @Test
    void crear_Exito() throws Exception {
        ResenaRequestDTO req = new ResenaRequestDTO();
        req.setIdProducto(1L);
        req.setIdCliente(10L);
        req.setCalificacion(5);

        ResenaResponseDTO res = new ResenaResponseDTO();
        res.setIdResena(1L);
        res.setCalificacion(5);

        when(catalogoService.crearResena(any(ResenaRequestDTO.class))).thenReturn(res);

        mockMvc.perform(post("/api/resenas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idResena").value(1L))
                .andExpect(jsonPath("$._links").doesNotExist());
    }

    @Test
    void listarTodos_Exito() throws Exception {
        ResenaResponseDTO res = new ResenaResponseDTO();
        res.setIdResena(1L);
        when(catalogoService.obtenerTodasResenas()).thenReturn(List.of(res));

        mockMvc.perform(get("/api/resenas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idResena").value(1L));
    }

    @Test
    void buscarPorId_Exito() throws Exception {
        ResenaResponseDTO res = new ResenaResponseDTO();
        res.setIdResena(1L);
        when(catalogoService.obtenerResenaPorId(1L)).thenReturn(res);

        mockMvc.perform(get("/api/resenas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idResena").value(1L));
    }

    @Test
    void eliminar_Exito() throws Exception {
        mockMvc.perform(delete("/api/resenas/1"))
                .andExpect(status().isNoContent());
    }
}
