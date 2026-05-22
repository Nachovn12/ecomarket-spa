package com.ecomarket.admin.dto;

import com.ecomarket.admin.entity.PrioridadTicket;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketSoporteRequestDTO {

    @NotBlank(message = "El asunto es obligatorio")
    @Size(max = 120, message = "El asunto no puede superar 120 caracteres")
    private String asunto;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 1000, message = "La descripción no puede superar 1000 caracteres")
    private String descripcion;

    @NotBlank(message = "El nombre de contacto es obligatorio")
    @Size(max = 120, message = "El nombre de contacto no puede superar 120 caracteres")
    private String nombreContacto;

    @NotBlank(message = "El correo de contacto es obligatorio")
    @Email(message = "El correo de contacto debe tener un formato válido")
    @Size(max = 120, message = "El correo de contacto no puede superar 120 caracteres")
    private String correoContacto;

    @NotNull(message = "La prioridad del ticket es obligatoria")
    private PrioridadTicket prioridad;
}
