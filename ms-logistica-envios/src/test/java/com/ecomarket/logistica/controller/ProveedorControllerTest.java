package com.ecomarket.logistica.controller;

import com.ecomarket.logistica.dto.ProveedorDTO;
import com.ecomarket.logistica.model.Proveedor;
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

@WebMvcTest(ProveedorController.class)
class ProveedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private LogisticaService logisticaService;

    @Test
    void crear_Exito() throws Exception {
        ProveedorDTO dto = new ProveedorDTO();
        dto.setRazonSocial("LogisT");
        dto.setRut("12345678-9");
        dto.setTipoProveedor("TRANSPORTE");
        dto.setCobertura("NACIONAL");

        Proveedor prov = new Proveedor();
        prov.setId(1L);
        prov.setRazonSocial("LogisT");

        when(logisticaService.crearProveedor(any(ProveedorDTO.class))).thenReturn(prov);

        mockMvc.perform(post("/api/envios/proveedores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$._links").doesNotExist());
    }

    @Test
    void obtenerTodos_Exito() throws Exception {
        Proveedor prov = new Proveedor();
        prov.setId(1L);

        when(logisticaService.obtenerProveedores()).thenReturn(List.of(prov));

        mockMvc.perform(get("/api/envios/proveedores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0]._links").doesNotExist());
    }

    @Test
    void obtenerPorId_Exito() throws Exception {
        Proveedor prov = new Proveedor();
        prov.setId(1L);
        when(logisticaService.obtenerProveedorPorId(1L)).thenReturn(prov);

        mockMvc.perform(get("/api/envios/proveedores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void actualizar_Exito() throws Exception {
        ProveedorDTO dto = new ProveedorDTO();
        dto.setRazonSocial("Test");
        dto.setRut("1234");
        dto.setTipoProveedor("TRANSPORTE");
        dto.setCobertura("LOCAL");
        Proveedor prov = new Proveedor();
        prov.setId(1L);
        when(logisticaService.actualizarProveedor(org.mockito.ArgumentMatchers.eq(1L), any(ProveedorDTO.class))).thenReturn(prov);

        mockMvc.perform(put("/api/envios/proveedores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void desactivar_Exito() throws Exception {
        mockMvc.perform(patch("/api/envios/proveedores/1/desactivar"))
                .andExpect(status().isNoContent());
    }

    @Test
    void activar_Exito() throws Exception {
        mockMvc.perform(patch("/api/envios/proveedores/1/activar"))
                .andExpect(status().isNoContent());
    }

    @Test
    void obtenerActivos_Exito() throws Exception {
        when(logisticaService.obtenerProveedoresActivos()).thenReturn(List.of(new Proveedor()));
        mockMvc.perform(get("/api/envios/proveedores/activos"))
                .andExpect(status().isOk());
    }

    @Test
    void buscar_Exito() throws Exception {
        when(logisticaService.buscarProveedores("A", "B")).thenReturn(List.of(new Proveedor()));
        mockMvc.perform(get("/api/envios/proveedores/buscar")
                .param("tipoProveedor", "A")
                .param("cobertura", "B"))
                .andExpect(status().isOk());
    }
}
