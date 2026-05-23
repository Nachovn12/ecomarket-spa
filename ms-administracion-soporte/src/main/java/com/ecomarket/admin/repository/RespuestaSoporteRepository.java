package com.ecomarket.admin.repository;

import com.ecomarket.admin.entity.RespuestaSoporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RespuestaSoporteRepository extends JpaRepository<RespuestaSoporte, Long> {

    List<RespuestaSoporte> findByIdTicket(Long idTicket);
}
