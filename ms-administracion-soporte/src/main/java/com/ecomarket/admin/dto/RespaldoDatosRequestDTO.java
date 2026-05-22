package com.ecomarket.admin.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RespaldoDatosRequestDTO {

    @NotBlank(message = "El origen de datos es obligatorio")
    @Size(max = 120, message = "El origen de datos no puede superar 120 caracteres")
    private String origenDatos;

    @NotBlank(message = "La frecuencia es obligatoria")
    @Size(max = 80, message = "La frecuencia no puede superar 80 caracteres")
    private String frecuencia;

    @NotBlank(message = "El responsable es obligatorio")
    @Size(max = 120, message = "El responsable no puede superar 120 caracteres")
    private String responsable;

    @NotNull(message = "La fecha programada es obligatoria")
    @FutureOrPresent(message = "La fecha programada debe ser actual o futura")
    private LocalDateTime fechaProgramada;
}
