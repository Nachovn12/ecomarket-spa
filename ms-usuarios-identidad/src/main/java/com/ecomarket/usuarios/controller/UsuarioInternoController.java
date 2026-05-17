package com.ecomarket.usuarios.controller;

import com.ecomarket.usuarios.dto.UsuarioInternoRequestDTO;
import com.ecomarket.usuarios.dto.UsuarioInternoResponseDTO;
import com.ecomarket.usuarios.dto.UsuarioInternoUpdateDTO;
import com.ecomarket.usuarios.exception.AccesoNoAutorizadoException;
import com.ecomarket.usuarios.exception.UsuarioNoEncontradoException;
import com.ecomarket.usuarios.exception.UsuarioYaExisteException;
import com.ecomarket.usuarios.service.UsuarioInternoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios/internos")
@RequiredArgsConstructor
public class UsuarioInternoController {

    private final UsuarioInternoService usuarioInternoService;

    @PostMapping
    public ResponseEntity<UsuarioInternoResponseDTO> crearUsuarioInterno(
            @RequestHeader(value = "X-Rol-Usuario", required = false) String rolSolicitante,
            @Valid @RequestBody UsuarioInternoRequestDTO request) {

        UsuarioInternoResponseDTO response = usuarioInternoService.crearUsuarioInterno(request, rolSolicitante);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioInternoResponseDTO>> listarUsuariosInternos(
            @RequestHeader(value = "X-Rol-Usuario", required = false) String rolSolicitante) {

        return ResponseEntity.ok(usuarioInternoService.listarUsuariosInternos(rolSolicitante));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioInternoResponseDTO> actualizarUsuarioInterno(
            @PathVariable Long id,
            @RequestHeader(value = "X-Rol-Usuario", required = false) String rolSolicitante,
            @Valid @RequestBody UsuarioInternoUpdateDTO request) {

        return ResponseEntity.ok(usuarioInternoService.actualizarUsuarioInterno(id, request, rolSolicitante));
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<UsuarioInternoResponseDTO> desactivarUsuarioInterno(
            @PathVariable Long id,
            @RequestHeader(value = "X-Rol-Usuario", required = false) String rolSolicitante) {

        return ResponseEntity.ok(usuarioInternoService.desactivarUsuarioInterno(id, rolSolicitante));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UsuarioInternoResponseDTO> eliminarUsuarioInterno(
            @PathVariable Long id,
            @RequestHeader(value = "X-Rol-Usuario", required = false) String rolSolicitante) {

        return ResponseEntity.ok(usuarioInternoService.eliminarUsuarioInterno(id, rolSolicitante));
    }

    @ExceptionHandler(AccesoNoAutorizadoException.class)
    public ResponseEntity<String> manejarAccesoNoAutorizado(AccesoNoAutorizadoException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<String> manejarUsuarioNoEncontrado(UsuarioNoEncontradoException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(UsuarioYaExisteException.class)
    public ResponseEntity<String> manejarUsuarioYaExiste(UsuarioYaExisteException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> manejarSolicitudInvalida(IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}