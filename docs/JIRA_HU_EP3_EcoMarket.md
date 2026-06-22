# JIRA_HU_EP3_EcoMarket.md — Datos completos de Jira (formato profesional real)

**Proyecto:** HU (EcoMarket SPA) — board 67
**Sprint:** S5 - Pruebas Unitarias EP3 (15-21 jun 2026)
**Epic link:** HU-60 (EP-08 Pruebas Unitarias)
**Fecha:** 2026-06-16
**Empresa:** Cordillera (Atlassian Cloud)

> Este archivo contiene TODOS los datos que necesita una issue en Jira
> para estar completa y bien configurada segun buenas practicas de proyectos
> Scrum reales: Summary, Description, Story Points, Original Estimate (h),
> Sub-tasks con su propio SP y tiempo, y DoD.
>
> **Como usarlo:** Abre cada bloque, abre la issue correspondiente en Jira,
> y actualiza los campos segun la tabla de mapeo de abajo.

> **DECISION GLOBAL EP3 - HATEOAS REMOVIDO:** Por instruccion explicita del
> profesor, los 7 microservicios NO usan HATEOAS. Los controllers devuelven
> DTOs directos (ResponseEntity<DTO> o ResponseEntity<List<DTO>>) sin
> envoltorios EntityModel/CollectionModel y sin _links/_embedded. Todos los
> ACs y sub-tasks que mencionaban HATEOAS en versiones previas de este doc
> fueron actualizados a "Validacion JSON puro / DTO directo". NO reintroducir
> la dependencia spring-boot-starter-hateoas ni importar
> org.springframework.hateoas.* en ningun MS.
## Tabla de mapeo rapido (para no equivocarse)

| Issue | Asignado actual | SP a poner | Original Estimate (h) | Sprint | Labels actuales |
|---|---|---|---|---|---|
| HU-61 | Ignacio Valeria | 3 | 4h | S5 | ep3, gateway, ms-api-gateway, webflux, yaml |
| HU-62 | Ignacio Valeria | 3 | 4h | S5 | ep3, gateway, ms-api-gateway, webflux |
| HU-63 | Benjamin Espinoza | 5 | 8h | S5 | ep3, tests, ms-catalogo |
| HU-64 | Benjamin Espinoza | 5 | 8h | S5 | ep3, tests, ms-logistica-envios |
| HU-65 | Benjamin Flores | 8 | 12h | S5 | ep3, tests, ms-usuarios-identidad |
| HU-66 | Benjamin Flores | 5 | 8h | S5 | ep3, tests, ms-administracion-soporte |
| HU-67 | Benjamin Flores | 2 | 3h | S5 | ep3, tests, postman |
| HU-68 | Benjamin Palma | 8 | 12h | S5 | ep3, tests, ms-inventario-abastecimiento |
| HU-69 | Benjamin Palma | 5 | 8h | S5 | ep3, tests, ms-reportes |
| HU-70 | Todos | 3 | 4h | S5 | ep3, defensa, transversal |
| HU-71 | Ignacio Valeria | 13 | 20h | S5 | ep3, tests, ms-pedidos-ventas |
| **Total** | | **60** | **91h** | | |

## Convenciones de tiempo (escala no lineal por complejidad)

- 1 SP = 4h
- 2 SP = 4h
- 3 SP = 4h
- 5 SP = 8h (1 dia)
- 8 SP = 12h (1.5 dias)
- 13 SP = 20h (2.5 dias)

## Convencion de sub-tasks (1 sub-task = 1 archivo de test)

Las sub-tasks se crean en Jira con el boton "+" bajo "Subtareas" en el panel
lateral. **No** van dentro de la descripcion de la issue padre (eso seria
duplicar informacion). Cada sub-task tiene su propio SP y Original estimate.

---

===========================================
ISSUE: HU-61 — Migrar API Gateway a application.yml (WebFlux)
===========================================

**CAMPOS A ACTUALIZAR EN JIRA:**

| Campo | Valor |
|---|---|
| Summary (ya esta) | [EP3] Migrar API Gateway a application.yml (WebFlux) |
| Issue Type (ya esta) | Story |
| Priority (ya esta) | High |
| Assignee (ya esta) | Ignacio Valeria |
| Sprint (ya esta) | S5 - Pruebas Unitarias EP3 |
| Epic Link (ya esta) | HU-60 EP-08 Pruebas Unitarias |
| **Story point estimate** | **3** |
| **Original estimate** | **4h** |
| Labels (ya estan) | ep3, gateway, ms-api-gateway, webflux, yaml |
| Fix versions | (vacio) |

**SUB-TASKS A CREAR (boton + en panel Subtareas):**

Sub-task 1: Migrar pom.xml a WebFlux
  Summary: chore: migrar api-gateway pom.xml a spring-cloud-starter-gateway-server-webflux
  Story point estimate: 1
  Original estimate: 1h
  Description: Cambiar dependency webmvc -> webflux, actualizar Spring Boot 4.0.6 -> 4.0.7, Spring Cloud 2025.1.1 -> 2025.1.2

Sub-task 2: Crear application.yml con 17 rutas WebFlux
  Summary: feat: reescribir application.yml con 17 rutas en formato webflux.routes
  Story point estimate: 1
  Original estimate: 1h
  Description: Crear api-gateway/src/main/resources/application.yml con spring.cloud.gateway.server.webflux.routes, 17 rutas hacia los 7 MS, server.port 8081, management.endpoints y logging.level

Sub-task 3: Crear ApiGatewayApplication.java
  Summary: feat: crear clase principal ApiGatewayApplication con @SpringBootApplication
  Story point estimate: 1
  Original estimate: 2h
  Description: Crear api-gateway/src/main/java/com/ecomarket/apigateway/ApiGatewayApplication.java con @SpringBootApplication y Javadoc


Sub-task 4: Validacion runtime de las 17 rutas (GATING para cerrar HU-61)
  Summary: chore(api-gateway): smoke test runtime de 17 rutas con mock servers locales
  Story point estimate: 1
  Original estimate: 1h
  Description: PREREQUISITO para cerrar HU-61 y HU-62 como Done. Codigo compilando no garantiza runtime. Procedimiento: (1) Levantar mocks de los 7 MS en sus puertos (8083 usuarios, 8084 catalogo, 8085 inventario, 8086 pedidos/ventas, 8087 logistica, 8088 admin, 8089 reportes) usando python -m http.server con respuestas JSON fijas, o mvn -f <ms>/pom.xml spring-boot:run si estan operativos; (2) Arrancar gateway con java -jar api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar en background; (3) Ejecutar curl -sS -o /dev/null -w "%{http_code}\n" http://localhost:8081/<ruta> para las 17 rutas declaradas en application.yml; (4) Confirmar que las 17 devuelven 2xx o 4xx esperado (no 5xx ni connection refused). Guardar output en docs/evidencias-tecnicas/capturas/api-gateway/runtime-17-rutas.log. Si alguna ruta falla, abrir bug en HU-61 antes de marcarla Done. Esta sub-tarea DEBE estar en estado Done antes de cerrar la issue padre.
**DESCRIPCION (campo Descripcion de la issue HU-61):**

**Historia de Usuario:**
Como desarrollador del API Gateway
quiero migrar la configuracion del api-gateway de application.properties a application.yml usando spring-cloud-starter-gateway-server-webflux
para alinear el gateway al stack del repositorio del profesor (getawayspring) y cumplir con la rubrica EP3 (IE 3.3.4 - YAML 3%)

**Criterios de Aceptacion (formato Gherkin):**

AC-1: Routes declarados en formato WebFlux
- Dado el archivo api-gateway/src/main/resources/application.yml
- Cuando se inspecciona el bloque spring.cloud.gateway.server.webflux.routes
- Entonces existen las 17 rutas hacia los 7 MS

AC-2: Formato lista de rutas con id, uri, predicates
- Dado cada ruta del application.yml
- Cuando se revisa su estructura YAML
- Entonces tiene los campos id, uri y predicates (Path) bien formateados

AC-3: Puerto 8081
- Dado el archivo application.yml
- Cuando se inspecciona la clave server.port
- Entonces el valor es 8081

