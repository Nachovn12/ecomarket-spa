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

/**
 * Pruebas unitarias de CategoriaController usando @WebMvcTest.
 * Datos alineados al dominio del marketplace EcoMarket SPA.
 */
@WebMvcTest(CategoriaController.class)
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private CatalogoService catalogoService;

    @Test
    void crear_Exito() throws Exception {
        // Escenario: El administrador de EcoMarket crea la categoría "Higiene Natural".
        CategoriaRequestDTO req = new CategoriaRequestDTO();
        req.setNombre("Higiene Natural");
        req.setDescripcion("Productos de higiene personal libres de químicos");

        CategoriaResponseDTO resp = new CategoriaResponseDTO();
        resp.setIdCategoria(2L);
        resp.setNombre("Higiene Natural");
        resp.setDescripcion("Productos de higiene personal libres de químicos");
        resp.setEstado("ACTIVA");

        when(catalogoService.crearCategoria(any(CategoriaRequestDTO.class))).thenReturn(resp);

        mockMvc.perform(post("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCategoria").value(2L))
                .andExpect(jsonPath("$.nombre").value("Higiene Natural"))
                .andExpect(jsonPath("$.estado").value("ACTIVA"))
                .andExpect(jsonPath("$._links").doesNotExist()); // AC-7: sin HATEOAS
    }

    @Test
    void listarTodos_Exito() throws Exception {
        // Escenario: El front-end pide las categorías para poblar el menú lateral de filtros.
        CategoriaResponseDTO c1 = new CategoriaResponseDTO();
        c1.setIdCategoria(1L);
        c1.setNombre("Productos Biodegradables");

        CategoriaResponseDTO c2 = new CategoriaResponseDTO();
        c2.setIdCategoria(2L);
        c2.setNombre("Higiene Natural");

        when(catalogoService.obtenerTodasCategorias()).thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Productos Biodegradables"))
                .andExpect(jsonPath("$[1].nombre").value("Higiene Natural"));
    }

    @Test
    void buscarPorId_Exito() throws Exception {
        // Escenario: Se consultan los detalles de la categoría "Hogar Sustentable" (ID 3).
        CategoriaResponseDTO resp = new CategoriaResponseDTO();
        resp.setIdCategoria(3L);
        resp.setNombre("Hogar Sustentable");

        when(catalogoService.obtenerCategoriaPorId(3L)).thenReturn(resp);

        mockMvc.perform(get("/api/categorias/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCategoria").value(3L))
                .andExpect(jsonPath("$.nombre").value("Hogar Sustentable"));
    }

    @Test
    void actualizar_Exito() throws Exception {
        // Escenario: Se actualiza el nombre de la categoría "Higiene Natural" vía API PUT.
        CategoriaRequestDTO req = new CategoriaRequestDTO();
        req.setNombre("Higiene y Cuidado Natural");

        CategoriaResponseDTO resp = new CategoriaResponseDTO();
        resp.setIdCategoria(2L);
        resp.setNombre("Higiene y Cuidado Natural");

        when(catalogoService.actualizarCategoria(eq(2L), any(CategoriaRequestDTO.class))).thenReturn(resp);

        mockMvc.perform(put("/api/categorias/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCategoria").value(2L))
                .andExpect(jsonPath("$.nombre").value("Higiene y Cuidado Natural"));
    }

    @Test
    void eliminar_Exito() throws Exception {
        // Escenario: El administrador elimina una categoría vacía (ID 4).
        mockMvc.perform(delete("/api/categorias/4"))
                .andExpect(status().isNoContent());
    }
}
