package com.ecomarket.reportes.repository;

import com.ecomarket.reportes.entity.Reporte;
import com.ecomarket.reportes.entity.TipoReporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    List<Reporte> findByTipo(TipoReporte tipo);
    List<Reporte> findByIdTienda(Long idTienda);
    List<Reporte> findByTipoAndIdTienda(TipoReporte tipo, Long idTienda);
}
