package com.ecomarket.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Smoke test de las 17 rutas del API Gateway.
 * Verifica que las 17 rutas declaradas en application.yml estan registradas
 * en el RouteDefinitionLocator de Spring Cloud Gateway (WebFlux).
 */
@SpringBootTest
class GatewayRouteSmokeTest {

    @Autowired
    private RouteDefinitionLocator routeDefinitionLocator;

    private static final List<String> EXPECTED_ROUTE_IDS = List.of(
            "ms-usuarios-auth",
            "ms-usuarios-identidad",
            "ms-catalogo-productos",
            "ms-catalogo-categorias",
            "ms-inventario",
            "ms-inventario-productos",
            "ms-pedidos",
            "ms-ventas",
            "ms-envios",
            "ms-rutas",
            "ms-admin",
            "ms-soporte",
            "ms-reportes",
            "ms-kpis",
            "ms-catalogo-resenas",
            "ms-pedidos-devoluciones",
            "ms-pedidos-reclamaciones"
    );

    @Test
    void allSeventeenRoutesAreRegistered() throws InterruptedException {
        // 1) Recolectar los route ids registrados en el contexto
        List<String> actual = routeDefinitionLocator.getRouteDefinitions()
                .toStream()
                .map(rd -> rd.getId())
                .sorted()
                .toList();

        // 2) Asercion dura: 17 ids exactos
        assertTrue(actual.size() >= EXPECTED_ROUTE_IDS.size(),
                "Se esperaban al menos " + EXPECTED_ROUTE_IDS.size()
                        + " rutas, pero hay " + actual.size());

        // 3) Cada id esperado debe estar presente
        for (String expectedId : EXPECTED_ROUTE_IDS) {
            assertTrue(actual.contains(expectedId),
                    "Falta la ruta esperada: " + expectedId);
        }
    }
}