AC-4: Arranque correcto con mvn spring-boot:run
- Dado el modulo api-gateway con todas las rutas configuradas
- Cuando se ejecuta mvn -f api-gateway/pom.xml spring-boot:run
- Entonces el gateway arranca en 3-5 segundos y la ultima linea del log dice "Started ApiGatewayApplication" + "Netty started on port 8081 (http)"

AC-6: Verificacion runtime de las 17 rutas (gating, ver sub-task 4)
- Dado el gateway levantado en localhost:8081 con mocks de los 7 MS en sus puertos
- Cuando se ejecuta curl contra cada una de las 17 rutas declaradas en application.yml
- Entonces las 17 responden 2xx o 4xx esperado (cero 5xx, cero connection refused). Evidencia en docs/evidencias-tecnicas/capturas/api-gateway/runtime-17-rutas.log

AC-5: Dependencia WebFlux (no WebMVC) en pom.xml
- Dado el pom.xml del api-gateway
- Cuando se inspecciona el bloque <dependencies>
- Entonces declara spring-cloud-starter-gateway-server-webflux y NO contiene spring-cloud-starter-gateway-server-webmvc

**Archivos:**
- api-gateway/pom.xml (modificado)
- api-gateway/src/main/resources/application.yml (nuevo)
- api-gateway/src/main/java/com/ecomarket/apigateway/ApiGatewayApplication.java (nuevo)
- api-gateway/src/test/resources/application-test.yml (nuevo)

**DoD (Definition of Done):**
- mvn -f api-gateway/pom.xml test ejecuta BUILD SUCCESS
- mvn -f api-gateway/pom.xml package genera el JAR
- Log de arranque con "Netty started on port 8081" en docs/evidencias-tecnicas/capturas/api-gateway/
- Sub-task 4 completa: las 17 rutas verificadas en runtime (AC-6), log en docs/evidencias-tecnicas/capturas/api-gateway/runtime-17-rutas.log
- PR revisado por 1 companero (Benjamin Espinoza o Palma)
- Commit: `chore(api-gateway): migrar de properties a yml con WebFlux`

===========================================


===========================================
ISSUE: HU-62 — Pruebas del API Gateway (context-load + smoke de 17 rutas)
===========================================

**CAMPOS A ACTUALIZAR EN JIRA:**

| Campo | Valor |
|---|---|
| Summary (ya esta) | [EP3] Pruebas del API Gateway (context-load + smoke de 17 rutas) |
| Issue Type (ya esta) | Story |
| Priority (ya esta) | High |
| Assignee (ya esta) | Ignacio Valeria |
| Sprint (ya esta) | S5 - Pruebas Unitarias EP3 |
| Epic Link (ya esta) | HU-60 EP-08 Pruebas Unitarias |
| **Story point estimate** | **3** |
| **Original estimate** | **4h** |
| Labels (ya estan) | ep3, gateway, ms-api-gateway, webflux |
| Fix versions | (vacio) |

**SUB-TASKS A CREAR:**

Sub-task 1: Crear ApiGatewayApplicationTests
  Summary: test: ApiGatewayApplicationTests con @SpringBootTest contextLoads
  Story point estimate: 1
  Original estimate: 1h
  Description: Crear test con @SpringBootTest y metodo contextLoads() que valida que el ApplicationContext del gateway carga sin error

Sub-task 2: Crear GatewayRouteSmokeTest
  Summary: test: GatewayRouteSmokeTest con validacion de las 17 rutas
  Story point estimate: 1
  Original estimate: 2h
  Description: Inyectar RouteDefinitionLocator y verificar que los 17 ids de ruta estan registrados: ms-usuarios-auth, ms-usuarios-identidad, ms-catalogo-productos, ms-catalogo-categorias, ms-inventario, ms-inventario-productos, ms-pedidos, ms-ventas, ms-envios, ms-rutas, ms-admin, ms-soporte, ms-reportes, ms-kpis, ms-catalogo-resenas, ms-pedidos-devoluciones, ms-pedidos-reclamaciones

Sub-task 3: Crear GatewayPortTest
  Summary: test: GatewayPortTest valida puerto 8081
  Story point estimate: 1
  Original estimate: 1h
  Description: Validar que application.yml declara server.port=8081 usando @SpringBootTest con @LocalServerPort y assertion sobre la property

**DESCRIPCION:**

**Historia de Usuario:**
Como desarrollador del API Gateway
quiero contar con pruebas automatizadas que validen el arranque del contexto y el enrutamiento de las 17 rutas
para asegurar que el gateway queda enrutando correctamente y defender IE 3.1.1 (Pruebas Unitarias 8%)

**Criterios de Aceptacion (formato Gherkin):**

AC-1: Context-load verde
- Dado el contexto Spring del gateway con todas las rutas configuradas
- Cuando se ejecuta mvn -f api-gateway/pom.xml test
- Entonces el test contextLoads() pasa en verde

AC-2: 17 rutas registradas
- Dado los 17 ids de ruta declarados en application.yml
- Cuando se inyecta RouteDefinitionLocator y se listan las rutas
- Entonces los 17 ids estan presentes

AC-3: Cobertura por MS critico
- Dado los MS criticos: usuarios, catalogo, inventario, pedidos, envios, admin, reportes
- Cuando se ejecuta el smoke test
- Entonces cada MS tiene al menos una ruta con predicate Path

AC-4: Configuracion puerto 8081
- Dado el gateway configurado con server.port=8081
- Cuando se levanta el contexto
- Entonces application.yml declara server.port=8081

AC-5: mvn test BUILD SUCCESS
- Dado el modulo api-gateway con los 3 tests
- Cuando se ejecuta mvn -f api-gateway/pom.xml test
- Entonces Tests run >= 3, Failures = 0, Errors = 0

**Archivos:**
- api-gateway/src/test/java/com/ecomarket/apigateway/ApiGatewayApplicationTests.java
- api-gateway/src/test/java/com/ecomarket/apigateway/GatewayRouteSmokeTest.java
- api-gateway/src/test/java/com/ecomarket/apigateway/GatewayPortTest.java
- api-gateway/src/test/resources/application-test.yml

**DoD:**
- mvn test BUILD SUCCESS con 3/3 tests verdes
- Log de mvn test en docs/evidencias-tecnicas/capturas/api-gateway/mvn-test.log
- PR revisado por 1 companero
- Commit: `test(gateway): 3 tests (context, smoke 17 rutas, puerto 8081)`

===========================================


===========================================
ISSUE: HU-63 — Pruebas MS Catalogo (CatalogoService + Producto/Categoria/Resena)
===========================================

**CAMPOS A ACTUALIZAR:**

| Campo | Valor |
|---|---|
| Summary (ya esta) | [EP3] Pruebas MS Catalogo (CatalogoService + Producto/Categoria/Resena) |
| Issue Type (ya esta) | Story |
| Priority (ya esta) | High |
| Assignee (ya esta) | Benjamin Espinoza |
| Sprint (ya esta) | S5 - Pruebas Unitarias EP3 |
| Epic Link (ya esta) | HU-60 EP-08 Pruebas Unitarias |
| **Story point estimate** | **5** |
| **Original estimate** | **8h** |
| Labels (recomendado) | ep3, tests, ms-catalogo, mockito |

**SUB-TASKS A CREAR:**

Sub-task 1: Crear CatalogoServiceTest
  Summary: test(ms-catalogo): CatalogoServiceTest con mocks de repos
  Story point estimate: 2
  Original estimate: 3h
  Description: Cubrir AC-1 (mocks de Producto/Categoria/Resena repos), AC-3 (CRUD completo), AC-4 (busqueda "bambu"), AC-5 (solo resenas de productos comprados), AC-6 (promedio calificaciones con HALF_UP a 1 decimal)

Sub-task 2: Crear ProductoControllerTest
  Summary: test(ms-catalogo): ProductoControllerTest con @WebMvcTest
  Story point estimate: 1
  Original estimate: 2h
  Description: Cubrir AC-2 (controller con @WebMvcTest + @MockitoBean), AC-3 (CRUD), AC-7 (DTO directo en GET/POST/PUT, sin _links)

