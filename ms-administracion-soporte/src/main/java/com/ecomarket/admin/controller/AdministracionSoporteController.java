package com.ecomarket.admin.controller;

import com.ecomarket.admin.dto.*;
import com.ecomarket.admin.entity.EstadoTicket;
import com.ecomarket.admin.service.AdministracionSoporteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequiredArgsConstructor
public class AdministracionSoporteController {

    private final AdministracionSoporteService administracionSoporteService;

    @PostMapping("/api/admin/tiendas")
    public ResponseEntity<EntityModel<TiendaResponseDTO>> crearTienda(@Valid @RequestBody TiendaRequestDTO request) {
        TiendaResponseDTO response = administracionSoporteService.crearTienda(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelarTienda(response));
    }

    @GetMapping("/api/admin/tiendas")
    public ResponseEntity<CollectionModel<EntityModel<TiendaResponseDTO>>> listarTiendas() {
        var tiendas = administracionSoporteService.listarTiendas().stream()
                .map(this::modelarTienda)
                .toList();

        return ResponseEntity.ok(CollectionModel.of(tiendas,
                linkTo(methodOn(AdministracionSoporteController.class).listarTiendas()).withSelfRel()));
    }

    @GetMapping("/api/admin/tiendas/{idTienda}")
    public ResponseEntity<EntityModel<TiendaResponseDTO>> consultarTienda(@PathVariable Long idTienda) {
        return ResponseEntity.ok(modelarTienda(administracionSoporteService.consultarTienda(idTienda)));
    }

    @PutMapping("/api/admin/tiendas/{idTienda}")
    public ResponseEntity<EntityModel<TiendaResponseDTO>> actualizarTienda(
            @PathVariable Long idTienda,
            @Valid @RequestBody TiendaRequestDTO request) {

        return ResponseEntity.ok(modelarTienda(administracionSoporteService.actualizarTienda(idTienda, request)));
    }

