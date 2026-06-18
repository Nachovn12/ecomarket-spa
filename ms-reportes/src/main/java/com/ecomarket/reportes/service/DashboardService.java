package com.ecomarket.reportes.service;

import com.ecomarket.reportes.dto.DashboardMetricasDTO;
import com.ecomarket.reportes.model.IndicadorKPI;
import com.ecomarket.reportes.model.TipoKPI;
import com.ecomarket.reportes.repository.IndicadorKPIRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private static final Logger log = LoggerFactory.getLogger(DashboardService.class);

    private final IndicadorKPIRepository indicadorKPIRepository;

    public DashboardService(IndicadorKPIRepository indicadorKPIRepository) {
        this.indicadorKPIRepository = indicadorKPIRepository;
    }

    public Double getVentasDelDia() {
        log.info("Calculando ventas del dia");
        return indicadorKPIRepository.findByTipo(TipoKPI.VENTAS_TOTALES)
                .stream()
                .mapToDouble(IndicadorKPI::getValor)
                .sum();
    }

    public List<IndicadorKPI> getProductosMasVendidos() {
        log.info("Obteniendo productos mas vendidos");
        return indicadorKPIRepository.findByTipo(TipoKPI.VENTAS_TOTALES)
                .stream()
                .sorted((a, b) -> Double.compare(b.getValor(), a.getValor()))
                .toList();
    }

    public BigDecimal calcularTasaConversion(int pedidos, int ventasConcretadas) {
        if (pedidos == 0) return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        return new BigDecimal(ventasConcretadas)
                .divide(new BigDecimal(pedidos), 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularTicketPromedio(double totalVentas, int transacciones) {
        if (transacciones == 0) return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        return new BigDecimal(totalVentas)
                .divide(new BigDecimal(transacciones), 2, RoundingMode.HALF_UP);
    }

    public DashboardMetricasDTO getDashboardMetrics() {
        log.info("Obteniendo metricas del dashboard");
        DashboardMetricasDTO dto = new DashboardMetricasDTO();
        dto.setVentasDelDia(getVentasDelDia());
        dto.setProductosMasVendidos(getProductosMasVendidos());
        dto.setTasaConversion(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        dto.setTicketPromedio(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        return dto;
    }
}