Sub-task 3: Crear CategoriaControllerTest
  Summary: test(ms-catalogo): CategoriaControllerTest con @WebMvcTest
  Story point estimate: 1
  Original estimate: 1h
  Description: Cubrir AC-2 (controller), AC-3 (CRUD de categorias), AC-7 (List<DTO> directo en colecciones, sin _embedded)

Sub-task 4: Crear ResenaControllerTest
  Summary: test(ms-catalogo): ResenaControllerTest con regla de "solo productos comprados"
  Story point estimate: 1
  Original estimate: 2h
  Description: Cubrir AC-2, AC-5 (rechazar resena de cliente sin pedido del producto), AC-6 (promedio de calificaciones)

**DESCRIPCION:**

**Historia de Usuario:**
Como desarrollador del MS Catalogo
quiero contar con pruebas unitarias de CatalogoService y de los controllers ProductoController, CategoriaController, ResenaController
para garantizar las operaciones CRUD de productos, categorias y resenas, y validar las busquedas del catalogo

**Dominio EcoMarket SPA:**
El Cliente del marketplace puede "Navegar y Buscar Productos" con filtros (categoria, precio, eco-certificacion) y barra de busqueda. "Dejar Resenas y Calificaciones" sobre productos comprados. Solo clientes que compraron el producto pueden dejar resena.

**Criterios de Aceptacion (formato Gherkin):**

AC-1: CatalogoServiceTest con mocks
- Dado CatalogoService con ProductoRepository, CategoriaRepository y ResenaRepository mockeados
- Cuando se ejecuta el ServiceTest con @ExtendWith(MockitoExtension.class)
- Entonces todos los tests pasan

AC-2: Tests de 3 controllers con @WebMvcTest
- Cuando se ejecutan ProductoControllerTest, CategoriaControllerTest, ResenaControllerTest
- Entonces los 3 archivos usan @WebMvcTest + @MockitoBean

AC-3: Cobertura CRUD completa
- Cuando se ejecutan tests de listar, guardar, obtener por id (existente y no existente), actualizar y eliminar
- Entonces todos los casos estan cubiertos

AC-4: Busqueda por texto
- Dado un catalogo con productos eco bamboo
- Cuando se busca "bambu"
- Entonces se devuelven los productos bamboo

AC-5: Regla "solo resenas de productos comprados"
- Dado un cliente SIN pedido del producto X
- Cuando intenta dejar resena sobre X
- Entonces el sistema rechaza con excepcion de negocio

AC-6: Promedio de calificaciones
- Dado un producto con resenas (4, 5, 3 estrellas)
- Cuando se calcula el promedio
- Entonces el resultado es 4.0 con HALF_UP a 1 decimal

AC-7: Validacion JSON puro / DTO directo
- Cuando se invoca GET, POST, PUT
- Entonces el JSON expone DTOs directos (sin _links ni _embedded)

AC-8: Cobertura JaCoCo >= 80%
- Cuando se ejecuta mvn test con JaCoCo
- Entonces la cobertura es >= 80%

**DoD:**
- mvn -f ms-catalogo/pom.xml test ejecuta BUILD SUCCESS
- Reporte JaCoCo >= 80% en docs/evidencias-tecnicas/capturas/ms-catalogo/
- Rama: feature/ep3-tests-ms-catalogo
- PR revisado por Ignacio Valeria
- Commit: `test(ms-catalogo): CatalogoServiceTest + 3 ControllerTest con regla resenas`

===========================================


===========================================
ISSUE: HU-64 - Pruebas MS Logistica de Envios (LogisticaService + 3 controllers + EtaCalculator)
===========================================

**CAMPOS A ACTUALIZAR:**

| Campo | Valor |
|---|---|
| Summary (REEMPLAZAR) | [EP3] Pruebas MS Logistica de Envios (LogisticaService, Envio, Proveedor, RutaEntrega, EtaCalculator) |
| Issue Type (ya esta) | Story |
| Priority (ya esta) | High |
| Assignee (ya esta) | Benjamin Espinoza |
| Sprint (ya esta) | S5 - Pruebas Unitarias EP3 |
| Epic Link (ya esta) | HU-60 EP-08 Pruebas Unitarias |
| Story point estimate | 5 |
| Labels (recomendado) | ep3, tests, ms-logistica-envios, mockito, eta-calculator |

**ESTRUCTURA REAL DEL MS (verificada contra ms-logistica-envios/src/main/java/com/ecomarket/logistica/):**
- service/LogisticaService.java (UN SOLO service, no 4 separados)
- controller/EnvioController.java
- controller/ProveedorController.java
- controller/RutaEntregaController.java (3 controllers, no 4)
- util/EtaCalculator.java (logica de ruta optima con tiempo estimado)
- model/Envio.java, Proveedor.java, RutaEntrega.java, SeguimientoEnvio.java
- repository/: EnvioRepository, ProveedorRepository, RutaEntregaRepository, SeguimientoEnvioRepository
- service/PedidosClientService.java (cliente REST hacia ms-pedidos-ventas)

**SUB-TASKS A CREAR:**

Sub-task 1: Crear LogisticaServiceTest
  Summary: test(ms-logistica-envios): LogisticaServiceTest con Mockito (cubre Envio, Proveedor, RutaEntrega, Seguimiento)
  Story point estimate: 2
  Original estimate: 3h
  Description: Cubrir AC-1 (LogisticaService con @ExtendWith(MockitoExtension.class) + @Mock de los 4 repositorios + @Mock de PedidosClientService). Validar AC-3 (asignacion de envio a proveedor con menor tiempo estimado) y AC-4 (calculo de ruta optima con distancia total en km, EtaCalculator). Dado que LogisticaService es unico, este test cubre las 4 entidades (Envio, Proveedor, RutaEntrega, SeguimientoEnvio) en una sola clase con multiples @Nested.

Sub-task 2: Crear 3 ControllerTest con @WebMvcTest
  Summary: test(ms-logistica-envios): 3 ControllerTest con @WebMvcTest + @MockitoBean (DTO directo)
  Story point estimate: 2
  Original estimate: 3h
  Description: Cubrir AC-2 (EnvioControllerTest, ProveedorControllerTest, RutaEntregaControllerTest con @WebMvcTest + @MockitoBean de LogisticaService). Validar AC-5 (DTO directo en GET/POST/PUT, List<DTO> en colecciones, sin _links/_embedded). 3 archivos, no 4. Si los controllers delegan logica de seguimiento a LogisticaService, el test va por ahi.

Sub-task 3: Crear EtaCalculatorTest
  Summary: test(ms-logistica-envios): EtaCalculatorTest para ruta optima por menor tiempo
  Story point estimate: 1
  Original estimate: 2h
  Description: Cubrir AC-4. EtaCalculator es una clase utilitaria pura (sin dependencias de Spring), testeable con JUnit puro + parametros. Validar: (a) dada una lista de paradas, calcula el orden de menor tiempo total; (b) retorna distancia total en km; (c) maneja caso de 1 sola parada (retorna 0 km); (d) maneja lista vacia (lanza IllegalArgumentException).

**DESCRIPCION:**

**Historia de Usuario:**
Como desarrollador del MS Logistica de Envios
quiero contar con pruebas unitarias de LogisticaService (unico service del MS), los 3 controllers (Envio, Proveedor, RutaEntrega) y la clase utilitaria EtaCalculator
para validar la asignacion de envios a proveedores, el calculo de rutas optimas y el seguimiento de paquetes

**Dominio EcoMarket SPA:**
El MS Logistica recibe los pedidos confirmados del MS Pedidos-Ventas mediante PedidosClientService y los asigna a un transportista (proveedor) con una ruta optimizada calculada por EtaCalculator. El cliente consulta el estado del envio mediante el endpoint de seguimiento (que vive en LogisticaService, no en un service separado). Caso: el Cliente puede "Consultar estado de envio y tracking"; el Empleado de Logistica "Asigna envios, gestiona rutas y proveedores".

**Criterios de Aceptacion (formato Gherkin):**

AC-1: LogisticaServiceTest con Mockito
- Cuando se ejecuta LogisticaServiceTest
- Entonces el archivo existe con @ExtendWith(MockitoExtension.class), @Mock de EnvioRepository + ProveedorRepository + RutaEntregaRepository + SeguimientoEnvioRepository + PedidosClientService, @InjectMocks de LogisticaService
- Y cubre: alta de envio, asignacion a proveedor, calculo de ruta, registro de seguimiento, consulta de tracking, cambio de estado (DTO CambioEstadoRequestDTO)

