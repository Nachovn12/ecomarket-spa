package com.ecomarket.admin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"spring.datasource.url=jdbc:h2:mem:admin_test;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.hibernate.ddl-auto=create-drop",
		"spring.jpa.show-sql=false",
		"spring.jpa.properties.hibernate.format_sql=false",
		"spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
		"spring.sql.init.mode=never",
		"ms.usuarios.url=http://localhost:8083"
})
class MsAdministracionSoporteApplicationTests {

	@Test
	void contextLoads() {
		// Verifica que el contexto de spring carga correctamente.
		// UsuarioInternoClientService usa RestTemplate pero no realiza
		// Llamadas HTTP durante el arranque, por lo que el test es seguro.
	}

    // SE AGREGA PARA EL 100%
    @Test
    void mainEjecutaAplicacion() {
        // El main() llama a SpringApplication.run(); el contexto H2 ya esta activo
        // gracias a @TestPropertySource en la clase. Solo verificamos que no lanza excepcion.
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(
            () -> MsAdministracionSoporteApplication.main(new String[]{
                "--spring.datasource.url=jdbc:h2:mem:admin_main_test;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE",
                "--spring.datasource.driver-class-name=org.h2.Driver",
                "--spring.datasource.username=sa",
                "--spring.datasource.password=",
                "--spring.jpa.hibernate.ddl-auto=create-drop",
                "--spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
                "--spring.sql.init.mode=never",
                "--ms.usuarios.url=http://localhost:8083"
            })
        );
    }
}
