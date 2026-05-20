package com.ecomarket.usuarios.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class RolPermisosResponseDTO {

    private Long idUsuario;
    private String nombre;
    private String correo;
    private String rol;
    private String nivelAcceso;
    private List<String> permisos;
    private String modificadoPor;
    private LocalDateTime fechaModificacionAcceso;
}