AC-2: 3 ControllerTest con @WebMvcTest
- Cuando se ejecutan EnvioControllerTest, ProveedorControllerTest, RutaEntregaControllerTest
- Entonces los archivos usan @WebMvcTest + @MockitoBean de LogisticaService
- Y validan endpoints REST: GET listar, GET por id (200 y 404), POST, PUT, DELETE

AC-3: Asignacion de envio a proveedor con menor tiempo estimado
- Dado un envio con destino Santiago y 2 proveedores (Proveedor A con tiempo 24h, Proveedor B con tiempo 12h)
- Cuando LogisticaService.asignarProveedor() resuelve
- Entonces selecciona Proveedor B y persiste la asignacion con el tiempo estimado retornado por EtaCalculator

AC-4: Calculo de ruta optima con EtaCalculator
- Dado 4 paradas (origen + 3 destinos) con tiempos entre pares
- Cuando EtaCalculator.calcularRutaOptima() resuelve
- Entonces el orden de visita minimiza el tiempo total y retorna distanciaTotalKm
- Y con 1 sola parada retorna 0 km
- Y con lista vacia lanza IllegalArgumentException

AC-5: Validacion JSON puro / DTO directo
- Cuando se invocan GET/POST/PUT de Envio, Proveedor, RutaEntrega
- Entonces el JSON expone DTOs directos (sin _links ni _embedded)

AC-6: Cobertura JaCoCo >= 80%
- Cuando se ejecuta mvn -f ms-logistica-envios/pom.xml test con jacoco:report
- Entonces el reporte muestra >= 80% de line coverage en LogisticaService + controllers + EtaCalculator

**DoD:**
- mvn -f ms-logistica-envios/pom.xml test ejecuta BUILD SUCCESS (LogisticaServiceTest + 3 ControllerTest + EtaCalculatorTest)
- Reporte JaCoCo >= 80% en docs/evidencias-tecnicas/capturas/ms-logistica-envios/
- Rama: feature/ep3-tests-ms-logistica-envios
- PR revisado por Ignacio Valeria
- Commit: `test(ms-logistica-envios): LogisticaServiceTest + 3 ControllerTest + EtaCalculatorTest con Mockito`

===========================================
ISSUE: HU-65 — Pruebas MS Usuarios e Identidad (Auth, Usuario, RolPermiso, UsuarioInterno)
===========================================

**CAMPOS A ACTUALIZAR:**

| Campo | Valor |
|---|---|
| Summary (ya esta) | [EP3] Pruebas MS Usuarios e Identidad (Auth, Usuario, RolPermiso, UsuarioInterno) |
| Issue Type (ya esta) | Story |
| Priority (ya esta) | High |
| Assignee (ya esta) | Benjamin Flores |
| Sprint (ya esta) | S5 - Pruebas Unitarias EP3 |
| Epic Link (ya esta) | HU-60 EP-08 Pruebas Unitarias |
| **Story point estimate** | **8** |
| **Original estimate** | **12h** |
| Labels (recomendado) | ep3, tests, ms-usuarios-identidad, mockito, run, password |

**SUB-TASKS A CREAR:**

Sub-task 1: Crear AuthServiceTest
  Summary: test(ms-usuarios): AuthServiceTest con validacion RUN y password
  Story point estimate: 2
  Original estimate: 3h
  Description: Cubrir AC-1 (AuthService con @MockitoExtension). Validar AC-3 (RUN modulo 11 valido e invalido) y AC-4 (politica de password: min 8 chars, mayuscula, minuscula, numero)

Sub-task 2: Crear UsuarioServiceTest + RolPermisoServiceTest
  Summary: test(ms-usuarios): 2 ServiceTest con Mockito
  Story point estimate: 2
  Original estimate: 3h
  Description: Cubrir AC-1 (UsuarioService y RolPermisoService con @Mock y @InjectMocks). Validar registro, login, gestion de roles y permisos

Sub-task 3: Crear UsuarioInternoServiceTest
  Summary: test(ms-usuarios): UsuarioInternoServiceTest con Mockito
  Story point estimate: 1
  Original estimate: 1h
  Description: Cubrir AC-1 (UsuarioInternoService para empleados internos como Empleado de Ventas y Administrador)

Sub-task 4: Crear AuthControllerTest + UsuarioControllerTest
  Summary: test(ms-usuarios): 2 ControllerTest con @WebMvcTest
  Story point estimate: 1
  Original estimate: 2h
  Description: Cubrir AC-2 (AuthControllerTest y UsuarioControllerTest con @WebMvcTest + @MockitoBean), AC-5 (DTO directo, sin _links.self)

Sub-task 5: Crear RolPermisoControllerTest + UsuarioInternoControllerTest
  Summary: test(ms-usuarios): 2 ControllerTest adicionales
  Story point estimate: 1
  Original estimate: 1h
  Description: Cubrir AC-2 (RolPermisoControllerTest y UsuarioInternoControllerTest con @WebMvcTest), AC-5 (DTO directo, sin HATEOAS)

**DESCRIPCION:**

**Historia de Usuario:**
Como desarrollador del MS Usuarios e Identidad
quiero contar con pruebas unitarias de AuthService, UsuarioService, RolPermisoService, UsuarioInternoService y sus controllers
para asegurar el correcto funcionamiento del registro, login, gestion de roles y permisos

**Dominio EcoMarket SPA:**
El sistema maneja 3 tipos de usuarios: Cliente (RUT + datos personales), Empleado de Ventas (interno) y Administrador. El RUN chileno debe validarse con su digito verificador (modulo 11). La politica de password exige minimo 8 caracteres, 1 mayuscula, 1 minuscula y 1 numero.

**Criterios de Aceptacion (formato Gherkin):**

AC-1: Tests de 4 services con Mockito
- Cuando se ejecutan los 4 ServiceTest
- Entonces los archivos usan @ExtendWith(MockitoExtension.class), @Mock y @InjectMocks

AC-2: Tests de 4 controllers con @WebMvcTest + @MockitoBean

AC-3: RUN chileno con digito verificador (modulo 11)
- Dado un RUN valido (12345678-5) o invalido (12345678-K)
- Cuando AuthService valida el RUN
- Entonces acepta el valido y rechaza el invalido

AC-4: Politica de password robusta
- Dado un password que cumple la politica (min 8 chars, mayuscula, minuscula, numero)
- Cuando UsuarioService valida la password
- Entonces acepta el valido y rechaza los que no cumplen

AC-5: Validacion JSON puro / DTO directo (sin _links.self) en GET, POST, PUT

AC-6: Cobertura JaCoCo >= 80%

**DoD:**
- mvn -f ms-usuarios-identidad/pom.xml test ejecuta BUILD SUCCESS
- Reporte JaCoCo >= 80% en docs/evidencias-tecnicas/capturas/ms-usuarios-identidad/
- Rama: feature/ep3-tests-ms-usuarios-identidad
- PR revisado por Ignacio Valeria
- Commit: `test(ms-usuarios): 4 ServiceTest + 4 ControllerTest con validacion RUN/password`

===========================================


===========================================
ISSUE: HU-66 — Pruebas MS Administracion y Soporte (AdministracionSoporteService + Controller)
===========================================

**CAMPOS A ACTUALIZAR:**

| Campo | Valor |
|---|---|
| Summary (ya esta) | [EP3] Pruebas MS Administracion y Soporte (AdministracionSoporteService + Controller) |
| Issue Type (ya esta) | Story |
| Priority (ya esta) | High |
| Assignee (ya esta) | Benjamin Flores |
| Sprint (ya esta) | S5 - Pruebas Unitarias EP3 |
| Epic Link (ya esta) | HU-60 EP-08 Pruebas Unitarias |
| **Story point estimate** | **5** |
| **Original estimate** | **8h** |
| Labels (recomendado) | ep3, tests, ms-administracion-soporte, mockito |

**SUB-TASKS A CREAR:**

