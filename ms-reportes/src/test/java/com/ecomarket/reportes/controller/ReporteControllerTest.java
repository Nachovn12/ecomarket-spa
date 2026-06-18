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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
}
