package com.ecomarket.reportes.controller;

import com.ecomarket.reportes.dto.IndicadorKPIResponseDTO;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        return kpi;
    }

    private IndicadorKPIResponseDTO buildKpiDTO(Long id, String tipo, double valor) {
        IndicadorKPIResponseDTO dto = new IndicadorKPIResponseDTO();
        dto.setId(id);
        dto.setTipo(tipo);
        dto.setValor(valor);
        dto.setDescripcion("KPI de prueba");
        return dto;
    }

    // AC-4: GET /api/v1/kpis → 200 + lista de KPIs con campos correctos
    @Test
    void getKpis_retorna200ConListaDeKPIs() throws Exception {
        IndicadorKPI kpi = buildKpi(1L, TipoKPI.VENTAS_TOTALES, 1500.0);
        IndicadorKPIResponseDTO dto = buildKpiDTO(1L, "VENTAS_TOTALES", 1500.0);
        when(reporteService.listarKPIs()).thenReturn(List.of(kpi));
        when(reporteService.toDTO(kpi)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/kpis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].tipo").value("VENTAS_TOTALES"))
                .andExpect(jsonPath("$[0].valor").value(1500.0));
    }

    // AC-4: GET /api/v1/kpis/{id} → 200 + datos del KPI
    @Test
    void getKpiPorId_conIdValido_retorna200ConKPI() throws Exception {
        IndicadorKPI kpi = buildKpi(1L, TipoKPI.VENTAS_TOTALES, 1500.0);
        IndicadorKPIResponseDTO dto = buildKpiDTO(1L, "VENTAS_TOTALES", 1500.0);
        when(reporteService.obtenerKPIPorId(1L)).thenReturn(kpi);
        when(reporteService.toDTO(kpi)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/kpis/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.tipo").value("VENTAS_TOTALES"))
                .andExpect(jsonPath("$.valor").value(1500.0));
    }

    // AC-5: POST /api/v1/kpis con body válido → 201 + datos del KPI creado
    @Test
    void postKpi_conBodyValido_retorna201ConKPICreado() throws Exception {
        IndicadorKPI creado = buildKpi(1L, TipoKPI.VENTAS_TOTALES, 1500.0);
        IndicadorKPIResponseDTO dto = buildKpiDTO(1L, "VENTAS_TOTALES", 1500.0);
        when(reporteService.crearKPI(any(IndicadorKPI.class))).thenReturn(creado);
        when(reporteService.toDTO(creado)).thenReturn(dto);

        String body = "{\"tipo\":\"VENTAS_TOTALES\",\"valor\":1500.0}";

        mockMvc.perform(post("/api/v1/kpis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.tipo").value("VENTAS_TOTALES"));
    }

    // AC-4: GET /api/v1/kpis/{id} inexistente → 404
    @Test
    void getKpiPorId_conIdInexistente_retorna404() throws Exception {
        when(reporteService.obtenerKPIPorId(99L))
                .thenThrow(new com.ecomarket.reportes.exception.ReporteNotFoundException(
                        "IndicadorKPI no encontrado con id: 99"));

        mockMvc.perform(get("/api/v1/kpis/99"))
                .andExpect(status().isNotFound());
    }
}
