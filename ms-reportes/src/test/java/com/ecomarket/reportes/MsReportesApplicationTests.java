package com.ecomarket.reportes;

import com.ecomarket.reportes.entity.IndicadorKPI;
import com.ecomarket.reportes.entity.Reporte;
import com.ecomarket.reportes.entity.TipoKPI;
import com.ecomarket.reportes.entity.TipoReporte;
import com.ecomarket.reportes.exception.ReporteException;
import com.ecomarket.reportes.repository.IndicadorKPIRepository;
import com.ecomarket.reportes.repository.ReporteRepository;
import com.ecomarket.reportes.service.ReporteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class MsReportesApplicationTests {

    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private IndicadorKPIRepository indicadorKPIRepository;

    @Autowired
    private ReporteService reporteService;

    @Test
    void contextLoads() {
    }

    @Test
    void testCrearIndicadorKPI() {
        IndicadorKPI kpi = new IndicadorKPI();
        kpi.setTipo(TipoKPI.VENTAS_TOTALES);
        kpi.setValor(15000.0);
        kpi.setDescripcion("Ventas totales del mes");
        indicadorKPIRepository.save(kpi);

        var encontrado = indicadorKPIRepository.findByTipo(TipoKPI.VENTAS_TOTALES);
        assertFalse(encontrado.isEmpty());
        assertEquals(15000.0, encontrado.get(0).getValor());
    }

    @Test
    void testCrearReporteVentas() {
        Reporte reporte = new Reporte();
        reporte.setTipo(TipoReporte.VENTAS);
        reporte.setIdTienda(1L);

        Reporte guardado = reporteRepository.save(reporte);

        assertNotNull(guardado.getId());
        assertEquals(TipoReporte.VENTAS, guardado.getTipo());
    }

    @Test
    void testListarReportes() {
        long count = reporteRepository.count();
        assertTrue(count >= 0);
    }

    @Test
    void testKpiStockBajo() {
        IndicadorKPI kpi = new IndicadorKPI();
        kpi.setTipo(TipoKPI.STOCK_BAJO);
        kpi.setValor(5.0);
        kpi.setDescripcion("Productos con stock bajo");
        indicadorKPIRepository.save(kpi);

        var resultado = indicadorKPIRepository.findByTipo(TipoKPI.STOCK_BAJO);
        assertFalse(resultado.isEmpty());
    }

    @Test
    void testListarKpisPorTipo() {
        IndicadorKPI kpi = new IndicadorKPI();
        kpi.setTipo(TipoKPI.RENDIMIENTO_TIENDA);
        kpi.setValor(85.0);
        kpi.setDescripcion("Rendimiento tienda");
        indicadorKPIRepository.save(kpi);

        var resultado = indicadorKPIRepository.findByTipo(TipoKPI.RENDIMIENTO_TIENDA);
        assertFalse(resultado.isEmpty());
        assertEquals(TipoKPI.RENDIMIENTO_TIENDA, resultado.get(0).getTipo());
    }

    @Test
    void testObtenerReporteInexistenteLanzaExcepcion() {
        assertThrows(ReporteException.class, () -> {
            reporteService.obtenerReportePorId(999999L);
        });
    }
}