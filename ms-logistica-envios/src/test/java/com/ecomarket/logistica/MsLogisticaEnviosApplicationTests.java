package com.ecomarket.logistica;

import com.ecomarket.logistica.dto.EnvioDTO;
import com.ecomarket.logistica.model.Envio;
import com.ecomarket.logistica.model.Proveedor;
import com.ecomarket.logistica.model.Ruta;
import com.ecomarket.logistica.model.enums.EstadoEnvio;
import com.ecomarket.logistica.service.LogisticaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:logistica_test;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.sql.init.mode=never"
})
class MsLogisticaEnviosApplicationTests {

    @Autowired
    private LogisticaService logisticaService;

    @Test
    void contextLoads() {
        // Valida que el contexto cargue correctamente (Requisito de la guia)
        assertNotNull(logisticaService);
    }

    @Test
    void crearProveedorTest() {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre("Transportes Rapidos SPA");
        Proveedor guardado = logisticaService.guardarProveedor(proveedor);
        assertNotNull(guardado.getIdProveedor());
    }

    @Test
    void crearRutaTest() {
        Ruta ruta = new Ruta();
        ruta.setOrigen("Santiago");
        ruta.setDestino("Concepcion");
        Ruta guardada = logisticaService.guardarRuta(ruta);
        assertNotNull(guardada.getIdRuta());
    }

    @Test
    void crearEnvioTest() {
        EnvioDTO dto = new EnvioDTO();
        dto.setIdPedido(100L);
        dto.setEstado(EstadoEnvio.PREPARACION);
        Envio envio = logisticaService.crearEnvio(dto);
        assertNotNull(envio.getIdEnvio());
        assertEquals(100L, envio.getIdPedido());
    }
}
