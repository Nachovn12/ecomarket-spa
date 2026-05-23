package com.ecomarket.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlertaSistemaRequestDTO {

    @NotBlank(message = "El microservicio es obligatorio")
    @Size(max = 80, message = "El microservicio no puede superar 80 caracteres")
    private String microservicio;

    @NotBlank(message = "El tipo de alerta es obligatorio")
    @Size(max = 80, message = "El tipo de alerta no puede superar 80 caracteres")
    private String tipoAlerta;

    @NotBlank(message = "La descripción de la alerta es obligatoria")
    @Size(max = 500, message = "La descripción no puede superar 500 caracteres")
    private String descripcion;
}
