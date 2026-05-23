package com.ecomarket.admin.repository;

import com.ecomarket.admin.entity.AlertaSistema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertaSistemaRepository extends JpaRepository<AlertaSistema, Long> {

    List<AlertaSistema> findByResueltaFalse();

    List<AlertaSistema> findByMicroservicioIgnoreCase(String microservicio);
}
