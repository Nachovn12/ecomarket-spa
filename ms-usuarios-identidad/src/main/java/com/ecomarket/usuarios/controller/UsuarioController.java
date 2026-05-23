package com.ecomarket.usuarios.controller;

import com.ecomarket.usuarios.dto.ActualizarPerfilClienteRequestDTO;
import com.ecomarket.usuarios.dto.PerfilClienteResponseDTO;
import com.ecomarket.usuarios.dto.UsuarioRequestDTO;
import com.ecomarket.usuarios.dto.UsuarioResponseDTO;
import com.ecomarket.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/registro")
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> registrarCliente(@Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO response = usuarioService.registrarCliente(request);

        EntityModel<UsuarioResponseDTO> model = EntityModel.of(response,
                linkTo(methodOn(UsuarioController.class).registrarCliente(request)).withSelfRel());

        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @GetMapping("/clientes/{idCliente}/perfil")
    public ResponseEntity<EntityModel<PerfilClienteResponseDTO>> obtenerPerfilCliente(@PathVariable Long idCliente) {
        PerfilClienteResponseDTO response = usuarioService.obtenerPerfilCliente(idCliente);

        EntityModel<PerfilClienteResponseDTO> model = EntityModel.of(response,
                linkTo(methodOn(UsuarioController.class).obtenerPerfilCliente(idCliente)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).actualizarPerfilCliente(idCliente, null)).withRel("actualizarPerfil"));

        return ResponseEntity.ok(model);
    }

    @PutMapping("/clientes/{idCliente}/perfil")
    public ResponseEntity<EntityModel<PerfilClienteResponseDTO>> actualizarPerfilCliente(
            @PathVariable Long idCliente,
            @Valid @RequestBody ActualizarPerfilClienteRequestDTO request) {

        PerfilClienteResponseDTO response = usuarioService.actualizarPerfilCliente(idCliente, request);

        EntityModel<PerfilClienteResponseDTO> model = EntityModel.of(response,
                linkTo(methodOn(UsuarioController.class).obtenerPerfilCliente(idCliente)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).actualizarPerfilCliente(idCliente, request)).withRel("actualizarPerfil"));

        return ResponseEntity.ok(model);
    }
}
