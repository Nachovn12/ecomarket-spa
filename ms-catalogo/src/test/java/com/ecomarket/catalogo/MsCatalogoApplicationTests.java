package com.ecomarket.catalogo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test de integración mínimo del contexto de Spring Boot.
 * Verifica que el ApplicationContext de ms-catalogo cargue correctamente
 * con la BD H2 en memoria (perfil "test") sin levantar MySQL.
 */
@SpringBootTest
@ActiveProfiles("test")
class MsCatalogoApplicationTests {

    @Test
    void contextLoads() {
        // Verifica que el contexto de Spring Boot (JPA, Repos, Services, Controllers)
        // levanta sin errores en el perfil de pruebas con H2 en memoria.
    }

    @Test
    void mainEjecutaAplicacion() {
        // Verifica que el punto de entrada de la aplicación ejecuta correctamente.
        System.setProperty("spring.profiles.active", "test");
        MsCatalogoApplication.main(new String[]{});
    }
}
