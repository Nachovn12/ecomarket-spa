package com.ecomarket.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class TiendaRequestDTO {

    @NotBlank(message = "El nombre de la tienda es obligatorio")
    @Size(max = 120, message = "El nombre no puede superar 120 caracteres")
    private String nombre;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 80, message = "La ciudad no puede superar 80 caracteres")
    private String ciudad;

    @NotNull(message = "El horario de apertura es obligatorio")
    private LocalTime horarioApertura;

    @NotNull(message = "El horario de cierre es obligatorio")
    private LocalTime horarioCierre;

    @Size(max = 500, message = "Las políticas locales no pueden superar 500 caracteres")
    private String politicasLocales;
}
