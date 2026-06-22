package com.ecomarket.catalogo.controller;

import com.ecomarket.catalogo.dto.ProductoRequestDTO;
import com.ecomarket.catalogo.dto.ProductoResponseDTO;
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

@WebMvcTest(ProductoController.class)
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private CatalogoService catalogoService;

    @Test
    void crear_Exito() throws Exception {
        ProductoRequestDTO req = new ProductoRequestDTO();
        req.setSku("SKU-1");
        req.setNombre("Prod 1");
        req.setPrecio(100.0);

        ProductoResponseDTO res = new ProductoResponseDTO();
        res.setIdProducto(1L);
        res.setSku("SKU-1");
        res.setNombre("Prod 1");
        res.setPrecio(100.0);

        when(catalogoService.crearProducto(any(ProductoRequestDTO.class))).thenReturn(res);

        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idProducto").value(1L))
                .andExpect(jsonPath("$.sku").value("SKU-1"))
                .andExpect(jsonPath("$._links").doesNotExist());
    }

    @Test
    void listarTodos_Exito() throws Exception {
        ProductoResponseDTO res = new ProductoResponseDTO();
        res.setIdProducto(1L);
        when(catalogoService.obtenerTodosProductos()).thenReturn(List.of(res));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idProducto").value(1L));
    }

    @Test
    void buscarPorId_Exito() throws Exception {
        ProductoResponseDTO res = new ProductoResponseDTO();
        res.setIdProducto(1L);
        when(catalogoService.obtenerProductoPorId(1L)).thenReturn(res);

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto").value(1L));
    }

    @Test
    void actualizar_Exito() throws Exception {
        ProductoRequestDTO req = new ProductoRequestDTO();
        req.setSku("SKU-1");
        req.setNombre("Prod 1");
        req.setPrecio(100.0);

        ProductoResponseDTO res = new ProductoResponseDTO();
        res.setIdProducto(1L);
        when(catalogoService.actualizarProducto(eq(1L), any(ProductoRequestDTO.class))).thenReturn(res);

        mockMvc.perform(put("/api/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto").value(1L));
    }

    @Test
    void eliminar_Exito() throws Exception {
        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void buscar_Exito() throws Exception {
        ProductoResponseDTO res = new ProductoResponseDTO();
        res.setIdProducto(1L);
        when(catalogoService.buscarPorPalabraClave("test")).thenReturn(List.of(res));

        mockMvc.perform(get("/api/productos/buscar?palabraClave=test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idProducto").value(1L));
    }

    @Test
    void buscar_PorCategoria() throws Exception {
        ProductoResponseDTO res = new ProductoResponseDTO();
        res.setIdProducto(1L);
        when(catalogoService.buscarPorCategoria(1L)).thenReturn(List.of(res));

        mockMvc.perform(get("/api/productos/buscar?idCategoria=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idProducto").value(1L));
    }

    @Test
    void buscar_PorPrecio() throws Exception {
        ProductoResponseDTO res = new ProductoResponseDTO();
        res.setIdProducto(1L);
        when(catalogoService.buscarPorPrecio(10.0, 50.0)).thenReturn(List.of(res));

        mockMvc.perform(get("/api/productos/buscar?precioMinimo=10.0&precioMaximo=50.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idProducto").value(1L));
    }

    @Test
    void buscar_SinFiltros() throws Exception {
        ProductoResponseDTO res = new ProductoResponseDTO();
        res.setIdProducto(1L);
        when(catalogoService.obtenerTodosProductos()).thenReturn(List.of(res));

        mockMvc.perform(get("/api/productos/buscar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idProducto").value(1L));
    }

    @Test
    void buscarEcologicos_Exito() throws Exception {
        ProductoResponseDTO res = new ProductoResponseDTO();
        res.setIdProducto(1L);
        when(catalogoService.buscarEcologicos("biodegradable")).thenReturn(List.of(res));

        mockMvc.perform(get("/api/productos/ecologicos?atributoEcologico=biodegradable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idProducto").value(1L));
    }
}
