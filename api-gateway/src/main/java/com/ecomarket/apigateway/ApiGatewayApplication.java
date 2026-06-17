package com.ecomarket.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API Gateway del marketplace EcoMarket SPA.
 *
 * Enruta las 17 rutas declaradas en application.yml hacia los 7 microservicios
 * del backend (ms-usuarios-identidad, ms-catalogo, ms-inventario-abastecimiento,
 * ms-pedidos-ventas, ms-logistica-envios, ms-administracion-soporte, ms-reportes).
 *
 * Stack: Spring Boot 4.0.7 + Spring Cloud Gateway 2025.1.2 (WebFlux).
 * Puerto: 8081.
 */
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}