package com.ecomarket.admin.repository;

import com.ecomarket.admin.entity.EstadoTicket;
import com.ecomarket.admin.entity.TicketSoporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketSoporteRepository extends JpaRepository<TicketSoporte, Long> {

    List<TicketSoporte> findByEstado(EstadoTicket estado);

    List<TicketSoporte> findByCorreoContactoIgnoreCase(String correoContacto);
}
