package com.ecomarket.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarPerfilClienteRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 120, message = "El nombre debe tener entre 3 y 120 caracteres")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato válido")
    private String correo;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(min = 8, max = 30, message = "El teléfono debe tener entre 8 y 30 caracteres")
    private String telefono;

    @NotBlank(message = "La dirección de envío es obligatoria")
    @Size(max = 255, message = "La dirección de envío no puede superar 255 caracteres")
    private String direccionEnvio;

    @Size(max = 80, message = "El medio de pago no puede superar 80 caracteres")
    private String medioPago;
}
