package com.ecomarket.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RespuestaSoporteRequestDTO {

    @NotNull(message = "El id del ticket es obligatorio")
    private Long idTicket;

    @NotBlank(message = "El mensaje de respuesta es obligatorio")
    @Size(max = 1000, message = "El mensaje no puede superar 1000 caracteres")
    private String mensaje;

    @NotBlank(message = "El responsable de la respuesta es obligatorio")
    @Size(max = 120, message = "El responsable no puede superar 120 caracteres")
    private String respondidoPor;
}
