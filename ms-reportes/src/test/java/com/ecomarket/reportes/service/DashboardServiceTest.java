package com.ecomarket.reportes.service;

import com.ecomarket.reportes.dto.DashboardMetricasDTO;
import com.ecomarket.reportes.model.IndicadorKPI;
import com.ecomarket.reportes.model.TipoKPI;
import com.ecomarket.reportes.repository.IndicadorKPIRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private IndicadorKPIRepository indicadorKPIRepository;

    @InjectMocks
    private DashboardService dashboardService;

    private IndicadorKPI buildKpi(Long id, double valor) {
        IndicadorKPI kpi = new IndicadorKPI();
        kpi.setId(id);
        kpi.setTipo(TipoKPI.VENTAS_TOTALES);
        kpi.setValor(valor);
        return kpi;
    }

    // AC-6: ventas del día — suma correctamente los KPIs de VENTAS_TOTALES
    @Test
    void getVentasDelDia_sumaKPIsDeVentasTotales() {
        when(indicadorKPIRepository.findByTipo(TipoKPI.VENTAS_TOTALES))
                .thenReturn(List.of(buildKpi(1L, 1000.0), buildKpi(2L, 500.0)));

        Double resultado = dashboardService.getVentasDelDia();

        assertThat(resultado).isEqualTo(1500.0);
        verify(indicadorKPIRepository).findByTipo(TipoKPI.VENTAS_TOTALES);
    }

    // AC-6: productos más vendidos — lista ordenada descendente por valor
    @Test
    void getProductosMasVendidos_retornaListaOrdenadaDescendente() {
        when(indicadorKPIRepository.findByTipo(TipoKPI.VENTAS_TOTALES))
                .thenReturn(List.of(buildKpi(1L, 100.0), buildKpi(2L, 300.0), buildKpi(3L, 200.0)));

        List<IndicadorKPI> resultado = dashboardService.getProductosMasVendidos();

        assertThat(resultado).hasSize(3);
        assertThat(resultado.get(0).getValor()).isEqualTo(300.0);
        assertThat(resultado.get(1).getValor()).isEqualTo(200.0);
        assertThat(resultado.get(2).getValor()).isEqualTo(100.0);
    }

    // AC-7: tasa de conversión — dado 100 pedidos y 25 ventas concretadas retorna 25.00
    @Test
    void calcularTasaConversion_dado100Pedidosy25Ventas_retorna25_00() {
        BigDecimal resultado = dashboardService.calcularTasaConversion(100, 25);

        assertThat(resultado).isEqualByComparingTo(new BigDecimal("25.00"));
    }

    // AC-7: ticket promedio — totalVentas / transacciones con HALF_UP
    @Test
    void calcularTicketPromedio_dadoTotalYTransacciones_retornaPromedioCorrecto() {
        BigDecimal resultado = dashboardService.calcularTicketPromedio(1500.0, 3);

        assertThat(resultado).isEqualByComparingTo(new BigDecimal("500.00"));
    }

    // AC-6: getDashboardMetrics retorna DTO con los 4 campos no nulos
    @Test
    void getDashboardMetrics_retornaMetricasConCamposNoNulos() {
        when(indicadorKPIRepository.findByTipo(TipoKPI.VENTAS_TOTALES))
                .thenReturn(List.of(buildKpi(1L, 1000.0)));

        DashboardMetricasDTO dto = dashboardService.getDashboardMetrics();

        assertThat(dto.getVentasDelDia()).isNotNull();
        assertThat(dto.getProductosMasVendidos()).isNotNull();
        assertThat(dto.getTasaConversion()).isNotNull();
        assertThat(dto.getTicketPromedio()).isNotNull();
    }

    // Cobertura extra: calcularTasaConversion con 0 pedidos
    @Test
    void calcularTasaConversion_conCeroPedidos_retornaCero() {
        BigDecimal resultado = dashboardService.calcularTasaConversion(0, 0);
        assertThat(resultado).isEqualByComparingTo(BigDecimal.ZERO);
    }

    // Cobertura extra: calcularTicketPromedio con 0 transacciones
    @Test
    void calcularTicketPromedio_conCeroTransacciones_retornaCero() {
        BigDecimal resultado = dashboardService.calcularTicketPromedio(1000.0, 0);
        assertThat(resultado).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
