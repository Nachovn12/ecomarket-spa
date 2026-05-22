package com.ecomarket.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AsignacionPersonalRequestDTO {

    @NotNull(message = "El id del usuario interno es obligatorio")
    private Long idUsuarioInterno;

    @NotNull(message = "El id de la tienda es obligatorio")
    private Long idTienda;

    @NotBlank(message = "El cargo es obligatorio")
    @Size(max = 80, message = "El cargo no puede superar 80 caracteres")
    private String cargo;
}
