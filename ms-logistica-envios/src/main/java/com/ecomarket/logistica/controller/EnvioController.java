package com.ecomarket.logistica.controller;

import com.ecomarket.logistica.dto.EnvioDTO;
import com.ecomarket.logistica.model.Envio;
import com.ecomarket.logistica.model.enums.EstadoEnvio;
import com.ecomarket.logistica.service.LogisticaService;
import com.ecomarket.logistica.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/envios")
public class EnvioController {
    @Autowired
    private LogisticaService service;

    @PostMapping
    public ResponseEntity<EntityModel<Envio>> crear(@Valid @RequestBody EnvioDTO dto) {
        Envio guardado = service.crearEnvio(dto);
        return new ResponseEntity<>(ensamblar(guardado), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Envio>>> listarTodos() {
        List<EntityModel<Envio>> envios = service.obtenerEnvios().stream()
            .map(this::ensamblar)
            .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(envios, linkTo(methodOn(EnvioController.class).listarTodos()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Envio>> buscarPorId(@PathVariable Long id) {
        Envio envio = service.obtenerEnvioPorId(id)
            .orElseThrow(() -> new ResourceNotFoundException("Envio no encontrado con id: " + id));
        return ResponseEntity.ok(ensamblar(envio));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<EntityModel<Envio>> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        EstadoEnvio nuevoEstado;
        try {
            nuevoEstado = EstadoEnvio.valueOf(body.get("estado").toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Estado no valido. Use: PREPARACION, EN_TRANSITO, ENTREGADO, CANCELADO");
        }
        Envio actualizado = service.actualizarEstadoEnvio(id, nuevoEstado)
            .orElseThrow(() -> new ResourceNotFoundException("Envio no encontrado con id: " + id));
        return ResponseEntity.ok(ensamblar(actualizado));
    }

    private EntityModel<Envio> ensamblar(Envio envio) {
        return EntityModel.of(envio,
            linkTo(methodOn(EnvioController.class).buscarPorId(envio.getIdEnvio())).withSelfRel(),
            linkTo(methodOn(EnvioController.class).listarTodos()).withRel("envios"));
    }
}
