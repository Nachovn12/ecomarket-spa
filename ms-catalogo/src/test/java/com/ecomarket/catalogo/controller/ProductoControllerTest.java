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

/**
 * Pruebas unitarias de ProductoController usando @WebMvcTest.
 * Datos alineados al dominio del marketplace EcoMarket SPA.
 */
@WebMvcTest(ProductoController.class)
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private CatalogoService catalogoService;

    @Test
    void crear_Exito() throws Exception {
        // Escenario: El administrador expone un nuevo producto (Bolsa biodegradable) vía API.
        ProductoRequestDTO req = new ProductoRequestDTO();
        req.setSku("ECO-001");
        req.setNombre("Bolsa biodegradable mediana");
        req.setPrecio(1990.0);
        req.setDescripcion("Bolsa reutilizable hecha de almidón de maíz");
        req.setIdCategoria(1L);

        ProductoResponseDTO resp = new ProductoResponseDTO();
        resp.setIdProducto(1L);
        resp.setSku("ECO-001");
        resp.setNombre("Bolsa biodegradable mediana");
        resp.setPrecio(1990.0);
        resp.setIdCategoria(1L);
        resp.setNombreCategoria("Productos Biodegradables");
        resp.setEstado("PUBLICADO");

        when(catalogoService.crearProducto(any(ProductoRequestDTO.class))).thenReturn(resp);

        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idProducto").value(1L))
                .andExpect(jsonPath("$.sku").value("ECO-001"))
                .andExpect(jsonPath("$.nombreCategoria").value("Productos Biodegradables"))
                .andExpect(jsonPath("$._links").doesNotExist()); // AC-7: sin HATEOAS
    }

    @Test
    void listarTodos_Exito() throws Exception {
        // Escenario: El front-end del marketplace pide el catálogo completo para la vitrina inicial.
        ProductoResponseDTO p1 = new ProductoResponseDTO();
        p1.setIdProducto(1L);
        p1.setSku("ECO-001");
        p1.setNombre("Bolsa biodegradable mediana");

        ProductoResponseDTO p2 = new ProductoResponseDTO();
        p2.setIdProducto(9L);
        p2.setSku("ECO-009");
        p2.setNombre("Cepillo de dientes de bambú");

        when(catalogoService.obtenerTodosProductos()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("ECO-001"))
                .andExpect(jsonPath("$[1].sku").value("ECO-009"));
    }

    @Test
    void buscarPorId_Exito() throws Exception {
        // Escenario: El cliente hace clic en el "Set de cubiertos de bambú" para ver su detalle (PDP).
        ProductoResponseDTO resp = new ProductoResponseDTO();
        resp.setIdProducto(3L);
        resp.setSku("ECO-003");
        resp.setNombre("Set cubiertos de bambú x6");
        resp.setPrecio(6990.0);

        when(catalogoService.obtenerProductoPorId(3L)).thenReturn(resp);

        mockMvc.perform(get("/api/productos/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto").value(3L))
                .andExpect(jsonPath("$.sku").value("ECO-003"))
                .andExpect(jsonPath("$._links").doesNotExist()); // AC-7
    }

    @Test
    void actualizar_Exito() throws Exception {
        // Escenario: Se actualiza el precio del jabón de avena vía API PUT.
        ProductoRequestDTO req = new ProductoRequestDTO();
        req.setSku("ECO-005");
        req.setNombre("Jabón de avena artesanal");
        req.setPrecio(3990.0); // nuevo precio

        ProductoResponseDTO resp = new ProductoResponseDTO();
        resp.setIdProducto(5L);
        resp.setSku("ECO-005");
        resp.setNombre("Jabón de avena artesanal");
        resp.setPrecio(3990.0);

        when(catalogoService.actualizarProducto(eq(5L), any(ProductoRequestDTO.class))).thenReturn(resp);

        mockMvc.perform(put("/api/productos/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto").value(5L))
                .andExpect(jsonPath("$.precio").value(3990.0));
    }

    @Test
    void eliminar_Exito() throws Exception {
        // Escenario: El administrador elimina el producto descatalogado (ID 8) del catálogo.
        mockMvc.perform(delete("/api/productos/8"))
                .andExpect(status().isNoContent());
    }

    @Test
    void buscar_Exito() throws Exception {
        // Escenario (AC-4): Búsqueda en la barra superior del marketplace ingresando "bambú".
        ProductoResponseDTO resp = new ProductoResponseDTO();
        resp.setIdProducto(9L);
        resp.setSku("ECO-009");
        resp.setNombre("Cepillo de dientes de bambú adulto");

        when(catalogoService.buscarPorPalabraClave("bambú")).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/productos/buscar?palabraClave=bambú"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("ECO-009"));
    }

    @Test
    void buscar_PorCategoria() throws Exception {
        // Escenario: El cliente navega por la categoría "Productos Biodegradables" (ID 1).
        ProductoResponseDTO resp = new ProductoResponseDTO();
        resp.setIdProducto(1L);
        resp.setSku("ECO-001");
        resp.setNombre("Bolsa biodegradable");
        resp.setIdCategoria(1L);

        when(catalogoService.buscarPorCategoria(1L)).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/productos/buscar?idCategoria=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("ECO-001"));
    }

    @Test
    void buscar_PorPrecio() throws Exception {
        // Escenario: El cliente filtra productos en el rango de $1000 a $5000 CLP.
        ProductoResponseDTO resp = new ProductoResponseDTO();
        resp.setIdProducto(1L);
        resp.setPrecio(1990.0);

        when(catalogoService.buscarPorPrecio(1000.0, 5000.0)).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/productos/buscar?precioMinimo=1000.0&precioMaximo=5000.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].precio").value(1990.0));
    }

    @Test
    void buscar_SinFiltros() throws Exception {
        // Escenario: El front-end llama a la búsqueda sin parámetros, se retorna todo el catálogo.
        ProductoResponseDTO resp = new ProductoResponseDTO();
        resp.setIdProducto(1L);
        resp.setSku("ECO-001");

        when(catalogoService.obtenerTodosProductos()).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/productos/buscar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("ECO-001"));
    }

    @Test
    void buscarEcologicos_Exito() throws Exception {
        // Escenario: Se buscan productos que tengan el atributo ecológico "biodegradable".
        ProductoResponseDTO resp = new ProductoResponseDTO();
        resp.setIdProducto(1L);
        resp.setSku("ECO-001");
        resp.setDescripcionEcologica("100% biodegradable en 180 días");

        when(catalogoService.buscarEcologicos("biodegradable")).thenReturn(List.of(resp));

        mockMvc.perform(get("/api/productos/ecologicos?atributoEcologico=biodegradable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("ECO-001"));
    }
}