Sub-task 1: Crear TiendaServiceTest + MetricaServiceTest
  Summary: test(ms-administracion): 2 ServiceTest con Mockito
  Story point estimate: 1
  Original estimate: 2h
  Description: Cubrir AC-1 (TiendaService y MetricaService con @MockitoExtension). Validar alta de tiendas y calculo de metricas (ventas/hora, conversion)

Sub-task 2: Crear AlertaServiceTest + RespaldoServiceTest
  Summary: test(ms-administracion): 2 ServiceTest con reglas de negocio
  Story point estimate: 1
  Original estimate: 2h
  Description: Cubrir AC-1 (AlertaService y RespaldoService con @Mock). Validar AC-3 (alerta automatica por stock bajo con severidad ALTA) y AC-4 (restauracion de respaldo registra la operacion)

Sub-task 3: Crear TicketServiceTest
  Summary: test(ms-administracion): TicketServiceTest
  Story point estimate: 1
  Original estimate: 1h
  Description: Cubrir AC-1 (TicketService para atencion de tickets de soporte de las tiendas)

Sub-task 4: Crear 5 ControllerTest
  Summary: test(ms-administracion): 5 ControllerTest con @WebMvcTest
  Story point estimate: 1
  Original estimate: 2h
  Description: Cubrir AC-2 (TiendaControllerTest, MetricaControllerTest, AlertaControllerTest, RespaldoControllerTest, TicketControllerTest con @WebMvcTest + @MockitoBean) y AC-5 (DTO directo, sin HATEOAS)

**DESCRIPCION:**

**Historia de Usuario:**
Como desarrollador del MS Administracion y Soporte
quiero contar con pruebas unitarias de TiendaService, MetricaService, AlertaService, RespaldoService, TicketService y sus controllers
para validar la gestion de tiendas, metricas, alertas, respaldos y tickets

**Dominio EcoMarket SPA:**
El equipo de operaciones usa este MS para dar de alta tiendas, monitorear metricas (ventas/hora, conversion), recibir alertas automaticas (stock bajo, MS caido), restaurar respaldos y atender tickets de soporte.

**Criterios de Aceptacion (formato Gherkin):**

AC-1: Tests de 5 services con Mockito
- Cuando se ejecutan los 5 ServiceTest
- Entonces los archivos usan @ExtendWith(MockitoExtension.class), @Mock y @InjectMocks

AC-2: Tests de 5 controllers con @WebMvcTest + @MockitoBean

AC-3: Alerta automatica por stock bajo
- Dado una metrica con stock bajo el minimo
- Cuando MetricaService detecta la condicion
- Entonces AlertaService genera alerta con severidad ALTA

AC-4: Restauracion de respaldo
- Dado un respaldo valido y una tienda que lo solicito
- Cuando se ejecuta la restauracion
- Entonces se restauran los datos y se registra la operacion

AC-5: Validacion JSON puro / DTO directo (sin _links.self ni _embedded) en colecciones

AC-6: Cobertura JaCoCo >= 80%

**DoD:**
- mvn -f ms-administracion-soporte/pom.xml test ejecuta BUILD SUCCESS
- Reporte JaCoCo >= 80% en docs/evidencias-tecnicas/capturas/ms-administracion-soporte/
- Rama: feature/ep3-tests-ms-administracion-soporte
- PR revisado por Ignacio Valeria
- Commit: `test(ms-administracion): 5 ServiceTest + 5 ControllerTest con reglas de negocio`

===========================================


===========================================
ISSUE: HU-67 — Coleccion Postman E2E para todos los MS (transversal)
===========================================

**CAMPOS A ACTUALIZAR:**

| Campo | Valor |
|---|---|
| Summary (ya esta) | [EP3] Coleccion Postman E2E para todos los MS (transversal) |
| Issue Type (ya esta) | Story |
| Priority (ya esta) | High |
| Assignee (ya esta) | Benjamin Flores |
| Sprint (ya esta) | S5 - Pruebas Unitarias EP3 |
| Epic Link (ya esta) | HU-60 EP-08 Pruebas Unitarias |
| **Story point estimate** | **2** |
| **Original estimate** | **3h** |
| Labels (recomendado) | ep3, tests, postman, e2e |

**SUB-TASKS A CREAR:**

Sub-task 1: Crear coleccion Postman con 7 carpetas
  Summary: test(postman): coleccion E2E con 7 carpetas (1 por MS)
  Story point estimate: 1
  Original estimate: 2h
  Description: Cubrir AC-1 (coleccion JSON v2.1) y AC-2 (7 carpetas: ms-usuarios, ms-catalogo, ms-inventario, ms-pedidos-ventas, ms-logistica, ms-administracion, ms-reportes). Al menos 2 requests por carpeta

Sub-task 2: Crear variables de entorno y README
  Summary: test(postman): variables de entorno + README
  Story point estimate: 1
  Original estimate: 1h
  Description: Cubrir AC-3 (variables baseUrl, jwtToken, runCliente, idPedido) y crear README con instrucciones de uso

**DESCRIPCION:**

**Historia de Usuario:**
Como desarrollador asignado a Postman
quiero crear una coleccion Postman que cubra los endpoints clave de los 7 MS
para validar el flujo end-to-end del marketplace durante la defensa tecnica

**Dominio EcoMarket SPA:**
Flujo E2E happy path: login cliente (ms-usuarios) -> buscar productos (ms-catalogo) -> agregar al carrito (ms-pedidos-ventas) -> aplicar cupon -> pagar -> ver factura. Ademas: consulta de stock (ms-inventario), asignacion de envio (ms-logistica), consulta de reporte (ms-reportes), alta de ticket (ms-administracion).

**Criterios de Aceptacion (formato Gherkin):**

AC-1: Coleccion Postman exportada (JSON v2.1)

AC-2: Cobertura de los 7 MS
- Cuando se revisan las carpetas
- Entonces existen carpetas para: ms-usuarios, ms-catalogo, ms-inventario, ms-pedidos-ventas, ms-logistica, ms-administracion, ms-reportes

AC-3: Variables de entorno (baseUrl, jwtToken, runCliente, idPedido)

AC-4: Flujo E2E happy path
- Dado un cliente autenticado
- Cuando se ejecutan los requests en secuencia (login -> buscar -> carrito -> pagar)
- Entonces la respuesta final es 201 Created con la factura

**DoD:**
- Coleccion importable en Postman sin errores
- 7 carpetas con al menos 2 requests cada una
- Variables de entorno funcionales
- Commit: `test(postman): coleccion E2E para los 7 MS con variables de entorno`

===========================================


===========================================
ISSUE: HU-68 — Pruebas MS Inventario y Abastecimiento (5 services + 5 controllers)
===========================================

**CAMPOS A ACTUALIZAR:**

| Campo | Valor |
|---|---|
| Summary (ya esta) | [EP3] Pruebas MS Inventario y Abastecimiento (5 services + 5 controllers) |
| Issue Type (ya esta) | Story |
| Priority (ya esta) | High |
| Assignee (ya esta) | Benjamin Palma |
| Sprint (ya esta) | S5 - Pruebas Unitarias EP3 |
| Epic Link (ya esta) | HU-60 EP-08 Pruebas Unitarias |
| **Story point estimate** | **8** |
| **Original estimate** | **12h** |
| Labels (recomendado) | ep3, tests, ms-inventario-abastecimiento, mockito |

**SUB-TASKS A CREAR:**

Sub-task 1: Crear InventarioServiceTest + AjusteStockServiceTest
  Summary: test(ms-inventario): InventarioServiceTest + AjusteStockServiceTest con regla stock minimo
  Story point estimate: 2
  Original estimate: 3h
  Description: Cubrir AC-1 (InventarioService y AjusteStockService con @MockitoExtension). Validar AC-3 (regla stock minimo antes de confirmar movimiento: rechaza con excepcion de negocio)

Sub-task 2: Crear PedidoReabastecimientoServiceTest + RecepcionMercanciaServiceTest
  Summary: test(ms-inventario): 2 ServiceTest con flujo reabastecimiento
  Story point estimate: 2
  Original estimate: 3h
  Description: Cubrir AC-1. Validar AC-4 (PedidoReabastecimiento generado por alerta stock bajo con cantidad sugerida) y AC-5 (RecepcionMercancia actualiza stock al confirmar recepcion)

