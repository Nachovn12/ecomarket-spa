# API Gateway

Componente tecnico de entrada para EcoMarket SPA. Centraliza el acceso REST hacia los microservicios y evita que los clientes tengan que conocer el puerto interno de cada servicio.

## Responsable

| Campo | Detalle |
| --- | --- |
| Responsable principal | Ignacio Valeria |
| Componente | API Gateway |
| Rama de trabajo | `feature/api-gateway` |
| Base de datos | No aplica |
| Puerto local | `8081` |
| URL base local | `http://localhost:8081` |

## Que hace

- Expone un punto unico de entrada para el backend.
- Redirige solicitudes REST hacia los microservicios correspondientes.
- Mantiene rutas ordenadas por dominio: usuarios, catalogo, inventario, pedidos, logistica, administracion y reportes.
- Permite integrar clientes como Frontend Web, Postman o Caja POS usando una URL base comun.
- Expone endpoints Actuator para validacion tecnica.

## Que no hace

- No implementa logica de negocio.
- No tiene base de datos propia.
- No tiene entidades JPA ni repositorios.
- No reemplaza a los microservicios de dominio.
- No almacena informacion del sistema.

## Tecnologias

- Java 21
- Spring Boot
- Spring Cloud Gateway MVC
- Spring Actuator
- Maven
- JUnit

## Configuracion

El archivo principal de configuracion esta en:

```text
src/main/resources/application.properties
```

Valores principales:

```properties
server.port=8081
spring.application.name=api-gateway
management.endpoints.web.exposure.include=health,info
management.endpoint.health.show-details=always
```

## Como ejecutar

Desde la raiz del repositorio:

```powershell
cd .\api-gateway\
.\mvnw.cmd spring-boot:run
```

## Como probar

```powershell
.\mvnw.cmd test
```

O desde la raiz:

```powershell
mvn -f api-gateway/pom.xml clean test
```

## Orden recomendado de ejecucion

```text
1. Iniciar MySQL o XAMPP.
2. Levantar los microservicios necesarios.
3. Levantar el API Gateway.
4. Consumir endpoints desde Postman o cliente web usando http://localhost:8081.
```

## Rutas configuradas

| Ruta Gateway | Microservicio destino | URL interna |
| --- | --- | --- |
| `/api/auth/**` | MS Usuarios e Identidad | `http://localhost:8083` |
| `/api/usuarios/**` | MS Usuarios e Identidad | `http://localhost:8083` |
| `/api/productos/**` | MS Catalogo | `http://localhost:8084` |
| `/api/categorias/**` | MS Catalogo | `http://localhost:8084` |
| `/api/inventario/**` | MS Inventario y Abastecimiento | `http://localhost:8085` |
| `/api/stock/**` | MS Inventario y Abastecimiento | `http://localhost:8085` |
| `/api/pedidos/**` | MS Pedidos y Ventas | `http://localhost:8086` |
| `/api/ventas/**` | MS Pedidos y Ventas | `http://localhost:8086` |
| `/api/envios/**` | MS Logistica de Envios | `http://localhost:8087` |
| `/api/rutas/**` | MS Logistica de Envios | `http://localhost:8087` |
| `/api/admin/**` | MS Administracion y Soporte | `http://localhost:8088` |
| `/api/soporte/**` | MS Administracion y Soporte | `http://localhost:8088` |
| `/api/v1/reportes/**` | MS Reportes | `http://localhost:8089` |
| `/api/v1/kpis/**` | MS Reportes | `http://localhost:8089` |

## Ejemplos de uso

Login mediante Gateway:

```http
POST http://localhost:8081/api/auth/login
```

Listar productos mediante Gateway:

```http
GET http://localhost:8081/api/productos
```

Consultar estado del Gateway:

```http
GET http://localhost:8081/actuator/health
```

## Documentacion relacionada

- `../docs/api-gateway-rutas.md`
- `../docs/arquitectura/arquitectura-microservicios.md`
- `../docs/integracion/comunicacion-rest-entre-servicios.md`
- `../docs/postman/postman-validacion.md`
