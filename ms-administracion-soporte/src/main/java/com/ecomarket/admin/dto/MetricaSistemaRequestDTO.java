package com.ecomarket.admin.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetricaSistemaRequestDTO {

    @NotBlank(message = "El microservicio es obligatorio")
    @Size(max = 80, message = "El microservicio no puede superar 80 caracteres")
    private String microservicio;

    @NotNull(message = "La disponibilidad es obligatoria")
    private Boolean disponible;

    @NotNull(message = "El tiempo de respuesta es obligatorio")
    @Min(value = 0, message = "El tiempo de respuesta no puede ser negativo")
    private Long tiempoRespuestaMs;

    @NotNull(message = "Los errores detectados son obligatorios")
    @Min(value = 0, message = "Los errores detectados no pueden ser negativos")
    private Integer erroresDetectados;
}
