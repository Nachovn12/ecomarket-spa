package com.ecomarket.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Test de carga del contexto Spring del API Gateway.
 * Valida que toda la configuracion (yml, rutas, beans) se carga sin error.
 */
@SpringBootTest
class ApiGatewayApplicationTests {

    @Test
    void contextLoads() {
    }

    // SE AGREGA PARA EL 100%
    @Test
    void mainEjecutaAplicacion() {
        ApiGatewayApplication.main(new String[] {"--spring.profiles.active=test"});
    }
}