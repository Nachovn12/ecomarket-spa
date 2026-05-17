package com.ecomarket.usuarios.controller;

import com.ecomarket.usuarios.dto.LoginRequestDTO;
import com.ecomarket.usuarios.dto.LoginResponseDTO;
import com.ecomarket.usuarios.exception.CredencialesInvalidasException;
import com.ecomarket.usuarios.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> iniciarSesion(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authService.iniciarSesion(request);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<String> manejarCredencialesInvalidas(CredencialesInvalidasException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    }
}