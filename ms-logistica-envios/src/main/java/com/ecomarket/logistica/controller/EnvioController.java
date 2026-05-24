package com.ecomarket.logistica.controller;

import com.ecomarket.logistica.dto.CambioEstadoRequestDTO;
import com.ecomarket.logistica.dto.EnvioDTO;
import com.ecomarket.logistica.dto.IncidenciaRequestDTO;
import com.ecomarket.logistica.model.Envio;
import com.ecomarket.logistica.model.SeguimientoEnvio;
import com.ecomarket.logistica.service.LogisticaService;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/envios")
public class EnvioController {

    private final LogisticaService logisticaService;

    public EnvioController(LogisticaService logisticaService) {
        this.logisticaService = logisticaService;
    }

    private EntityModel<Envio> ensamblar(Envio envio) {
        return EntityModel.of(envio,
                linkTo(methodOn(EnvioController.class).obtenerPorId(envio.getId())).withSelfRel(),
                linkTo(methodOn(EnvioController.class).obtenerTodos()).withRel("envios"),
                linkTo(methodOn(EnvioController.class).obtenerSeguimiento(envio.getId())).withRel("seguimiento")
        );
    }

    @PostMapping
    public ResponseEntity<EntityModel<Envio>> crear(@Valid @RequestBody EnvioDTO dto) {
        Envio creado = logisticaService.crearEnvio(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ensamblar(creado));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Envio>>> obtenerTodos() {
        List<EntityModel<Envio>> envios = logisticaService.obtenerEnvios().stream()
                .map(this::ensamblar)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(envios, linkTo(methodOn(EnvioController.class).obtenerTodos()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Envio>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ensamblar(logisticaService.obtenerEnvioPorId(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Envio>> actualizar(@PathVariable Long id, @RequestBody EnvioDTO dto) {
        return ResponseEntity.ok(ensamblar(logisticaService.actualizarEnvio(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logisticaService.eliminarEnvio(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<CollectionModel<EntityModel<Envio>>> obtenerPorPedido(@PathVariable Long idPedido) {
        List<EntityModel<Envio>> envios = logisticaService.obtenerEnviosPorPedido(idPedido).stream()
                .map(this::ensamblar)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(envios));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<EntityModel<Envio>> cambiarEstado(@PathVariable Long id, @Valid @RequestBody CambioEstadoRequestDTO dto) {
        return ResponseEntity.ok(ensamblar(logisticaService.cambiarEstadoEnvio(id, dto)));
    }

    @PatchMapping("/{id}/incidencia")
    public ResponseEntity<EntityModel<Envio>> registrarIncidencia(@PathVariable Long id, @Valid @RequestBody IncidenciaRequestDTO dto) {
        return ResponseEntity.ok(ensamblar(logisticaService.registrarIncidencia(id, dto)));
    }

    @GetMapping("/{id}/seguimiento")
    public ResponseEntity<CollectionModel<EntityModel<SeguimientoEnvio>>> obtenerSeguimiento(
            @PathVariable Long id) {

        List<EntityModel<SeguimientoEnvio>> seguimiento = logisticaService.obtenerSeguimiento(id)
                .stream()
                .map(s -> EntityModel.of(s,
                        linkTo(methodOn(EnvioController.class).obtenerSeguimiento(id)).withRel("seguimiento"),
                        linkTo(methodOn(EnvioController.class).obtenerPorId(id)).withRel("envio")))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<SeguimientoEnvio>> collection = CollectionModel.of(
                seguimiento,
                linkTo(methodOn(EnvioController.class).obtenerSeguimiento(id)).withSelfRel(),
                linkTo(methodOn(EnvioController.class).obtenerPorId(id)).withRel("envio")
        );

        return ResponseEntity.ok(collection);
    }
}
