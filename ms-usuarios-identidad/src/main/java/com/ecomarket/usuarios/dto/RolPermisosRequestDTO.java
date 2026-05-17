package com.ecomarket.usuarios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RolPermisosRequestDTO {

    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "EMPLEADO|GERENTE|ADMINISTRADOR", message = "El rol debe ser EMPLEADO, GERENTE o ADMINISTRADOR")
    private String rol;

    @NotEmpty(message = "Debe indicar al menos un permiso")
    private List<@NotBlank(message = "El permiso no puede estar vacío") String> permisos;

    @NotBlank(message = "Debe indicar quién realiza la modificación")
    private String modificadoPor;
}