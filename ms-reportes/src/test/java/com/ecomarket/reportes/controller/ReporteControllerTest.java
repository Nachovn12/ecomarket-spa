package com.ecomarket.reportes.controller;

import com.ecomarket.reportes.dto.ReporteFiltroRequestDTO;
import com.ecomarket.reportes.dto.ReporteInventarioDTO;
import com.ecomarket.reportes.dto.ReporteRendimientoDTO;
import com.ecomarket.reportes.dto.ReporteVentasDTO;
import com.ecomarket.reportes.exception.ReporteException;
import com.ecomarket.reportes.exception.ReporteNotFoundException;
import com.ecomarket.reportes.model.Reporte;
import com.ecomarket.reportes.model.TipoReporte;
import com.ecomarket.reportes.service.ReporteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private Reporte buildReporte(Long id, TipoReporte tipo, Long idTienda) {
        Reporte r = new Reporte();
        r.setId(id);
        r.setTipo(tipo);
        r.setIdTienda(idTienda);
        return r;
    }

    @Test
    void listarReportes_retornaOk() throws Exception {
        Reporte reporte = buildReporte(1L, TipoReporte.VENTAS, 1L);
        when(reporteService.listarReportes()).thenReturn(List.of(reporte));

        mockMvc.perform(get("/api/v1/reportes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerReportePorId_existente_retornaOkConCampos() throws Exception {
        Reporte reporte = buildReporte(1L, TipoReporte.VENTAS, 1L);
        when(reporteService.obtenerReportePorId(1L)).thenReturn(reporte);

        mockMvc.perform(get("/api/v1/reportes/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipo").value("VENTAS"))
                .andExpect(jsonPath("$.idTienda").value(1));
    }

    @Test
    void obtenerReportePorId_inexistente_retorna404() throws Exception {
        when(reporteService.obtenerReportePorId(999L))
                .thenThrow(new ReporteNotFoundException("Reporte no encontrado con id: 999"));

        mockMvc.perform(get("/api/v1/reportes/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void crearReporte_valido_retorna201ConCampos() throws Exception {
        Reporte entrada = buildReporte(null, TipoReporte.INVENTARIO, 2L);
        Reporte creado = buildReporte(3L, TipoReporte.INVENTARIO, 2L);
        when(reporteService.crearReporte(any(Reporte.class))).thenReturn(creado);

        mockMvc.perform(post("/api/v1/reportes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.tipo").value("INVENTARIO"));
    }

    @Test
    void eliminarReporte_existente_retorna204() throws Exception {
        doNothing().when(reporteService).eliminarReporte(1L);

        mockMvc.perform(delete("/api/v1/reportes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarReporte_inexistente_retorna404() throws Exception {
        doThrow(new ReporteNotFoundException("Reporte no encontrado con id: 99"))
                .when(reporteService).eliminarReporte(99L);

        mockMvc.perform(delete("/api/v1/reportes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void generarReporteVentas_valido_retorna201ConVentasTotales() throws Exception {
        ReporteFiltroRequestDTO filtro = new ReporteFiltroRequestDTO();
        filtro.setIdTienda(1L);
        filtro.setFechaInicio(LocalDate.of(2026, 6, 1));
        filtro.setFechaFin(LocalDate.of(2026, 6, 30));

        ReporteVentasDTO dto = new ReporteVentasDTO();
        dto.setIdTienda(1L);
        dto.setFechaInicio(filtro.getFechaInicio());
        dto.setFechaFin(filtro.getFechaFin());
        dto.setVentasTotales(8500000.0);
        dto.setTotalTransacciones(47);
        dto.setProductosVendidos(235);

        when(reporteService.generarReporteVentas(any(ReporteFiltroRequestDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/v1/reportes/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filtro)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idTienda").value(1))
                .andExpect(jsonPath("$.ventasTotales").value(8500000.0))
                .andExpect(jsonPath("$.totalTransacciones").value(47));
    }

    @Test
    void generarReporteInventario_valido_retorna201() throws Exception {
        ReporteInventarioDTO dto = new ReporteInventarioDTO();
        dto.setIdTienda(1L);
        dto.setProductosDisponibles(80);
        dto.setProductosBajoStock(3);
        dto.setProductosSinStock(0);

        when(reporteService.generarReporteInventario(1L)).thenReturn(dto);

        mockMvc.perform(post("/api/v1/reportes/inventario/1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idTienda").value(1))
                .andExpect(jsonPath("$.productosBajoStock").value(3));
    }

    @Test
    void generarReporteRendimiento_valido_retorna201() throws Exception {
        ReporteFiltroRequestDTO filtro = new ReporteFiltroRequestDTO();
        filtro.setIdTienda(1L);
        filtro.setFechaInicio(LocalDate.of(2026, 6, 1));
        filtro.setFechaFin(LocalDate.of(2026, 6, 30));

        ReporteRendimientoDTO dto = new ReporteRendimientoDTO();
        dto.setIdTienda(1L);
        dto.setFechaInicio(filtro.getFechaInicio());
        dto.setFechaFin(filtro.getFechaFin());
        dto.setVentasPorTienda(8500000.0);
        dto.setPedidosEntregados(47);
        dto.setStockBajo(14);
        dto.setRendimientoOperativo(0.92);

        when(reporteService.generarReporteRendimiento(any(ReporteFiltroRequestDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/v1/reportes/rendimiento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filtro)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idTienda").value(1))
                .andExpect(jsonPath("$.rendimientoOperativo").value(0.92));
    }

    @Test
    void listarPorTipo_retornaOk() throws Exception {
        Reporte reporte = buildReporte(1L, TipoReporte.VENTAS, 1L);
        when(reporteService.listarPorTipo(TipoReporte.VENTAS)).thenReturn(List.of(reporte));

        mockMvc.perform(get("/api/v1/reportes/tipo/VENTAS")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void listarPorTienda_retornaOk() throws Exception {
        Reporte reporte = buildReporte(1L, TipoReporte.VENTAS, 1L);
        when(reporteService.listarPorTienda(1L)).thenReturn(List.of(reporte));

        mockMvc.perform(get("/api/v1/reportes/tienda/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void generarReporteVentas_bodyVacio_retorna400() throws Exception {
        mockMvc.perform(post("/api/v1/reportes/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void generarReporteVentas_jsonMalformado_retorna400() throws Exception {
        mockMvc.perform(post("/api/v1/reportes/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void generarReporteVentas_serviceLanzaReporteException_retorna400() throws Exception {
        ReporteFiltroRequestDTO filtro = new ReporteFiltroRequestDTO();
        filtro.setIdTienda(1L);
        filtro.setFechaInicio(LocalDate.of(2026, 6, 30));
        filtro.setFechaFin(LocalDate.of(2026, 6, 1));

        when(reporteService.generarReporteVentas(any(ReporteFiltroRequestDTO.class)))
                .thenThrow(new ReporteException("La fecha de inicio no puede ser posterior a la fecha de fin"));

        mockMvc.perform(post("/api/v1/reportes/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filtro)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}
