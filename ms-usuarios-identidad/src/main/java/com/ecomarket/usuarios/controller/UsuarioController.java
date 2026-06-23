package com.ecomarket.usuarios.controller;

import com.ecomarket.usuarios.dto.ActualizarPerfilClienteRequestDTO;
import com.ecomarket.usuarios.dto.PerfilClienteResponseDTO;
import com.ecomarket.usuarios.dto.UsuarioRequestDTO;
import com.ecomarket.usuarios.dto.UsuarioResponseDTO;
import com.ecomarket.usuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios (Clientes)", description = "Registro, perfil y actualizacion de clientes")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Registrar un nuevo cliente",
            description = "Crea la cuenta de un cliente. Valida email y RUN unicos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente registrado",
                    content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Email o RUN ya registrados", content = @Content)
    })
    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponseDTO> registrarCliente(@Valid @RequestBody UsuarioRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrarCliente(request));
    }

    @Operation(summary = "Obtener el perfil de un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil del cliente",
                    content = @Content(schema = @Schema(implementation = PerfilClienteResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    @GetMapping("/clientes/{idCliente}/perfil")
    public ResponseEntity<PerfilClienteResponseDTO> obtenerPerfilCliente(
            @Parameter(description = "ID del cliente", example = "10", required = true) @PathVariable Long idCliente) {
        return ResponseEntity.ok(usuarioService.obtenerPerfilCliente(idCliente));
    }

    @Operation(summary = "Actualizar el perfil de un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil actualizado",
                    content = @Content(schema = @Schema(implementation = PerfilClienteResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    @PutMapping("/clientes/{idCliente}/perfil")
    public ResponseEntity<PerfilClienteResponseDTO> actualizarPerfilCliente(
            @Parameter(description = "ID del cliente", example = "10", required = true) @PathVariable Long idCliente,
            @Valid @RequestBody ActualizarPerfilClienteRequestDTO request) {
        return ResponseEntity.ok(usuarioService.actualizarPerfilCliente(idCliente, request));
    }
}
