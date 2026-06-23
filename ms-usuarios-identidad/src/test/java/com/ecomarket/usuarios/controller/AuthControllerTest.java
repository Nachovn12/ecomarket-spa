package com.ecomarket.usuarios.controller;

import com.ecomarket.usuarios.dto.LoginRequestDTO;
import com.ecomarket.usuarios.dto.LoginResponseDTO;
import com.ecomarket.usuarios.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AuthService authService;

    @Test
    void login_Exito() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setCorreo("test@test.com");
        request.setPassword("P@ssw0rd1");

        LoginResponseDTO response = LoginResponseDTO.builder()
                .correo("test@test.com")
                .tokenSesion("token-123")
                .rol("CLIENTE")
                .build();

        when(authService.iniciarSesion(any(LoginRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correo").value("test@test.com"))
                .andExpect(jsonPath("$.tokenSesion").value("token-123"));
    }
}