Sub-task 3: Crear ProductoServiceTest del inventario
  Summary: test(ms-inventario): ProductoServiceTest del MS inventario
  Story point estimate: 1
  Original estimate: 1h
  Description: Cubrir AC-1 (ProductoService especifico del MS inventario-abastecimiento, distinto al del MS catalogo)

Sub-task 4: Crear 5 ControllerTest
  Summary: test(ms-inventario): 5 ControllerTest con @WebMvcTest
  Story point estimate: 2
  Original estimate: 3h
  Description: Cubrir AC-2 (InventarioControllerTest, AjusteStockControllerTest, ProductoControllerTest, PedidoReabastecimientoControllerTest, RecepcionMercanciaControllerTest con @WebMvcTest + @MockitoBean) y AC-6 (DTO directo, sin HATEOAS)

**DESCRIPCION:**

**Historia de Usuario:**
Como desarrollador del MS Inventario y Abastecimiento
quiero contar con pruebas unitarias de InventarioService, AjusteStockService, ProductoService, PedidoReabastecimientoService, RecepcionMercanciaService y sus controllers
para asegurar la trazabilidad del stock, reservas, movimientos y alertas

**Dominio EcoMarket SPA:**
El MS Inventario mantiene el stock por tienda y producto, registra los movimientos (entradas/salidas), dispara alertas automaticas cuando el stock esta bajo el minimo, y gestiona los pedidos de reabastecimiento a proveedores.

**Criterios de Aceptacion (formato Gherkin):**

AC-1: Tests de 5 services con Mockito

AC-2: Tests de 5 controllers con @WebMvcTest

AC-3: Regla stock minimo antes de confirmar movimiento
- Dado un movimiento que dejaria el stock bajo el minimo
- Cuando se intenta confirmar
- Entonces el sistema rechaza con excepcion de negocio

AC-4: Generacion de pedido de reabastecimiento
- Dado una alerta de stock bajo
- Cuando el sistema la procesa
- Entonces genera un PedidoReabastecimiento al proveedor

AC-5: Recepcion de mercancia
- Dado un PedidoReabastecimiento en estado EN_TRANSITO
- Cuando se confirma la recepcion
- Entonces se actualiza el stock y se registra el movimiento

AC-6: Validacion JSON puro / DTO directo (sin _links ni _embedded)

AC-7: Cobertura JaCoCo >= 80%

**DoD:**
- mvn -f ms-inventario-abastecimiento/pom.xml test ejecuta BUILD SUCCESS
- Reporte JaCoCo >= 80% en docs/evidencias-tecnicas/capturas/ms-inventario-abastecimiento/
- Rama: feature/ep3-tests-ms-inventario-abastecimiento
- PR revisado por Ignacio Valeria
- Commit: `test(ms-inventario): 5 ServiceTest + 5 ControllerTest con regla stock minimo`

===========================================


===========================================
ISSUE: HU-69 — Pruebas MS Reportes (ReporteService, KpiController, ReporteController)
===========================================

**CAMPOS A ACTUALIZAR:**

| Campo | Valor |
|---|---|
| Summary (ya esta) | [EP3] Pruebas MS Reportes (ReporteService, KpiController, ReporteController) |
| Issue Type (ya esta) | Story |
| Priority (ya esta) | High |
| Assignee (ya esta) | Benjamin Palma |
| Sprint (ya esta) | S5 - Pruebas Unitarias EP3 |
| Epic Link (ya esta) | HU-60 EP-08 Pruebas Unitarias |
| **Story point estimate** | **5** |
| **Original estimate** | **8h** |
| Labels (recomendado) | ep3, tests, ms-reportes, mockito, kpi |

**SUB-TASKS A CREAR:**

Sub-task 1: Crear ReporteServiceTest
  Summary: test(ms-reportes): ReporteServiceTest con mocks de repos
  Story point estimate: 2
  Original estimate: 3h
  Description: Cubrir AC-1 (ReporteService con @MockitoExtension). Validar AC-3 (reglas de negocio: rango fechas no vacio, tipo reporte permitido, tienda existente)

Sub-task 2: Crear ReporteControllerTest
  Summary: test(ms-reportes): ReporteControllerTest con @WebMvcTest
  Story point estimate: 1
  Original estimate: 2h
  Description: Cubrir AC-2 (ReporteControllerTest con @WebMvcTest + @MockitoBean), AC-5 (DTO directo, sin _links ni _embedded)

Sub-task 3: Crear KpiControllerTest
  Summary: test(ms-reportes): KpiControllerTest con validacion de KPIs
  Story point estimate: 1
  Original estimate: 2h
  Description: Cubrir AC-2 (KpiControllerTest con @WebMvcTest). Validar AC-4 (conversion: 25 ventas de 100 pedidos = 25% con redondeo HALF_UP)

**DESCRIPCION:**

**Historia de Usuario:**
Como desarrollador del MS Reportes
quiero contar con pruebas unitarias de ReporteService y los controllers ReporteController y KpiController
para validar la generacion de reportes operacionales y calculo de KPIs

**Dominio EcoMarket SPA:**
El equipo administrativo consulta reportes de ventas (por tienda, por periodo, por producto), inventario (stock bajo, rotacion), y KPIs operacionales (conversion, ticket promedio, ventas por hora).

**Criterios de Aceptacion (formato Gherkin):**

AC-1: ReporteServiceTest con Mockito

AC-2: Tests de 2 controllers con @WebMvcTest

AC-3: Reglas de negocio de reportes
- Dado parametros (rango fechas, tipo, tienda)
- Cuando se invoca /api/v1/reportes
- Entonces se valida: rango fechas no vacio, tipo permitido, tienda existente

AC-4: Calculo de KPIs
- Dado un periodo con 100 pedidos y 25 ventas concretadas
- Cuando se calcula conversion
- Entonces el resultado es 25% con HALF_UP

AC-5: Validacion JSON puro / DTO directo (sin _links.self ni _embedded)

AC-6: Cobertura JaCoCo >= 80%

**DoD:**
- mvn -f ms-reportes/pom.xml test ejecuta BUILD SUCCESS
- Reporte JaCoCo >= 80% en docs/evidencias-tecnicas/capturas/ms-reportes/
- Rama: feature/ep3-tests-ms-reportes
- PR revisado por Ignacio Valeria
- Commit: `test(ms-reportes): ReporteServiceTest + 2 ControllerTest con validacion de KPIs`

===========================================


===========================================
ISSUE: HU-70 ? Preparacion defensa tecnica individual
===========================================

**CAMPOS A ACTUALIZAR EN JIRA:**

| Campo | Valor |
|---|---|
| Summary (REEMPLAZAR si dice "transversal") | [EP3] Preparacion defensa tecnica individual |
| Issue Type (ya esta) | Story |
| Priority (ya esta) | High |
| Assignee | Ignacio Valeria (IN) |
| Sprint (ya esta) | S5 - Pruebas Unitarias EP3 |
| Epic Link (ya esta) | HU-60 EP-08 Pruebas Unitarias |
| Story point estimate | 3 |
| Labels (recomendado) | ep3, defensa, documentacion |
| Watchers (agregar panel derecho) | Benjamin Espinoza, Benjamin Flores, Benjamin Palma |

**SUB-TASKS A CREAR (3):**

Sub-task 1: docs(defensa): Rubrica de preguntas por HU con respuestas de cada integrante
  Summary: docs(defensa): Rubrica de preguntas por HU con respuestas de cada integrante
  Description: Cada integrante prepara respuestas a 5 preguntas probables sobre su HU: (1) que cubre, (2) por que esa tecnologia, (3) como se mockea, (4) que cobertura JaCoCo se logro, (5) que mejorarias. Ignacio: HU-61, HU-62, HU-71. Espinoza: HU-63, HU-64. Flores: HU-65, HU-66, HU-67. Palma: HU-68, HU-69. Formato markdown en docs/defensa/preguntas-<iniciales>.md

