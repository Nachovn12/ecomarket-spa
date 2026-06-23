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

/**
 * Pruebas unitarias de ResenaController usando @WebMvcTest.
 * Datos alineados al dominio del marketplace EcoMarket SPA.
 */
@WebMvcTest(ResenaController.class)
public class ResenaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private CatalogoService catalogoService;

    @Test
    void crear_Exito() throws Exception {
        // Escenario: El cliente María Fernández (ID 12) deja una reseña de 5 estrellas
        // para el Aceite esencial de lavanda (ID 15) que acaba de comprar.
        ResenaRequestDTO req = new ResenaRequestDTO();
        req.setIdProducto(15L);
        req.setIdCliente(12L);
        req.setCalificacion(5);
        req.setComentario("Excelente calidad, el aroma es increíble y duró todo el día");

        ResenaResponseDTO resp = new ResenaResponseDTO();
        resp.setIdResena(1L);
        resp.setIdProducto(15L);
        resp.setIdCliente(12L);
        resp.setCalificacion(5);
        resp.setComentario("Excelente calidad, el aroma es increíble y duró todo el día");
        resp.setEstado("PUBLICADA");

        when(catalogoService.crearResena(any(ResenaRequestDTO.class))).thenReturn(resp);

        mockMvc.perform(post("/api/resenas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idResena").value(1L))
                .andExpect(jsonPath("$.calificacion").value(5))
                .andExpect(jsonPath("$._links").doesNotExist()); // AC-7: sin HATEOAS
    }

    @Test
    void listarTodos_Exito() throws Exception {
        // Escenario: El administrador revisa el feed global de reseñas del marketplace.
        ResenaResponseDTO r1 = new ResenaResponseDTO();
        r1.setIdResena(1L);
        r1.setCalificacion(5);
        r1.setComentario("Muy buena bolsa");

        ResenaResponseDTO r2 = new ResenaResponseDTO();
        r2.setIdResena(2L);
        r2.setCalificacion(3);
        r2.setComentario("El cepillo llegó roto");

        when(catalogoService.obtenerTodasResenas()).thenReturn(List.of(r1, r2));

        mockMvc.perform(get("/api/resenas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idResena").value(1L))
                .andExpect(jsonPath("$[1].idResena").value(2L));
    }

    @Test
    void buscarPorId_Exito() throws Exception {
        // Escenario: Se consulta el detalle de la reseña ID 1.
        ResenaResponseDTO resp = new ResenaResponseDTO();
        resp.setIdResena(1L);
        resp.setCalificacion(5);

        when(catalogoService.obtenerResenaPorId(1L)).thenReturn(resp);

        mockMvc.perform(get("/api/resenas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idResena").value(1L))
                .andExpect(jsonPath("$.calificacion").value(5));
    }

    @Test
    void eliminar_Exito() throws Exception {
        // Escenario: El moderador elimina una reseña spam u ofensiva (ID 99).
        mockMvc.perform(delete("/api/resenas/99"))
                .andExpect(status().isNoContent());
    }
}
