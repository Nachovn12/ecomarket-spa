package com.ecomarket.usuarios.controller;

import com.ecomarket.usuarios.dto.UsuarioInternoRequestDTO;
import com.ecomarket.usuarios.dto.UsuarioInternoResponseDTO;
import com.ecomarket.usuarios.dto.UsuarioInternoUpdateDTO;
import com.ecomarket.usuarios.service.UsuarioInternoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioInternoController.class)
public class UsuarioInternoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UsuarioInternoService usuarioInternoService;

    @Test
    void crearUsuarioInterno_Exito() throws Exception {
        UsuarioInternoRequestDTO request = new UsuarioInternoRequestDTO();
        request.setNombre("Interno");
        request.setCorreo("interno@test.com");
        request.setPassword("P@ssw0rd1");
        request.setRol("EMPLEADO");

        UsuarioInternoResponseDTO response = UsuarioInternoResponseDTO.builder()
                .id(1L)
                .correo("interno@test.com")
                .rol("EMPLEADO")
                .build();

        when(usuarioInternoService.crearUsuarioInterno(any(UsuarioInternoRequestDTO.class), anyString())).thenReturn(response);

        mockMvc.perform(post("/api/usuarios/internos")
                .header("X-Rol-Usuario", "ADMINISTRADOR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.correo").value("interno@test.com"));
    }

    @Test
    void listarUsuariosInternos_Exito() throws Exception {
        UsuarioInternoResponseDTO response = UsuarioInternoResponseDTO.builder()
                .id(1L)
                .correo("interno@test.com")
                .build();

        when(usuarioInternoService.listarUsuariosInternos(anyString())).thenReturn(List.of(response));

        mockMvc.perform(get("/api/usuarios/internos")
                .header("X-Rol-Usuario", "ADMINISTRADOR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].correo").value("interno@test.com"));
    }

    @Test
    void obtenerUsuarioInterno_Exito() throws Exception {
        UsuarioInternoResponseDTO response = UsuarioInternoResponseDTO.builder()
                .id(1L)
                .correo("interno@test.com")
                .build();

        when(usuarioInternoService.obtenerUsuarioInternoPorId(eq(1L), anyString())).thenReturn(response);

        mockMvc.perform(get("/api/usuarios/internos/1")
                .header("X-Rol-Usuario", "ADMINISTRADOR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value("interno@test.com"));
    }

    @Test
    void actualizarUsuarioInterno_Exito() throws Exception {
        UsuarioInternoUpdateDTO request = new UsuarioInternoUpdateDTO();
        request.setNombre("Nuevo");

        UsuarioInternoResponseDTO response = UsuarioInternoResponseDTO.builder()
                .id(1L)
                .nombre("Nuevo")
                .build();

        when(usuarioInternoService.actualizarUsuarioInterno(eq(1L), any(UsuarioInternoUpdateDTO.class), anyString())).thenReturn(response);

        mockMvc.perform(put("/api/usuarios/internos/1")
                .header("X-Rol-Usuario", "ADMINISTRADOR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Nuevo"));
    }

    @Test
    void desactivarUsuarioInterno_Exito() throws Exception {
        UsuarioInternoResponseDTO response = UsuarioInternoResponseDTO.builder()
                .id(1L)
                .activo(false)
                .build();

        when(usuarioInternoService.desactivarUsuarioInterno(eq(1L), anyString())).thenReturn(response);

        mockMvc.perform(put("/api/usuarios/internos/1/desactivar")
                .header("X-Rol-Usuario", "ADMINISTRADOR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(false));
    }

    @Test
    void eliminarUsuarioInterno_Exito() throws Exception {
        UsuarioInternoResponseDTO response = UsuarioInternoResponseDTO.builder()
                .id(1L)
                .activo(false)
                .eliminado(true)
                .build();

        when(usuarioInternoService.eliminarUsuarioInterno(eq(1L), anyString())).thenReturn(response);

        mockMvc.perform(delete("/api/usuarios/internos/1")
                .header("X-Rol-Usuario", "ADMINISTRADOR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eliminado").value(true));
    }
}