Sub-task 2: docs(defensa): Comandos demo y checklist de ejecucion local
  Summary: docs(defensa): Comandos demo y checklist de ejecucion local
  Description: Documentar los comandos exactos para levantar el sistema completo en los PCs del instituto: (1) levantar cada MS con mvn spring-boot:run en su puerto, (2) levantar api-gateway, (3) correr suite de tests con mvn test en cada MS, (4) ejecutar la coleccion Postman E2E, (5) abrir navegador en localhost:8081 y demostrar enrutamiento. Guardar en docs/defensa/comandos-demo.md con copy-paste directo.

Sub-task 3: docs(defensa): Checklist de cierre Sprint 5 + cobertura-resumen.md
  Summary: docs(defensa): Checklist de cierre Sprint 5 + cobertura-resumen.md
  Description: Verificar antes de la defensa: (1) las 11 issues de Jira en estado Done o In Progress con sus sub-tasks completas, (2) reporte JaCoCo >= 80% en cada MS guardado en docs/evidencias-tecnicas/capturas/, (3) log de runtime del gateway con 17 rutas OK en docs/evidencias-tecnicas/capturas/api-gateway/runtime-17-rutas.log, (4) capturas de pantalla de los tests pasando, (5) README del repo actualizado con instrucciones de ejecucion. Crear docs/evidencias-tecnicas/cobertura-resumen.md con tabla de % por MS. Ignacio coordina, cada integrante sube su evidencia.

**DESCRIPCION (campo Description de la issue HU-70):**

**Historia de Usuario:**
Como equipo de desarrollo, queremos documentar y preparar la defensa tecnica individual de las pruebas unitarias implementadas para cumplir con el IE 3.1.2 (7%) y los demas IE de Defensa Tecnica (60% del EP3).

**Dominio EcoMarket SPA:**
La defensa tecnica del EP3 vale 60% de la nota. El profesor evaluara oralmente: (1) como funciona el codigo de las pruebas unitarias (IE 3.1.2 = 7%), (2) consistencia de datos e interoperabilidad entre MS (IE 2.4.2 = 5%), (3) aporte personal al proyecto (IE 2.5.3 = 5%), (4) ejecucion de microservicios en vivo (IE 3.3.7 = 10%) y (5) configuracion YAML del gateway (IE 3.3.5 = 4%). Cada integrante debe poder defender su HU asignada en ~5 min.

**Criterios de Aceptacion (formato Gherkin):**

AC-1: Preguntas frecuentes respondidas por cada integrante
- Dado cada integrante con su HU asignada en el sprint 5
- Cuando se le pregunta sobre sus tests
- Entonces puede explicar: Given-When-Then, funcion evaluada, proposito de cada assert, mocks usados, reglas de negocio validadas e interpretacion de resultados

AC-2: Comandos demo documentados
- Dado un PC del instituto limpio
- Cuando se siguen los comandos de docs/defensa/comandos-demo.md
- Entonces el sistema completo (7 MS + gateway) se levanta en menos de 5 min y los 7 MS responden en sus puertos

AC-3: Resumen de cobertura por MS
- Cuando se genera el reporte JaCoCo de los 7 MS
- Entonces existe docs/evidencias-tecnicas/cobertura-resumen.md con tabla % por MS y total acumulado

AC-4: Ejecucion en vivo durante la defensa
- Dado el sistema levantado en PCs del instituto
- Cuando el profesor pide ejecutar mvn test en el MS asignado
- Entonces el integrante ejecuta el comando, muestra Tests run >= X verde, y abre 1 test relevante para explicarlo linea por linea

**Asignaciones por integrante (sub-tasks):**

| Integrante | HUs a defender | MS asignado |
|---|---|---|
| Ignacio Valeria | HU-61, HU-62, HU-71 | api-gateway + ms-pedidos-ventas |
| Benjamin Espinoza | HU-63, HU-64 | ms-catalogo + ms-logistica-envios |
| Benjamin Flores | HU-65, HU-66, HU-67 | ms-usuarios-identidad + ms-administracion-soporte + Postman E2E |
| Benjamin Palma | HU-68, HU-69 | ms-inventario-abastecimiento + ms-reportes |

**Archivos esperados:**
- docs/defensa/preguntas-ignacio.md (Ignacio: HU-61, HU-62, HU-71)
- docs/defensa/preguntas-espinoza.md (Espinoza: HU-63, HU-64)
- docs/defensa/preguntas-flores.md (Flores: HU-65, HU-66, HU-67)
- docs/defensa/preguntas-palma.md (Palma: HU-68, HU-69)
- docs/defensa/comandos-demo.md (coordinado por Ignacio)
- docs/evidencias-tecnicas/cobertura-resumen.md (coordinado por Ignacio)

**DoD:**
- 4 archivos preguntas-<iniciales>.md creados (uno por integrante)
- comandos-demo.md validado funcionando en PC local
- cobertura-resumen.md con 7 MS y su % JaCoCo
- Cada integrante practica 1 vez la defensa oral antes del dia real
- PR hacia develop con todos los archivos de defensa

===========================================
ISSUE: HU-71 - Pruebas MS Pedidos y Ventas (5 services + 5 controllers + 1 IT)
===========================================

**CAMPOS A ACTUALIZAR:**

| Campo | Valor |
|---|---|
| Summary (REEMPLAZAR) | [EP3] Pruebas MS Pedidos y Ventas (autor, 5 services + 5 controllers + 1 IT) |
| Issue Type (ya esta) | Story |
| Priority (ya esta) | High |
| Assignee (ya esta) | Ignacio Valeria |
| Sprint (ya esta) | S5 - Pruebas Unitarias EP3 |
| Epic Link (ya esta) | HU-60 EP-08 Pruebas Unitarias |
| Story point estimate | 13 |
| Labels (recomendado) | ep3, tests, ms-pedidos-ventas, mockito, iva, transicion-estados, devolucion |

**ESTRUCTURA REAL DEL MS (verificada contra ms-pedidos-ventas/src/main/java/com/ecomarket/pedidos/):**
- service/PedidoService.java (nucleo: crear pedido, transiciones de estado, IVA)
- service/VentaService.java (facturacion, calculo de IVA directo)
- service/CarritoService.java (alta, agregar items, aplicar cupon)
- service/CuponDescuentoService.java (validar, calcular descuento)
- service/DevolucionService.java (nota de credito, gestion de ReclamacionRepository)
- repository/ReclamacionRepository.java (las reclamaciones NO tienen service propio, se gestionan via DevolucionService)
- service/CatalogoClientService.java, InventarioClientService.java, LogisticaClientService.java (clientes REST, no son dominio de negocio testeable aqui)
- controller/PedidoController.java, VentaController.java, CarritoController.java, CuponDescuentoController.java, DevolucionController.java (5 controllers)

**REGLA DE IVA VERIFICADA EN CODIGO (NO INVENTAR):**
- VentaService.java:58 -> `double iva = Math.round(subtotal * 0.19 * 100.0) / 100.0;` con comentario `// IE 2.2.1: Calculo de IVA (19%) sobre la base imponible`
- PedidoService.java:113-114 -> `iva = Math.round((subtotal - descuento) * 0.19 * 100.0) / 100.0`
- El IVA es DIRECTO sobre la base imponible, NO se calcula hacia atras desde un total con IVA incluido.
- Ejemplo correcto: subtotal=1000, descuento=0 -> iva=190, total=1190
- Ejemplo con descuento: subtotal=1000, descuento=100 -> iva=171 (redondeo HALF_UP de 171.0), total=1071

**SUB-TASKS A CREAR:**

Sub-task 1: Crear PedidoServiceTest (el mas completo)
  Summary: test(ms-pedidos-ventas): PedidoServiceTest con IVA 19% directo y transiciones de estado
  Story point estimate: 3
  Original estimate: 4h
  Description: Cubrir AC-1 (PedidoService con @MockitoExtension + @Mock de PedidoRepository, CarritoCompraRepository, HistorialPedidoRepository, ReclamacionRepository + @Mock de CatalogoClientService, InventarioClientService, LogisticaClientService). Validar AC-3 (IVA 19% directo: subtotal=1000, descuento=0 -> iva=190, total=1190; subtotal=1000, descuento=100 -> iva=171, total=1071, redondeo HALF_UP), AC-4 (carrito no vacio al crear pedido: rechaza 0 items con IllegalStateException), AC-7 (transicion CREADO -> PAGADO -> EN_PREPARACION -> ENVIADO -> ENTREGADO sin saltarse pasos). Test es del autor Ignacio Valeria (nucleo del MS).