    @PostMapping("/api/admin/tiendas/{idTienda}/personal")
    public ResponseEntity<EntityModel<AsignacionPersonalResponseDTO>> asignarPersonal(
            @PathVariable Long idTienda,
            @Valid @RequestBody AsignacionPersonalRequestDTO request) {

        request.setIdTienda(idTienda);
        AsignacionPersonalResponseDTO response = administracionSoporteService.asignarPersonal(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(modelarAsignacion(response));
    }

    @GetMapping("/api/admin/tiendas/{idTienda}/personal")
    public ResponseEntity<CollectionModel<EntityModel<AsignacionPersonalResponseDTO>>> listarPersonalPorTienda(
            @PathVariable Long idTienda) {

        var personal = administracionSoporteService.listarPersonalPorTienda(idTienda).stream()
                .map(this::modelarAsignacion)
                .toList();

        return ResponseEntity.ok(CollectionModel.of(personal,
                linkTo(methodOn(AdministracionSoporteController.class).listarPersonalPorTienda(idTienda)).withSelfRel(),
                linkTo(methodOn(AdministracionSoporteController.class).consultarTienda(idTienda)).withRel("tienda")));
    }

    @PostMapping("/api/soporte/tickets")
    public ResponseEntity<EntityModel<TicketSoporteResponseDTO>> crearTicketSoporte(
            @Valid @RequestBody TicketSoporteRequestDTO request) {

        TicketSoporteResponseDTO response = administracionSoporteService.crearTicketSoporte(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelarTicket(response));
    }

    @GetMapping("/api/soporte/tickets")
    public ResponseEntity<CollectionModel<EntityModel<TicketSoporteResponseDTO>>> listarTickets() {
        var tickets = administracionSoporteService.listarTickets().stream()
                .map(this::modelarTicket)
                .toList();

        return ResponseEntity.ok(CollectionModel.of(tickets,
                linkTo(methodOn(AdministracionSoporteController.class).listarTickets()).withSelfRel()));
    }

    @GetMapping("/api/soporte/tickets/{idTicket}")
    public ResponseEntity<EntityModel<TicketSoporteResponseDTO>> consultarTicket(@PathVariable Long idTicket) {
        return ResponseEntity.ok(modelarTicket(administracionSoporteService.consultarTicket(idTicket)));
    }

    @PatchMapping("/api/soporte/tickets/{idTicket}/estado")
    public ResponseEntity<EntityModel<TicketSoporteResponseDTO>> actualizarEstadoTicket(
            @PathVariable Long idTicket,
            @RequestParam EstadoTicket estado) {

        return ResponseEntity.ok(modelarTicket(administracionSoporteService.actualizarEstadoTicket(idTicket, estado)));
    }

    @PostMapping("/api/soporte/tickets/{idTicket}/respuestas")
    public ResponseEntity<EntityModel<RespuestaSoporteResponseDTO>> responderTicket(
            @PathVariable Long idTicket,
            @Valid @RequestBody RespuestaSoporteRequestDTO request) {

        request.setIdTicket(idTicket);
        RespuestaSoporteResponseDTO response = administracionSoporteService.responderTicket(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(modelarRespuesta(response));
    }

    @GetMapping("/api/soporte/tickets/{idTicket}/respuestas")
    public ResponseEntity<CollectionModel<EntityModel<RespuestaSoporteResponseDTO>>> listarRespuestasTicket(
            @PathVariable Long idTicket) {

        var respuestas = administracionSoporteService.listarRespuestasTicket(idTicket).stream()
                .map(this::modelarRespuesta)
                .toList();

        return ResponseEntity.ok(CollectionModel.of(respuestas,
                linkTo(methodOn(AdministracionSoporteController.class).listarRespuestasTicket(idTicket)).withSelfRel(),
                linkTo(methodOn(AdministracionSoporteController.class).consultarTicket(idTicket)).withRel("ticket")));
    }

    @PostMapping("/api/admin/monitorizacion/metricas")
    public ResponseEntity<EntityModel<MetricaSistemaResponseDTO>> registrarMetrica(
            @Valid @RequestBody MetricaSistemaRequestDTO request) {

        MetricaSistemaResponseDTO response = administracionSoporteService.registrarMetrica(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelarMetrica(response));
    }

    @GetMapping("/api/admin/monitorizacion/metricas")
    public ResponseEntity<CollectionModel<EntityModel<MetricaSistemaResponseDTO>>> listarMetricas() {
        var metricas = administracionSoporteService.listarMetricas().stream()
                .map(this::modelarMetrica)
                .toList();

        return ResponseEntity.ok(CollectionModel.of(metricas,
                linkTo(methodOn(AdministracionSoporteController.class).listarMetricas()).withSelfRel()));
    }

    @PostMapping("/api/admin/monitorizacion/alertas")
    public ResponseEntity<EntityModel<AlertaSistemaResponseDTO>> registrarAlerta(
            @Valid @RequestBody AlertaSistemaRequestDTO request) {

        AlertaSistemaResponseDTO response = administracionSoporteService.registrarAlerta(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelarAlerta(response));
    }

    @GetMapping("/api/admin/monitorizacion/alertas")
    public ResponseEntity<CollectionModel<EntityModel<AlertaSistemaResponseDTO>>> listarAlertasActivas() {
        var alertas = administracionSoporteService.listarAlertasActivas().stream()
                .map(this::modelarAlerta)
                .toList();

        return ResponseEntity.ok(CollectionModel.of(alertas,
                linkTo(methodOn(AdministracionSoporteController.class).listarAlertasActivas()).withSelfRel()));
    }

    @PatchMapping("/api/admin/monitorizacion/alertas/{idAlerta}/resolver")
    public ResponseEntity<EntityModel<AlertaSistemaResponseDTO>> resolverAlerta(@PathVariable Long idAlerta) {
        return ResponseEntity.ok(modelarAlerta(administracionSoporteService.resolverAlerta(idAlerta)));
    }

    @PostMapping("/api/admin/respaldos")
    public ResponseEntity<EntityModel<RespaldoDatosResponseDTO>> programarRespaldo(
            @Valid @RequestBody RespaldoDatosRequestDTO request) {

        RespaldoDatosResponseDTO response = administracionSoporteService.programarRespaldo(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelarRespaldo(response));
    }

    @GetMapping("/api/admin/respaldos")
    public ResponseEntity<CollectionModel<EntityModel<RespaldoDatosResponseDTO>>> listarRespaldos() {
        var respaldos = administracionSoporteService.listarRespaldos().stream()
                .map(this::modelarRespaldo)
                .toList();

        return ResponseEntity.ok(CollectionModel.of(respaldos,
                linkTo(methodOn(AdministracionSoporteController.class).listarRespaldos()).withSelfRel()));
    }

    @PatchMapping("/api/admin/respaldos/{idRespaldo}/ejecutar")
    public ResponseEntity<EntityModel<RespaldoDatosResponseDTO>> ejecutarRespaldo(@PathVariable Long idRespaldo) {
        return ResponseEntity.ok(modelarRespaldo(administracionSoporteService.ejecutarRespaldo(idRespaldo)));
    }

    @PatchMapping("/api/admin/respaldos/{idRespaldo}/restaurar")
    public ResponseEntity<EntityModel<RespaldoDatosResponseDTO>> restaurarRespaldo(@PathVariable Long idRespaldo) {
        return ResponseEntity.ok(modelarRespaldo(administracionSoporteService.restaurarRespaldo(idRespaldo)));
    }

    private EntityModel<TiendaResponseDTO> modelarTienda(TiendaResponseDTO tienda) {
        return EntityModel.of(tienda,
                linkTo(methodOn(AdministracionSoporteController.class).consultarTienda(tienda.getIdTienda())).withSelfRel(),
                linkTo(methodOn(AdministracionSoporteController.class).listarTiendas()).withRel("tiendas"),
                linkTo(methodOn(AdministracionSoporteController.class).listarPersonalPorTienda(tienda.getIdTienda())).withRel("personal"));
    }

    private EntityModel<AsignacionPersonalResponseDTO> modelarAsignacion(AsignacionPersonalResponseDTO asignacion) {
        return EntityModel.of(asignacion,
                linkTo(methodOn(AdministracionSoporteController.class).listarPersonalPorTienda(asignacion.getIdTienda())).withRel("personalTienda"),
                linkTo(methodOn(AdministracionSoporteController.class).consultarTienda(asignacion.getIdTienda())).withRel("tienda"));
    }

    private EntityModel<TicketSoporteResponseDTO> modelarTicket(TicketSoporteResponseDTO ticket) {
        return EntityModel.of(ticket,
                linkTo(methodOn(AdministracionSoporteController.class).consultarTicket(ticket.getIdTicket())).withSelfRel(),
                linkTo(methodOn(AdministracionSoporteController.class).listarTickets()).withRel("tickets"),
                linkTo(methodOn(AdministracionSoporteController.class).listarRespuestasTicket(ticket.getIdTicket())).withRel("respuestas"));
    }

    private EntityModel<RespuestaSoporteResponseDTO> modelarRespuesta(RespuestaSoporteResponseDTO respuesta) {
        return EntityModel.of(respuesta,
                linkTo(methodOn(AdministracionSoporteController.class).consultarTicket(respuesta.getIdTicket())).withRel("ticket"),
                linkTo(methodOn(AdministracionSoporteController.class).listarRespuestasTicket(respuesta.getIdTicket())).withRel("respuestasTicket"));
    }

    private EntityModel<MetricaSistemaResponseDTO> modelarMetrica(MetricaSistemaResponseDTO metrica) {
        return EntityModel.of(metrica,
                linkTo(methodOn(AdministracionSoporteController.class).listarMetricas()).withRel("metricas"),
                linkTo(methodOn(AdministracionSoporteController.class).listarAlertasActivas()).withRel("alertasActivas"));
    }

    private EntityModel<AlertaSistemaResponseDTO> modelarAlerta(AlertaSistemaResponseDTO alerta) {
        return EntityModel.of(alerta,
                linkTo(methodOn(AdministracionSoporteController.class).listarAlertasActivas()).withRel("alertasActivas"),
                linkTo(methodOn(AdministracionSoporteController.class).resolverAlerta(alerta.getIdAlerta())).withRel("resolver"));
    }

    private EntityModel<RespaldoDatosResponseDTO> modelarRespaldo(RespaldoDatosResponseDTO respaldo) {
        return EntityModel.of(respaldo,
                linkTo(methodOn(AdministracionSoporteController.class).listarRespaldos()).withRel("respaldos"),
                linkTo(methodOn(AdministracionSoporteController.class).ejecutarRespaldo(respaldo.getIdRespaldo())).withRel("ejecutar"),
                linkTo(methodOn(AdministracionSoporteController.class).restaurarRespaldo(respaldo.getIdRespaldo())).withRel("restaurar"));
    }
}
