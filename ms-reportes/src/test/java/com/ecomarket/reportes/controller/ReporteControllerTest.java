package com.ecomarket.reportes.controller;

import com.ecomarket.reportes.model.Reporte;
import com.ecomarket.reportes.model.TipoReporte;
import com.ecomarket.reportes.service.ReporteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import com.ecomarket.reportes.dto.ReporteInventarioDTO;
import com.ecomarket.reportes.dto.ReporteRendimientoDTO;
import com.ecomarket.reportes.dto.ReporteVentasDTO;
import com.ecomarket.reportes.dto.ReporteFiltroRequestDTO;
import com.ecomarket.reportes.exception.ReporteNotFoundException;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReporteController.class)
@TestPropertySource(properties = {
        "springdoc.api-docs.enabled=false",
        "springdoc.swagger-ui.enabled=false"
})
class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReporteService reporteService;

    private Reporte buildReporte(Long id, TipoReporte tipo) {
        Reporte r = new Reporte();
        r.setId(id);
        r.setTipo(tipo);
        r.setIdTienda(1L);
        return r;
    }

    // AC-3: GET /api/v1/reportes → 200 + lista de reportes
    @Test
    void getReportes_retorna200ConListaDeReportes() throws Exception {
        when(reporteService.listarReportes()).thenReturn(List.of(buildReporte(1L, TipoReporte.VENTAS)));

        mockMvc.perform(get("/api/v1/reportes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].tipo").value("VENTAS"))
                .andExpect(jsonPath("$[0].idTienda").value(1L));
    }

    // AC-3: GET /api/v1/reportes/{id} → 200 + datos del reporte
    @Test
    void getReportePorId_conIdValido_retorna200ConReporte() throws Exception {
        when(reporteService.obtenerReportePorId(1L)).thenReturn(buildReporte(1L, TipoReporte.VENTAS));

        mockMvc.perform(get("/api/v1/reportes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.tipo").value("VENTAS"))
                .andExpect(jsonPath("$.idTienda").value(1L));
    }

    // AC-5: POST /api/v1/reportes con body válido → 201 + datos del reporte creado
    @Test
    void postReporte_conBodyValido_retorna201ConReporteCreado() throws Exception {
        Reporte creado = buildReporte(1L, TipoReporte.VENTAS);
        when(reporteService.crearReporte(any(Reporte.class))).thenReturn(creado);

        String body = "{\"tipo\":\"VENTAS\",\"idTienda\":1}";

        mockMvc.perform(post("/api/v1/reportes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.tipo").value("VENTAS"));
    }

    // AC-3: POST /api/v1/reportes/ventas con fechaInicio nula → 400 (Bean Validation)
    @Test
    void postReporteVentas_conFechaInicioNula_retorna400() throws Exception {
        // ReporteFiltroRequestDTO tiene @NotNull en fechaInicio y fechaFin
        String bodyInvalido = "{\"idTienda\":1,\"fechaFin\":\"2026-06-30\"}";

        mockMvc.perform(post("/api/v1/reportes/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyInvalido))
                .andExpect(status().isBadRequest());
    }

    // AC-5: DELETE /api/v1/reportes/{id} con id existente → 204
    @Test
    void deleteReporte_conIdExistente_retorna204() throws Exception {
        doNothing().when(reporteService).eliminarReporte(1L);

        mockMvc.perform(delete("/api/v1/reportes/1"))
                .andExpect(status().isNoContent());
    }

    // AC-5: DELETE /api/v1/reportes/{id} con id inexistente → 404
    @Test
    void deleteReporte_conIdInexistente_retorna404() throws Exception {
        doThrow(new ReporteNotFoundException("Reporte no encontrado con id: 99"))
                .when(reporteService).eliminarReporte(99L);

        mockMvc.perform(delete("/api/v1/reportes/99"))
                .andExpect(status().isNotFound());
    }

    // AC-3: GET /api/v1/reportes/tipo/{tipo} → 200 + lista de reportes del tipo
    @Test
    void getReportesPorTipo_retorna200ConListaFiltrada() throws Exception {
        when(reporteService.listarPorTipo(TipoReporte.INVENTARIO))
                .thenReturn(List.of(buildReporte(2L, TipoReporte.INVENTARIO)));

        mockMvc.perform(get("/api/v1/reportes/tipo/INVENTARIO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("INVENTARIO"));
    }

    // AC-3: GET /api/v1/reportes/tienda/{idTienda} → 200 + lista de reportes de la tienda
    @Test
    void getReportesPorTienda_retorna200ConListaDeTienda() throws Exception {
        when(reporteService.listarPorTienda(3L))
                .thenReturn(List.of(buildReporte(1L, TipoReporte.VENTAS), buildReporte(2L, TipoReporte.INVENTARIO)));

        mockMvc.perform(get("/api/v1/reportes/tienda/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    // AC-1: POST /api/v1/reportes/inventario/{idTienda} → 201 + DTO de inventario
    @Test
    void postReporteInventario_retorna201ConDTO() throws Exception {
        ReporteInventarioDTO dto = new ReporteInventarioDTO();
        dto.setIdTienda(1L);
        dto.setProductosDisponibles(100);
        dto.setProductosBajoStock(3);
        dto.setProductosSinStock(0);
        when(reporteService.generarReporteInventario(eq(1L))).thenReturn(dto);

        mockMvc.perform(post("/api/v1/reportes/inventario/1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idTienda").value(1L))
                .andExpect(jsonPath("$.productosBajoStock").value(3));
    }

    // AC-1: POST /api/v1/reportes/rendimiento con filtro válido → 201 + DTO de rendimiento
    @Test
    void postReporteRendimiento_conFiltroValido_retorna201() throws Exception {
        ReporteRendimientoDTO dto = new ReporteRendimientoDTO();
        dto.setIdTienda(1L);
        dto.setVentasPorTienda(120000.0);
        dto.setPedidosEntregados(47);
        dto.setRendimientoOperativo(0.85);

        when(reporteService.generarReporteRendimiento(any(ReporteFiltroRequestDTO.class))).thenReturn(dto);

        String body = "{\"idTienda\":1,\"fechaInicio\":\"2026-06-01\",\"fechaFin\":\"2026-06-30\"}";

        mockMvc.perform(post("/api/v1/reportes/rendimiento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ventasPorTienda").value(120000.0))
                .andExpect(jsonPath("$.rendimientoOperativo").value(0.85));
    }

    // AC-1: POST /api/v1/reportes/ventas con filtro válido → 201 + DTO de ventas
    @Test
    void postReporteVentas_conFiltroValido_retorna201() throws Exception {
        ReporteVentasDTO dto = new ReporteVentasDTO();
        dto.setIdTienda(1L);
        dto.setVentasTotales(80000.0);
        dto.setTotalTransacciones(4);
        dto.setProductosVendidos(20);

        when(reporteService.generarReporteVentas(any(ReporteFiltroRequestDTO.class))).thenReturn(dto);

        String body = "{\"idTienda\":1,\"fechaInicio\":\"2026-06-01\",\"fechaFin\":\"2026-06-30\"}";

        mockMvc.perform(post("/api/v1/reportes/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ventasTotales").value(80000.0))
                .andExpect(jsonPath("$.totalTransacciones").value(4));
    }
}