Sub-task 2: Crear VentaServiceTest + CarritoServiceTest
  Summary: test(ms-pedidos-ventas): 2 ServiceTest con Mockito
  Story point estimate: 2
  Original estimate: 3h
  Description: Cubrir AC-1. VentaServiceTest valida facturacion electronica (CrearFacturaRequest, FacturaResponse) y el calculo de IVA directo (subtotal * 0.19 redondeado HALF_UP a 2 decimales). CarritoServiceTest valida AC-4 (carrito no vacio: rechaza operacion con 0 items), agregar/quitar items, recalcularSubtotal en ItemCarrito.

Sub-task 3: Crear CuponDescuentoServiceTest + DevolucionServiceTest
  Summary: test(ms-pedidos-ventas): 2 ServiceTest con reglas de negocio
  Story point estimate: 2
  Original estimate: 3h
  Description: Cubrir AC-1. CuponDescuentoServiceTest valida AC-5 (descuento porcentual: subtotal=1000, valorDescuento=10 -> descuento=100; descuento fijo: subtotal=1000, valorDescuento=150 -> descuento=min(150,1000)=150), AC-6 (cupon exhaurido lanza BusinessException con mensaje "cupon agotado"), validacion de monto minimo. DevolucionServiceTest valida AC-8 (devolucion parcial: venta con 3 items, se devuelve 1, recalcula IVA de la nota de credito; nota de credito usa el mismo calculo de IVA directo que la venta original).

Sub-task 4: Crear 5 ControllerTest con DTO directo
  Summary: test(ms-pedidos-ventas): 5 ControllerTest con @WebMvcTest + DTO directo
  Story point estimate: 3
  Original estimate: 5h
  Description: Cubrir AC-2 (PedidoControllerTest, VentaControllerTest, CarritoControllerTest, CuponDescuentoControllerTest, DevolucionControllerTest con @WebMvcTest + @MockitoBean del service correspondiente) y AC-9 (DTO directo en GET/POST/PUT/DELETE, List<DTO> en colecciones, sin _links ni _embedded). NO hay ReclamacionController; las endpoints de reclamacion viven en DevolucionController o PedidoController (verificar). 5 controllers totales, no 6.

Sub-task 5: Crear PedidoControllerIT (test de integracion end-to-end)
  Summary: test(ms-pedidos-ventas): PedidoControllerIT con @SpringBootTest + H2
  Story point estimate: 3
  Original estimate: 5h
  Description: Cubrir AC-10. Test de integracion con @SpringBootTest + H2 que valida el flujo end-to-end: crear carrito -> agregar 2 items -> aplicar cupon -> pagar -> ver factura. Usa MockMvc o TestRestTemplate. Verifica que el calculo de IVA directo del flujo completo arroja los valores esperados (subtotal X -> iva X*0.19 -> total X*1.19).

**DESCRIPCION:**

**Historia de Usuario:**
Como desarrollador del MS Pedidos y Ventas
quiero contar con pruebas unitarias de PedidoService, VentaService, CarritoService, CuponDescuentoService, DevolucionService y sus 5 controllers
para validar el flujo comercial completo: carrito, pedidos, ventas, pagos, facturas, cupones, devoluciones y notas de credito

**Dominio EcoMarket SPA:**
Este es el nucleo comercial del marketplace. Registra pedidos, ventas, devoluciones, cupones, carritos y facturacion con IVA 19% (regla tributaria chilena, calculo directo sobre la base imponible). Las acciones del Empleado de Ventas son: "Registrar Ventas (aplicando descuentos y ofertas)", "Atender Devoluciones y Reclamaciones", "Generar Facturas electronicas". El Cliente "Agrega productos al carrito, realiza pedidos, consulta historial, aplica cupones y descuentos". Las reclamaciones del cliente NO tienen un service propio: se persisten en ReclamacionRepository y se gestionan a traves de DevolucionService.

**Criterios de Aceptacion (formato Gherkin):**

AC-1: Tests de 5 services con Mockito
- Cuando se ejecutan los 5 ServiceTest
- Entonces existen PedidoServiceTest, VentaServiceTest, CarritoServiceTest, CuponDescuentoServiceTest, DevolucionServiceTest con @ExtendWith(MockitoExtension.class) + @Mock + @InjectMocks

AC-2: Tests de 5 controllers con @WebMvcTest + @MockitoBean
- Cuando se ejecutan los 5 ControllerTest
- Entonces existen tests para PedidoController, VentaController, CarritoController, CuponDescuentoController, DevolucionController

AC-3: Calculo de IVA 19% DIRECTO sobre la base imponible (alineado con VentaService.java:58 y PedidoService.java:113-114)
- Dado subtotal=1000, descuento=0
- Cuando PedidoService/VentaService calcula el total
- Entonces iva = 190, total = 1190
- Dado subtotal=1000, descuento=100
- Cuando se calcula el total
- Entonces iva = 171, total = 1071
- Redondeo HALF_UP a 2 decimales (no a 1)

AC-4: Regla carrito no vacio al crear pedido
- Dado un carrito con 0 items
- Cuando se intenta crear pedido
- Entonces rechaza con IllegalStateException (mensaje "carrito vacio" o similar)

AC-5: Aplicacion de cupon (porcentual y fijo)
- Dado un carrito con subtotal 1000 y cupon DESC10 (porcentual, valorDescuento=10)
- Cuando se aplica el cupon
- Entonces descuento = 100, total = 900 + IVA
- Dado un carrito con subtotal 1000 y cupon FIJO150 (tipo fijo, valorDescuento=150)
- Cuando se aplica el cupon
- Entonces descuento = 150, total = 850 + IVA

AC-6: Cupon exhaurido lanza excepcion
- Dado un cupon con usos disponibles = 0 (o usosRestantes = 0)
- Cuando se intenta aplicar
- Entonces lanza BusinessException con mensaje "cupon agotado" o equivalente

AC-7: Transicion de estados del pedido
- Dado un pedido en estado CREADO
- Cuando se invoca pagar() -> preparar() -> enviar() -> entregar()
- Entonces pasa por PAGADO -> EN_PREPARACION -> ENVIADO -> ENTREGADO sin saltarse pasos
- Y un cambio invalido (ej. CREADO -> ENTREGADO directo) lanza IllegalStateException

AC-8: Devolucion parcial con recalculo de IVA en nota de credito
- Dado una venta con 3 items (subtotal 3000) y se devuelve 1 item (subtotal del item devuelto = 1000)
- Cuando se procesa la devolucion parcial
- Entonces se genera nota de credito con: subtotal=1000, iva=190, total=1190
- Y el calculo de IVA sigue siendo DIRECTO (no inverso)

AC-9: Validacion JSON puro / DTO directo en endpoints REST
- Cuando se invocan GET/POST/PUT/DELETE
- Entonces el JSON expone DTOs directos (sin _links ni _embedded)

AC-10: Test de integracion end-to-end (PedidoControllerIT)
- Dado contexto Spring con H2 + @SpringBootTest
- Cuando se ejecuta crear carrito -> agregar 2 items -> aplicar cupon -> pagar -> ver factura
- Entonces el test pasa en verde y el JSON de factura tiene iva=190 (subtotal 1000 sin descuento)

AC-11: Cobertura JaCoCo >= 80%
- Cuando se ejecuta mvn -f ms-pedidos-ventas/pom.xml test con jacoco:report
- Entonces el reporte muestra >= 80% de line coverage acumulado

**DoD:**
- mvn -f ms-pedidos-ventas/pom.xml test ejecuta BUILD SUCCESS (5 ServiceTest + 5 ControllerTest + 1 IT)
- Reporte JaCoCo >= 80% en docs/evidencias-tecnicas/capturas/ms-pedidos-ventas/
- Rama: feature/ep3-tests-ms-pedidos-ventas
- PR revisado por Benjamin Flores (cross-review)
- Commit: `test(ms-pedidos-ventas): 5 ServiceTest + 5 ControllerTest + 1 IT con IVA 19% directo`
