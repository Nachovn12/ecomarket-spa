package com.ecomarket.usuarios.controller;

import com.ecomarket.usuarios.dto.ActualizarPerfilClienteRequestDTO;
import com.ecomarket.usuarios.dto.PerfilClienteResponseDTO;
import com.ecomarket.usuarios.dto.UsuarioRequestDTO;
import com.ecomarket.usuarios.dto.UsuarioResponseDTO;
import com.ecomarket.usuarios.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UsuarioService usuarioService;

    @Test
    void registrarCliente_Exito() throws Exception {
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setNombre("Test");
        request.setCorreo("test@test.com");
        request.setPassword("P@ssw0rd1");

        UsuarioResponseDTO response = UsuarioResponseDTO.builder()
                .id(1L)
                .correo("test@test.com")
                .rol("CLIENTE")
                .build();

        when(usuarioService.registrarCliente(any(UsuarioRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/usuarios/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.correo").value("test@test.com"));
    }

    @Test
    void obtenerPerfilCliente_Exito() throws Exception {
        PerfilClienteResponseDTO response = PerfilClienteResponseDTO.builder()
                .id(1L)
                .correo("test@test.com")
                .build();

        when(usuarioService.obtenerPerfilCliente(1L)).thenReturn(response);

        mockMvc.perform(get("/api/usuarios/clientes/1/perfil"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value("test@test.com"));
    }

    @Test
    void actualizarPerfilCliente_Exito() throws Exception {
        ActualizarPerfilClienteRequestDTO request = new ActualizarPerfilClienteRequestDTO();
        request.setNombre("Nuevo");
        request.setCorreo("nuevo@test.com");
        request.setTelefono("+56987654321");
        request.setDireccionEnvio("Av. Siempre Viva 742, Santiago");

        PerfilClienteResponseDTO response = PerfilClienteResponseDTO.builder()
                .id(1L)
                .correo("nuevo@test.com")
                .nombre("Nuevo")
                .build();

        when(usuarioService.actualizarPerfilCliente(eq(1L), any(ActualizarPerfilClienteRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/usuarios/clientes/1/perfil")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value("nuevo@test.com"))
                .andExpect(jsonPath("$.nombre").value("Nuevo"));
    }
}
