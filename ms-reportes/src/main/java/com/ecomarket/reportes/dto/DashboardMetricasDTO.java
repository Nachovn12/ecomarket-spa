package com.ecomarket.reportes.dto;

import com.ecomarket.reportes.model.IndicadorKPI;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DashboardMetricasDTO {
    private Double ventasDelDia;
    private List<IndicadorKPI> productosMasVendidos;
    private BigDecimal tasaConversion;
    private BigDecimal ticketPromedio;
}
