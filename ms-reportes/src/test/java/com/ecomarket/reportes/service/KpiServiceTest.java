package com.ecomarket.reportes.service;

import com.ecomarket.reportes.dto.IndicadorKPIResponseDTO;
import com.ecomarket.reportes.exception.ReporteNotFoundException;
import com.ecomarket.reportes.model.IndicadorKPI;
import com.ecomarket.reportes.model.TipoKPI;
import com.ecomarket.reportes.repository.IndicadorKPIRepository;
import com.ecomarket.reportes.repository.ReporteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KpiServiceTest {

    @Mock
    private ReporteRepository reporteRepository;

    @Mock
    private IndicadorKPIRepository indicadorKPIRepository;

    @InjectMocks
    private ReporteService reporteService;

    private IndicadorKPI buildKpi(Long id, TipoKPI tipo, double valor) {
        IndicadorKPI kpi = new IndicadorKPI();
        kpi.setId(id);
        kpi.setTipo(tipo);
        kpi.setValor(valor);
        kpi.setDescripcion("KPI " + tipo.name());
        return kpi;
    }

    // AC-4: listarKPIs retorna todos los indicadores del repositorio
    @Test
    void listarKPIs_retornaListaDelRepositorio() {
        IndicadorKPI k1 = buildKpi(1L, TipoKPI.VENTAS_TOTALES, 15500000.0);
        k1.setDescripcion("Ventas mensuales consolidadas RM");
        IndicadorKPI k2 = buildKpi(2L, TipoKPI.STOCK_BAJO, 14.0);
        k2.setDescripcion("Productos críticos bajo stock de seguridad");
        when(indicadorKPIRepository.findAll()).thenReturn(List.of(k1, k2));

        List<IndicadorKPI> result = reporteService.listarKPIs();

        assertThat(result).hasSize(2);
        verify(indicadorKPIRepository).findAll();
    }

    // AC-4: obtenerKPIPorId con id válido retorna el KPI correcto
    @Test
    void obtenerKPIPorId_conIdValido_retornaKPI() {
        IndicadorKPI kpi = buildKpi(1L, TipoKPI.RENDIMIENTO_TIENDA, 0.92);
        kpi.setDescripcion("Eficiencia general Sucursal La Florida");
        when(indicadorKPIRepository.findById(1L)).thenReturn(Optional.of(kpi));

        IndicadorKPI result = reporteService.obtenerKPIPorId(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTipo()).isEqualTo(TipoKPI.RENDIMIENTO_TIENDA);
        assertThat(result.getValor()).isEqualTo(0.92);
    }

    // AC-4: obtenerKPIPorId con id inexistente lanza ReporteNotFoundException
    @Test
    void obtenerKPIPorId_conIdInexistente_lanzaReporteNotFoundException() {
        when(indicadorKPIRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ReporteNotFoundException.class,
                () -> reporteService.obtenerKPIPorId(99L));
    }

    // AC-5: crearKPI persiste y retorna el KPI guardado con id asignado
    @Test
    void crearKPI_guardaYRetornaKPI() {
        IndicadorKPI nuevo = buildKpi(null, TipoKPI.PEDIDOS_ENTREGADOS, 47.0);
        IndicadorKPI guardado = buildKpi(10L, TipoKPI.PEDIDOS_ENTREGADOS, 47.0);
        when(indicadorKPIRepository.save(any(IndicadorKPI.class))).thenReturn(guardado);

        IndicadorKPI result = reporteService.crearKPI(nuevo);

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getTipo()).isEqualTo(TipoKPI.PEDIDOS_ENTREGADOS);
        verify(indicadorKPIRepository).save(nuevo);
    }

    // AC-5: eliminarKPI con id existente llama a deleteById
    @Test
    void eliminarKPI_conIdExistente_llamaDeleteById() {
        when(indicadorKPIRepository.existsById(1L)).thenReturn(true);

        reporteService.eliminarKPI(1L);

        verify(indicadorKPIRepository).deleteById(1L);
    }

    // AC-5: eliminarKPI con id inexistente lanza ReporteNotFoundException sin llamar deleteById
    @Test
    void eliminarKPI_conIdInexistente_lanzaReporteNotFoundException() {
        when(indicadorKPIRepository.existsById(99L)).thenReturn(false);

        assertThrows(ReporteNotFoundException.class,
                () -> reporteService.eliminarKPI(99L));
        verify(indicadorKPIRepository, never()).deleteById(any());
    }

    // AC-4: listarKPIsPorTipo retorna solo los KPIs del tipo indicado
    @Test
    void listarKPIsPorTipo_retornaListaFiltrada() {
        IndicadorKPI k1 = buildKpi(1L, TipoKPI.VENTAS_TOTALES, 8500000.0);
        k1.setDescripcion("Ventas totales semanales zona centro");
        IndicadorKPI k2 = buildKpi(2L, TipoKPI.VENTAS_TOTALES, 6200000.0);
        k2.setDescripcion("Ventas totales semanales zona sur");
        when(indicadorKPIRepository.findByTipo(TipoKPI.VENTAS_TOTALES)).thenReturn(List.of(k1, k2));

        List<IndicadorKPI> result = reporteService.listarKPIsPorTipo(TipoKPI.VENTAS_TOTALES);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(k -> k.getTipo() == TipoKPI.VENTAS_TOTALES);
    }

    // AC-4: toDTO mapea todos los campos del KPI al DTO de respuesta
    @Test
    void toDTO_mapeaTodosLosCamposCorrectamente() {
        LocalDateTime ahora = LocalDateTime.of(2026, 6, 1, 10, 0);
        IndicadorKPI kpi = buildKpi(7L, TipoKPI.ROTACION_INVENTARIO, 85.3);
        kpi.setDescripcion("Tasa de rotación mensual abarrotes");
        kpi.setFechaCalculo(ahora);

        IndicadorKPIResponseDTO dto = reporteService.toDTO(kpi);

        assertThat(dto.getId()).isEqualTo(7L);
        assertThat(dto.getTipo()).isEqualTo("ROTACION_INVENTARIO");
        assertThat(dto.getValor()).isEqualTo(85.3);
        assertThat(dto.getDescripcion()).isEqualTo("Tasa de rotación mensual abarrotes");
        assertThat(dto.getFechaCalculo()).isEqualTo(ahora);
    }
}
