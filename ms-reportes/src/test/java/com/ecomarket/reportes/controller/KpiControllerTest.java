package com.ecomarket.reportes.controller;

import com.ecomarket.reportes.dto.IndicadorKPIResponseDTO;
import com.ecomarket.reportes.exception.ReporteNotFoundException;
import com.ecomarket.reportes.model.IndicadorKPI;
import com.ecomarket.reportes.model.TipoKPI;
import com.ecomarket.reportes.service.ReporteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KpiController.class)
@TestPropertySource(properties = {
        "springdoc.api-docs.enabled=false",
        "springdoc.swagger-ui.enabled=false"
})
class KpiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReporteService reporteService;

    private IndicadorKPI buildKpi(Long id, TipoKPI tipo, double valor) {
        IndicadorKPI kpi = new IndicadorKPI();
        kpi.setId(id);
        kpi.setTipo(tipo);
        kpi.setValor(valor);
        kpi.setDescripcion(descripcionPorTipo(tipo));
        return kpi;
    }

    private String descripcionPorTipo(TipoKPI tipo) {
        return switch (tipo) {
            case VENTAS_TOTALES -> "Ventas totales mes de Junio - Sucursal Alameda";
            case STOCK_BAJO -> "Alerta: Harina Panadera 1KG bajo stock critico";
            case RENDIMIENTO_TIENDA -> "Eficiencia operativa Sucursal La Florida";
            case ROTACION_INVENTARIO -> "Tasa de rotacion mensual abarrotes";
            case PEDIDOS_ENTREGADOS -> "Pedidos online despachados exitosamente";
        };
    }

    private IndicadorKPIResponseDTO buildDTO(Long id, String tipo, double valor) {
        IndicadorKPIResponseDTO dto = new IndicadorKPIResponseDTO();
        dto.setId(id);
        dto.setTipo(tipo);
        dto.setValor(valor);
        dto.setDescripcion("Ventas totales mes de Junio - Sucursal Alameda");
        dto.setFechaCalculo(LocalDateTime.of(2026, 6, 22, 10, 0));
        return dto;
    }

    @Test
    void listarKpis_retornaOk() throws Exception {
        IndicadorKPI kpi = buildKpi(1L, TipoKPI.VENTAS_TOTALES, 15500000.0);
        IndicadorKPIResponseDTO dto = buildDTO(1L, "VENTAS_TOTALES", 15500000.0);
        when(reporteService.listarKPIs()).thenReturn(List.of(kpi));
        when(reporteService.toDTO(kpi)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/kpis")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerKpiPorId_existente_retornaOkConCampos() throws Exception {
        IndicadorKPI kpi = buildKpi(1L, TipoKPI.VENTAS_TOTALES, 15500000.0);
        IndicadorKPIResponseDTO dto = buildDTO(1L, "VENTAS_TOTALES", 15500000.0);
        when(reporteService.obtenerKPIPorId(1L)).thenReturn(kpi);
        when(reporteService.toDTO(kpi)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/kpis/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipo").value("VENTAS_TOTALES"))
                .andExpect(jsonPath("$.valor").value(15500000.0));
    }

    @Test
    void obtenerKpiPorId_inexistente_retorna404() throws Exception {
        when(reporteService.obtenerKPIPorId(999L))
                .thenThrow(new ReporteNotFoundException("IndicadorKPI no encontrado con id: 999"));

        mockMvc.perform(get("/api/v1/kpis/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void crearKpi_valido_retorna201ConCampos() throws Exception {
        IndicadorKPI creado = buildKpi(1L, TipoKPI.STOCK_BAJO, 14.0);
        IndicadorKPIResponseDTO dto = buildDTO(1L, "STOCK_BAJO", 14.0);
        when(reporteService.crearKPI(any(IndicadorKPI.class))).thenReturn(creado);
        when(reporteService.toDTO(creado)).thenReturn(dto);

        String body = "{\"tipo\":\"STOCK_BAJO\",\"valor\":14.0,\"descripcion\":\"Alerta: Harina Panadera 1KG bajo stock critico\"}";

        mockMvc.perform(post("/api/v1/kpis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipo").value("STOCK_BAJO"))
                .andExpect(jsonPath("$.valor").value(14.0));
    }

    @Test
    void eliminarKpi_existente_retorna204() throws Exception {
        doNothing().when(reporteService).eliminarKPI(1L);

        mockMvc.perform(delete("/api/v1/kpis/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarKpi_inexistente_retorna404() throws Exception {
        doThrow(new ReporteNotFoundException("IndicadorKPI no encontrado con id: 99"))
                .when(reporteService).eliminarKPI(99L);

        mockMvc.perform(delete("/api/v1/kpis/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listarKpisPorTipo_retornaOk() throws Exception {
        IndicadorKPI kpi = buildKpi(1L, TipoKPI.STOCK_BAJO, 14.0);
        IndicadorKPIResponseDTO dto = buildDTO(1L, "STOCK_BAJO", 14.0);
        when(reporteService.listarKPIsPorTipo(TipoKPI.STOCK_BAJO)).thenReturn(List.of(kpi));
        when(reporteService.toDTO(kpi)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/kpis/tipo/STOCK_BAJO")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
