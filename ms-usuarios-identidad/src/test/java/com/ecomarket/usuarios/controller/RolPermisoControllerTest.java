package com.ecomarket.usuarios.controller;

import com.ecomarket.usuarios.dto.RolPermisosRequestDTO;
import com.ecomarket.usuarios.dto.RolPermisosResponseDTO;
import com.ecomarket.usuarios.dto.VerificacionAccesoResponseDTO;
import com.ecomarket.usuarios.service.RolPermisoService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RolPermisoController.class)
public class RolPermisoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private RolPermisoService rolPermisoService;

    @Test
    void asignarRolesPermisos_Exito() throws Exception {
        RolPermisosRequestDTO request = new RolPermisosRequestDTO();
        request.setRol("EMPLEADO");
        request.setPermisos(List.of("VENTAS"));
        request.setModificadoPor("ADMIN");

        RolPermisosResponseDTO response = RolPermisosResponseDTO.builder()
                .idUsuario(1L)
                .rol("EMPLEADO")
                .permisos(List.of("VENTAS"))
                .build();

        when(rolPermisoService.asignarRolesPermisos(eq(1L), any(RolPermisosRequestDTO.class), anyString())).thenReturn(response);

        mockMvc.perform(put("/api/usuarios/internos/1/roles-permisos")
                .header("X-Rol-Usuario", "ADMINISTRADOR")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rol").value("EMPLEADO"))
                .andExpect(jsonPath("$.permisos[0]").value("VENTAS"));
    }

    @Test
    void obtenerRolesPermisos_Exito() throws Exception {
        RolPermisosResponseDTO response = RolPermisosResponseDTO.builder()
                .idUsuario(1L)
                .rol("EMPLEADO")
                .permisos(List.of("VENTAS"))
                .build();

        when(rolPermisoService.obtenerRolesPermisos(eq(1L), anyString())).thenReturn(response);

        mockMvc.perform(get("/api/usuarios/internos/1/roles-permisos")
                .header("X-Rol-Usuario", "ADMINISTRADOR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rol").value("EMPLEADO"));
    }

    @Test
    void verificarAcceso_Exito() throws Exception {
        VerificacionAccesoResponseDTO response = VerificacionAccesoResponseDTO.builder()
                .accesoPermitido(true)
                .modulo("VENTAS")
                .build();

        when(rolPermisoService.verificarAcceso(1L, "VENTAS")).thenReturn(response);

        mockMvc.perform(get("/api/usuarios/internos/1/verificar-acceso")
                .param("modulo", "VENTAS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accesoPermitido").value(true))
                .andExpect(jsonPath("$.modulo").value("VENTAS"));
    }
}
