package com.ecomarket.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioInternoUpdateDTO {

    @Size(min = 3, max = 120, message = "El nombre debe tener entre 3 y 120 caracteres")
    private String nombre;

    @Email(message = "El correo debe tener un formato válido")
    private String correo;

    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String password;

    @Pattern(regexp = "EMPLEADO|GERENTE|ADMINISTRADOR", message = "El rol debe ser EMPLEADO, GERENTE o ADMINISTRADOR")
    private String rol;
}