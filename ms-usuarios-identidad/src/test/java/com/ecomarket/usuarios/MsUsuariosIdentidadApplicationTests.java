package com.ecomarket.usuarios;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MsUsuariosIdentidadApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void mainEjecutaAplicacion() {
        System.setProperty("spring.profiles.active", "test");
        MsUsuariosIdentidadApplication.main(new String[]{});
    }
}
