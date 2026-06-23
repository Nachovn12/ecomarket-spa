package com.ecomarket.admin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MsAdministracionSoporteApplicationTests {

	@Test
	void contextLoads() {
	}

	// SE AGREGA PARA EL 100%
	@Test
	void mainEjecutaAplicacion() {
		MsAdministracionSoporteApplication.main(new String[] {"--spring.profiles.active=test"});
	}

}

