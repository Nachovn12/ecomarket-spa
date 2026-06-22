package com.ecomarket.catalogo.controller;

import com.ecomarket.catalogo.dto.CategoriaRequestDTO;
import com.ecomarket.catalogo.dto.CategoriaResponseDTO;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoriaController.class)
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private CatalogoService catalogoService;

    @Test
    void crear_Exito() throws Exception {
        CategoriaRequestDTO req = new CategoriaRequestDTO();
        req.setNombre("Cat 1");

        CategoriaResponseDTO res = new CategoriaResponseDTO();
        res.setIdCategoria(1L);
        res.setNombre("Cat 1");

        when(catalogoService.crearCategoria(any(CategoriaRequestDTO.class))).thenReturn(res);

        mockMvc.perform(post("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCategoria").value(1L));
    }

    @Test
    void listarTodos_Exito() throws Exception {
        CategoriaResponseDTO res = new CategoriaResponseDTO();
        res.setIdCategoria(1L);
        when(catalogoService.obtenerTodasCategorias()).thenReturn(List.of(res));

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCategoria").value(1L));
    }

    @Test
    void buscarPorId_Exito() throws Exception {
        CategoriaResponseDTO res = new CategoriaResponseDTO();
        res.setIdCategoria(1L);
        when(catalogoService.obtenerCategoriaPorId(1L)).thenReturn(res);

        mockMvc.perform(get("/api/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCategoria").value(1L));
    }

    @Test
    void actualizar_Exito() throws Exception {
        CategoriaRequestDTO req = new CategoriaRequestDTO();
        req.setNombre("Cat NEW");

        CategoriaResponseDTO res = new CategoriaResponseDTO();
        res.setIdCategoria(1L);
        res.setNombre("Cat NEW");

        when(catalogoService.actualizarCategoria(eq(1L), any(CategoriaRequestDTO.class))).thenReturn(res);

        mockMvc.perform(put("/api/categorias/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Cat NEW"));
    }

    @Test
    void eliminar_Exito() throws Exception {
        mockMvc.perform(delete("/api/categorias/1"))
                .andExpect(status().isNoContent());
    }
}
