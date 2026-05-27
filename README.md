# EcoMarket SPA — Backend con Microservicios

## 1. Información del proyecto

| Campo           | Detalle                                                       |
| --------------- | ------------------------------------------------------------- |
| Proyecto        | EcoMarket SPA                                                 |
| Asignatura      | Desarrollo Full Stack I — DSY1103                             |
| Sección         | 003D                                                          |
| Institución     | Duoc UC                                                       |
| Tipo de entrega | EP2, Entrega de Encargo grupal Parte 1                        |
| Tipo de trabajo | Grupal                                                        |
| Arquitectura    | Microservicios independientes con API Gateway                 |
| Persistencia    | MySQL por microservicio                                       |
| Backend         | Java 21, Spring Boot, Maven, JPA/Hibernate, REST API, HATEOAS |

---

## 2. Integrantes del equipo

| Integrante        | Microservicio(s) asignado(s) en Jira                              | Rol o responsabilidad principal                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
| ----------------- | ----------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Benjamín Espinoza | MS Logística de Envíos, MS Catálogo                               | Desarrollo de los microservicios de logística y catálogo. Implementó funcionalidades asociadas a envíos, rutas, proveedores, seguimiento de envíos, productos, categorías, reseñas y búsqueda. Participó en validaciones de negocio, respuestas HATEOAS, pruebas manuales con Postman y pruebas unitarias con JUnit de sus microservicios asignados.                                                                                                                                                                                                                                  |
| Benjamín Flores   | MS Pedidos y Ventas                                               | Desarrollo del microservicio comercial. Implementó funcionalidades relacionadas con carrito de compras, pedidos, ventas presenciales, pagos, facturas, cupones, devoluciones, reclamaciones, validación de estados, documentación del flujo comercial, pruebas manuales con Postman y pruebas unitarias del microservicio.                                                                                                                                                                                                                                                                 |
| Benjamín Palma    | MS Inventario y Abastecimiento, MS Reportes                       | Desarrollo de los microservicios de inventario y reportes. Implementó funcionalidades asociadas a stock, reservas, movimientos de inventario, alertas, reportes de ventas, reportes de inventario, rendimiento de tienda, indicadores KPI, exportación de reportes, validaciones mediante DTOs, manejo de errores, respuestas HATEOAS y pruebas unitarias con JUnit.                                                                                                                                                                                                                  |
| Ignacio Valeria   | MS Usuarios e Identidad, MS Administración y Soporte, API Gateway | Desarrollo de los microservicios de usuarios, identidad, administración y soporte, además de la configuración del API Gateway. Implementó funcionalidades asociadas a registro, login, roles, permisos, tiendas, tickets de soporte, alertas, métricas y respaldos. Participó en la integración en la rama `develop`, revisión de Pull Requests, validación de builds/tests, documentación de arquitectura, comunicación REST, Postman, Git Flow y preparación de evidencias técnicas para la entrega EP2. |

---

## 3. Descripción general

EcoMarket SPA es una empresa chilena dedicada a la venta de productos ecológicos y sostenibles. Cuenta con tiendas físicas en Santiago, Valdivia y Antofagasta, además de una tienda online orientada a clientes web.

El sistema monolítico original presenta problemas de rendimiento, baja disponibilidad, alto acoplamiento y riesgo de punto único de fallo. Para resolver estos problemas, el proyecto propone una arquitectura backend distribuida basada en microservicios independientes, cada uno con su propia base de datos relacional MySQL.

La solución busca demostrar la aplicación de arquitectura distribuida, persistencia de datos mediante repositorios, comunicación REST entre servicios, patrón Controller-Service-Repository, validaciones, reglas de negocio, manejo de excepciones, logs y documentación técnica para la defensa del proyecto.

---

## 4. Objetivo del proyecto

Desarrollar un backend compuesto por microservicios independientes para EcoMarket SPA, permitiendo separar responsabilidades por dominio, mejorar la mantenibilidad del sistema, reducir el acoplamiento y facilitar la integración entre componentes mediante APIs REST.

---

## 5. Arquitectura general

El sistema se compone de siete microservicios de negocio y un API Gateway.

### 5.1 Diagrama de arquitectura

![Diagrama de arquitectura de microservicios EcoMarket](docs/diagramas/arquitectura/diagrama-arquitectura-microservicios-ecomarket.png)

### 5.2 Diagrama de despliegue

![Diagrama de despliegue backend EcoMarket](docs/diagramas/despliegue/diagrama-despliegue-backend-ecomarket.png)

---

## 6. Microservicios del sistema

| Microservicio                  | Carpeta                        | Base de datos MySQL | Responsabilidad                                                   |
| ------------------------------ | ------------------------------ | ------------------- | ----------------------------------------------------------------- |
| MS Usuarios e Identidad        | `ms-usuarios-identidad`        | `bd_usuarios`       | Login, registro, roles, permisos y usuarios internos              |
| MS Catálogo                    | `ms-catalogo`                  | `bd_catalogo`       | Productos, categorías, reseñas, búsqueda y atributos ecológicos   |
| MS Inventario y Abastecimiento | `ms-inventario-abastecimiento` | `bd_inventario`     | Stock, reservas, movimientos y alertas                            |
| MS Pedidos y Ventas            | `ms-pedidos-ventas`            | `bd_ventas`         | Carrito, pedidos, ventas, pagos, facturas, cupones y devoluciones |
| MS Logística de Envíos         | `ms-logistica-envios`          | `bd_logistica`      | Envíos, rutas, proveedores y seguimiento                          |
| MS Administración y Soporte    | `ms-administracion-soporte`    | `bd_admin`          | Tiendas, tickets, alertas, métricas y respaldos                   |
| MS Reportes                    | `ms-reportes`                  | `bd_reportes`       | Reportes, KPIs, exportaciones y auditoría                         |
| API Gateway                    | `api-gateway`                  | No aplica           | Enrutamiento REST y punto único de entrada                        |

---

## 7. Tecnologías utilizadas

| Categoría                  | Tecnología           |
| -------------------------- | -------------------- |
| Lenguaje                   | Java 21              |
| Framework backend          | Spring Boot          |
| Gestión de dependencias    | Maven                |
| Persistencia               | JPA/Hibernate        |
| Base de datos principal    | MySQL                |
| Base de datos para pruebas | H2                   |
| Pruebas automatizadas      | JUnit                |
| API REST                   | Spring Web           |
| HATEOAS                    | Spring HATEOAS       |
| Control de versiones       | Git y GitHub         |
| Pruebas manuales           | Postman              |
| Gateway                    | Spring Cloud Gateway |

---

## 8. Estructura general del repositorio

```text
ecomarket-spa/
│
├── api-gateway/
│
├── ms-usuarios-identidad/
├── ms-catalogo/
├── ms-inventario-abastecimiento/
├── ms-pedidos-ventas/
├── ms-logistica-envios/
├── ms-administracion-soporte/
├── ms-reportes/
│
├── docs/
│   ├── arquitectura/
│   ├── calidad/
│   ├── diagramas/
│   ├── evidencias/
│   ├── evidencias-tecnicas/
│   ├── hateoas/
│   ├── integracion/
│   └── postman/
│
└── README.md
```

Cada microservicio mantiene una estructura similar:

```text
src/
├── main/
│   ├── java/
│   │   └── com/ecomarket/...
│   │       ├── controller/
│   │       ├── service/
│   │       ├── repository/
│   │       ├── model/
│   │       ├── dto/
│   │       ├── exception/
│   │       └── config/
│   │
│   └── resources/
│       └── application.properties
│
└── test/
    ├── java/
    └── resources/
```

---

## 9. Requisitos previos

Antes de ejecutar el proyecto, se debe contar con las siguientes herramientas instaladas:

| Herramienta                        | Uso                                          |
| ---------------------------------- | -------------------------------------------- |
| Java JDK 21 o superior             | Ejecutar aplicaciones Spring Boot            |
| Maven 3.9 o superior               | Compilar, probar y empaquetar microservicios |
| MySQL o XAMPP                      | Motor de base de datos local                 |
| Git                                | Control de versiones                         |
| Postman                            | Pruebas manuales de endpoints REST           |
| Visual Studio Code o IntelliJ IDEA | Edición y revisión del código                |

Verificar instalación de Java:

```powershell
java -version
```

Verificar instalación de Maven:

```powershell
mvn -version
```

Verificar instalación de Git:

```powershell
git --version
```

---

## 10. Clonar el repositorio

Ejecutar en una terminal:

```powershell
git clone https://github.com/Nachovn12/ecomarket-spa.git
cd ecomarket-spa
git status
```

Al clonar el repositorio, el proyecto queda posicionado en la rama principal `main`, correspondiente a la rama final de entrega.

Resultado esperado:

```text
On branch main
Your branch is up to date with 'origin/main'.

nothing to commit, working tree clean
```

---

## 11. Configuración de bases de datos MySQL

El proyecto utiliza una base de datos MySQL independiente por microservicio.

Las bases de datos utilizadas en phpMyAdmin son:

```text
bd_admin
bd_catalogo
bd_inventario
bd_logistica
bd_reportes
bd_usuarios
bd_ventas
```

Crear las bases de datos ejecutando el siguiente script en MySQL Workbench, phpMyAdmin o consola MySQL:

```sql
CREATE DATABASE IF NOT EXISTS bd_usuarios
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_catalogo
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_inventario
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_ventas
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_logistica
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_admin
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

CREATE DATABASE IF NOT EXISTS bd_reportes
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

Si se utiliza XAMPP:

```text
1. Abrir XAMPP Control Panel.
2. Iniciar el servicio MySQL.
3. Abrir phpMyAdmin.
4. Ejecutar el script SQL anterior.
5. Verificar que las bases de datos aparezcan en el panel lateral.
```

---

## 12. Configuración de `application.properties`

Cada microservicio debe tener configurada su conexión a MySQL en:

```text
src/main/resources/application.properties
```

Configuración base esperada:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/NOMBRE_BD
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

En la propiedad `spring.datasource.url`, el valor `NOMBRE_BD` representa un nombre genérico de ejemplo y debe reemplazarse por la base de datos correspondiente a cada microservicio.

En una instalación típica de XAMPP para desarrollo local o laboratorio, MySQL utiliza el usuario `root` sin contraseña:

```properties
spring.datasource.username=root
spring.datasource.password=
```

Si el equipo donde se ejecuta el proyecto tiene MySQL configurado con contraseña, se debe reemplazar el valor de `spring.datasource.password` por la contraseña correspondiente.

Por seguridad, no se deben subir contraseñas reales al repositorio.

### 12.1 MS Usuarios e Identidad

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bd_usuarios
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### 12.2 MS Catálogo

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bd_catalogo
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### 12.3 MS Inventario y Abastecimiento

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bd_inventario
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### 12.4 MS Pedidos y Ventas

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bd_ventas
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### 12.5 MS Logística de Envíos

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bd_logistica
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### 12.6 MS Administración y Soporte

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bd_admin
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### 12.7 MS Reportes

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bd_reportes
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

---

## 13. Puertos sugeridos

| Componente                     | Puerto configurado |
| ------------------------------ | -----------------: |
| API Gateway                    |               8081 |
| MS Usuarios e Identidad        |               8083 |
| MS Catálogo                    |               8084 |
| MS Inventario y Abastecimiento |               8085 |
| MS Pedidos y Ventas            |               8086 |
| MS Logística de Envíos         |               8087 |
| MS Administración y Soporte    |               8088 |
| MS Reportes                    |               8089 |

Los puertos fueron configurados considerando la ejecución local y los equipos del instituto, donde algunos puertos comunes como `8080` y `8082` pueden estar ocupados por otros servicios.

Si algún puerto se encuentra ocupado en otro entorno, se debe modificar la propiedad `server.port` en el archivo `application.properties` del componente correspondiente.

---

## 14. Ejecución de microservicios

Cada microservicio se ejecuta de forma independiente.

Se recomienda abrir una terminal por cada microservicio que se desee levantar.

### 14.1 Ejecutar MS Usuarios e Identidad

```powershell
cd .\ms-usuarios-identidad\
.\mvnw.cmd spring-boot:run
```

### 14.2 Ejecutar MS Catálogo

```powershell
cd .\ms-catalogo\
.\mvnw.cmd spring-boot:run
```

### 14.3 Ejecutar MS Inventario y Abastecimiento

```powershell
cd .\ms-inventario-abastecimiento\
.\mvnw.cmd spring-boot:run
```

### 14.4 Ejecutar MS Pedidos y Ventas

```powershell
cd .\ms-pedidos-ventas\
.\mvnw.cmd spring-boot:run
```

### 14.5 Ejecutar MS Logística de Envíos

```powershell
cd .\ms-logistica-envios\
.\mvnw.cmd spring-boot:run
```

### 14.6 Ejecutar MS Administración y Soporte

```powershell
cd .\ms-administracion-soporte\
.\mvnw.cmd spring-boot:run
```

### 14.7 Ejecutar MS Reportes

```powershell
cd .\ms-reportes\
.\mvnw.cmd spring-boot:run
```

### 14.8 Ejecutar API Gateway

```powershell
cd .\api-gateway\
.\mvnw.cmd spring-boot:run
```

---

## 15. Orden recomendado para ejecutar el sistema localmente

Para validar el sistema de forma local, se recomienda levantar los servicios en el siguiente orden:

```text
1. MySQL o XAMPP.
2. Crear o verificar las bases de datos bd_*.
3. Ejecutar los microservicios necesarios para la prueba.
4. Ejecutar el API Gateway.
5. Ejecutar pruebas manuales desde Postman.
```

Para una prueba completa, levantar:

```text
1. MS Usuarios e Identidad.
2. MS Catálogo.
3. MS Inventario y Abastecimiento.
4. MS Pedidos y Ventas.
5. MS Logística de Envíos.
6. MS Administración y Soporte.
7. MS Reportes.
8. API Gateway.
```

Para una prueba parcial, solo se deben levantar los microservicios involucrados en el flujo a validar.

---

## 16. Compilación y pruebas automatizadas

Para validar el proyecto, se ejecutaron pruebas por microservicio.

### 16.1 Ejecutar tests por microservicio

Desde la raíz del repositorio:

```powershell
mvn -f ms-usuarios-identidad/pom.xml clean test
mvn -f ms-catalogo/pom.xml clean test
mvn -f ms-inventario-abastecimiento/pom.xml clean test
mvn -f ms-pedidos-ventas/pom.xml clean test
mvn -f ms-logistica-envios/pom.xml clean test
mvn -f ms-administracion-soporte/pom.xml clean test
mvn -f ms-reportes/pom.xml clean test
```

Resultado esperado:

```text
BUILD SUCCESS
```

### 16.2 Ejecutar package por microservicio

Desde la raíz del repositorio:

```powershell
mvn -f ms-usuarios-identidad/pom.xml clean package
mvn -f ms-catalogo/pom.xml clean package
mvn -f ms-inventario-abastecimiento/pom.xml clean package
mvn -f ms-pedidos-ventas/pom.xml clean package
mvn -f ms-logistica-envios/pom.xml clean package
mvn -f ms-administracion-soporte/pom.xml clean package
mvn -f ms-reportes/pom.xml clean package
```

Resultado esperado:

```text
BUILD SUCCESS
```

---

## 17. Evidencia de pruebas ejecutadas

La evidencia formal de build y tests se encuentra en:

```text
docs/evidencias/evidencia-build-tests.md
```

Resumen de resultados:

| Microservicio                  | Tests ejecutados | Resultado     |
| ------------------------------ | ---------------: | ------------- |
| MS Usuarios e Identidad        |                1 | BUILD SUCCESS |
| MS Catálogo                    |                1 | BUILD SUCCESS |
| MS Inventario y Abastecimiento |                5 | BUILD SUCCESS |
| MS Pedidos y Ventas            |               15 | BUILD SUCCESS |
| MS Logística de Envíos         |               12 | BUILD SUCCESS |
| MS Administración y Soporte    |                1 | BUILD SUCCESS |
| MS Reportes                    |                7 | BUILD SUCCESS |

---

## 18. Pruebas con Postman

Las pruebas manuales de endpoints REST se documentan en:

```text
docs/postman/evidencia-postman.md
```

Para probar el sistema con Postman:

```text
1. Iniciar MySQL.
2. Crear o verificar las bases de datos.
3. Ejecutar los microservicios necesarios.
4. Ejecutar el API Gateway.
5. Importar la colección Postman si está disponible.
6. Configurar variables de entorno.
7. Ejecutar endpoints por microservicio o mediante Gateway.
```

Variables sugeridas en Postman:

| Variable         | Valor sugerido          |
| ---------------- | ----------------------- |
| `gateway_url`    | `http://localhost:8081` |
| `usuarios_url`   | `http://localhost:8083` |
| `catalogo_url`   | `http://localhost:8084` |
| `inventario_url` | `http://localhost:8085` |
| `pedidos_url`    | `http://localhost:8086` |
| `logistica_url`  | `http://localhost:8087` |
| `admin_url`      | `http://localhost:8088` |
| `reportes_url`   | `http://localhost:8089` |

---

## 19. Rutas principales del API Gateway

| Ruta Gateway      | Microservicio destino          |
| ----------------- | ------------------------------ |
| `/api/auth`       | MS Usuarios e Identidad        |
| `/api/usuarios`   | MS Usuarios e Identidad        |
| `/api/productos`  | MS Catálogo                    |
| `/api/categorias` | MS Catálogo                    |
| `/api/inventario` | MS Inventario y Abastecimiento |
| `/api/stock`      | MS Inventario y Abastecimiento |
| `/api/pedidos`    | MS Pedidos y Ventas            |
| `/api/ventas`     | MS Pedidos y Ventas            |
| `/api/envios`     | MS Logística de Envíos         |
| `/api/rutas`      | MS Logística de Envíos         |
| `/api/admin`      | MS Administración y Soporte    |
| `/api/soporte`    | MS Administración y Soporte    |
| `/api/v1/reportes` | MS Reportes                   |
| `/api/v1/kpis`    | MS Reportes                    |

La documentación específica de rutas se encuentra en:

```text
docs/api-gateway-rutas.md
```

---

## 20. Endpoints principales por microservicio

### 20.1 MS Usuarios e Identidad

| Método | Ruta                 | Descripción              |
| ------ | -------------------- | ------------------------ |
| POST   | `/api/usuarios`      | Registrar usuario        |
| GET    | `/api/usuarios/{id}` | Consultar usuario por ID |
| POST   | `/api/auth/login`    | Iniciar sesión           |

### 20.2 MS Catálogo

| Método | Ruta                  | Descripción               |
| ------ | --------------------- | ------------------------- |
| POST   | `/api/productos`      | Crear producto            |
| GET    | `/api/productos`      | Listar productos          |
| GET    | `/api/productos/{id}` | Consultar producto por ID |
| POST   | `/api/categorias`     | Crear categoría           |
| GET    | `/api/categorias`     | Listar categorías         |

### 20.3 MS Inventario y Abastecimiento

| Método | Ruta                        | Descripción                  |
| ------ | --------------------------- | ---------------------------- |
| POST   | `/api/productos`            | Crear producto de inventario |
| GET    | `/api/productos`            | Listar productos             |
| GET    | `/api/productos/sku/{sku}`  | Consultar stock por SKU      |
| PATCH  | `/api/productos/{id}/stock` | Ajustar stock                |

### 20.4 MS Pedidos y Ventas

| Método | Ruta                                          | Descripción                      |
| ------ | --------------------------------------------- | -------------------------------- |
| POST   | `/api/pedidos`                                | Crear pedido                     |
| GET    | `/api/pedidos/{id}/estado`                    | Consultar estado de pedido       |
| GET    | `/api/pedidos/clientes/{idCliente}/historial` | Consultar historial de cliente   |
| POST   | `/api/ventas/presencial`                      | Registrar venta presencial       |
| POST   | `/api/ventas/{idVenta}/facturas`              | Generar factura                  |
| POST   | `/api/ventas/{idVenta}/devoluciones`          | Registrar devolución             |
| PATCH  | `/api/devoluciones/{id}/estado`               | Actualizar estado de devolución  |
| POST   | `/api/reclamaciones`                          | Registrar reclamación            |
| PATCH  | `/api/reclamaciones/{id}/estado`              | Actualizar estado de reclamación |

### 20.5 MS Logística de Envíos

| Método | Ruta                           | Descripción             |
| ------ | ------------------------------ | ----------------------- |
| POST   | `/api/proveedores`             | Crear proveedor         |
| POST   | `/api/envios`                  | Crear envío             |
| GET    | `/api/envios/{id}`             | Consultar envío         |
| PATCH  | `/api/envios/{id}/estado`      | Cambiar estado de envío |
| GET    | `/api/envios/{id}/seguimiento` | Obtener seguimiento     |
| PATCH  | `/api/envios/{id}/incidencia`  | Registrar incidencia    |
| POST   | `/api/rutas`                   | Crear ruta              |
| PATCH  | `/api/rutas/{id}/estado`       | Cambiar estado de ruta  |

### 20.6 MS Administración y Soporte

| Método | Ruta                               | Descripción                 |
| ------ | ---------------------------------- | --------------------------- |
| POST   | `/api/tiendas`                     | Crear tienda                |
| GET    | `/api/tiendas`                     | Listar tiendas              |
| GET    | `/api/tiendas/{id}`                | Consultar tienda            |
| POST   | `/api/soporte/tickets`             | Crear ticket                |
| PATCH  | `/api/soporte/tickets/{id}/estado` | Actualizar estado de ticket |

### 20.7 MS Reportes

| Método | Ruta                   | Descripción             |
| ------ | ---------------------- | ----------------------- |
| POST   | `/api/v1/reportes`        | Crear reporte           |
| GET    | `/api/v1/reportes/{id}`   | Consultar reporte       |
| POST   | `/api/v1/kpis`            | Registrar KPI           |
| GET    | `/api/v1/kpis/tipo/{tipo}` | Consultar KPIs por tipo |

---

## 21. Comunicación entre microservicios

La comunicación interna se realiza mediante servicios REST.

Flujos principales:

| Origen              | Destino                        | Propósito                              |
| ------------------- | ------------------------------ | -------------------------------------- |
| MS Pedidos y Ventas | MS Inventario y Abastecimiento | Consultar y reservar stock             |
| MS Pedidos y Ventas | MS Logística de Envíos         | Solicitar despacho                     |
| MS Reportes         | MS Pedidos y Ventas            | Obtener datos de ventas                |
| MS Reportes         | MS Inventario y Abastecimiento | Obtener datos de stock                 |
| MS Reportes         | MS Administración y Soporte    | Obtener datos de tiendas y rendimiento |

La documentación detallada está en:

```text
docs/integracion/comunicacion-rest-entre-servicios.md
```

---

## 22. HATEOAS

El proyecto incorpora HATEOAS en endpoints principales para entregar enlaces relacionados dentro de las respuestas REST.

Ejemplo conceptual:

```json
{
  "id": 1,
  "estado": "PREPARADO",
  "_links": {
    "self": {
      "href": "http://localhost:8087/api/envios/1"
    },
    "seguimiento": {
      "href": "http://localhost:8087/api/envios/1/seguimiento"
    }
  }
}
```

Documentación relacionada:

```text
docs/hateoas/documentacion-hateoas-base.md
```

---

## 23. Manejo de errores y códigos HTTP

El backend utiliza respuestas HTTP para representar el resultado de las operaciones.

| Código | Significado           | Uso                              |
| -----: | --------------------- | -------------------------------- |
|    200 | OK                    | Consulta o actualización exitosa |
|    201 | Created               | Recurso creado correctamente     |
|    204 | No Content            | Operación exitosa sin cuerpo     |
|    400 | Bad Request           | Datos inválidos                  |
|    404 | Not Found             | Recurso no encontrado            |
|    409 | Conflict              | Regla de negocio incumplida      |
|    500 | Internal Server Error | Error interno no controlado      |

---

## 24. Flujo Git utilizado

El proyecto utiliza un flujo basado en ramas.

| Rama        | Propósito                                |
| ----------- | ---------------------------------------- |
| `main`      | Rama estable final                       |
| `develop`   | Rama de integración                      |
| `feature/*` | Desarrollo por microservicio, HU o tarea |
| `hotfix/*`  | Correcciones urgentes                    |

Flujo general:

```text
feature/* -> Pull Request -> develop -> Pull Request final -> main
```

Documentación relacionada:

```text
docs/git-flow.md
```

---

## 25. Documentación técnica del proyecto

| Documento                         | Ruta                                                    |
| --------------------------------- | ------------------------------------------------------- |
| Arquitectura de microservicios    | `docs/arquitectura/arquitectura-microservicios.md`      |
| Bases de datos MySQL              | `docs/arquitectura/bases-datos-mysql.md`                |
| Comunicación REST entre servicios | `docs/integracion/comunicacion-rest-entre-servicios.md` |
| Evidencia de build y tests        | `docs/evidencias/evidencia-build-tests.md`              |
| Evidencias técnicas consolidadas  | `docs/evidencias-tecnicas/`                             |
| Evidencia Postman                 | `docs/postman/evidencia-postman.md`                     |
| API Gateway                       | `docs/api-gateway-rutas.md`                             |
| Git Flow                          | `docs/git-flow.md`                                      |
| HATEOAS                           | `docs/hateoas/documentacion-hateoas-base.md`            |
| Diagramas                         | `docs/diagramas/`                                       |

---

## 26. Preparación para entrega AVA

Para preparar la entrega, se recomienda verificar lo siguiente:

```text
1. Estar en la rama main.
2. Ejecutar git pull origin main.
3. Verificar git status limpio.
4. Ejecutar mvn clean test por microservicio.
5. Ejecutar mvn clean package por microservicio.
6. Revisar que las bases de datos MySQL estén documentadas.
7. Revisar evidencia Postman.
8. Revisar documentación de integración REST.
9. Revisar diagramas.
10. Comprimir el repositorio sin carpetas target.
```

No se deben incluir en el ZIP final:

```text
target/
.idea/
.vscode/
node_modules/
*.log
.env con credenciales reales
```

Sí se pueden incluir:

```text
README.md
docs/
src/
pom.xml
application.properties de cada microservicio
colecciones Postman si existen
```

---

## 27. Comandos útiles para limpieza antes de entrega

Eliminar carpetas `target` generadas por Maven:

```powershell
Get-ChildItem -Path . -Recurse -Directory -Filter target | Remove-Item -Recurse -Force
```

Verificar estado Git:

```powershell
git status
```

Ver últimos commits:

```powershell
git log --oneline -5
```

---

## 28. Comandos rápidos de validación final

Ejecutar tests:

```powershell
mvn -f ms-usuarios-identidad/pom.xml clean test
mvn -f ms-catalogo/pom.xml clean test
mvn -f ms-inventario-abastecimiento/pom.xml clean test
mvn -f ms-pedidos-ventas/pom.xml clean test
mvn -f ms-logistica-envios/pom.xml clean test
mvn -f ms-administracion-soporte/pom.xml clean test
mvn -f ms-reportes/pom.xml clean test
```

Ejecutar package:

```powershell
mvn -f ms-usuarios-identidad/pom.xml clean package
mvn -f ms-catalogo/pom.xml clean package
mvn -f ms-inventario-abastecimiento/pom.xml clean package
mvn -f ms-pedidos-ventas/pom.xml clean package
mvn -f ms-logistica-envios/pom.xml clean package
mvn -f ms-administracion-soporte/pom.xml clean package
mvn -f ms-reportes/pom.xml clean package
```

---

## 29. Estado actual de validación

La rama `develop` fue validada mediante compilación, empaquetado y evidencia documentada de pruebas por microservicio.

Resultado general:

```text
7 microservicios con tests ejecutados correctamente.
7 microservicios empaquetados correctamente.
0 errores bloqueantes.
BUILD SUCCESS global por microservicio.
```

La evidencia detallada está registrada en:

```text
docs/evidencias/evidencia-build-tests.md
```

---

## 30. Conclusión

EcoMarket SPA implementa una solución backend basada en microservicios independientes con API Gateway, persistencia MySQL por servicio, comunicación REST, estructura Controller-Service-Repository, validaciones, manejo de excepciones, logs y pruebas automatizadas.

El proyecto queda preparado para la entrega EP2 y para la defensa técnica, evidenciando arquitectura distribuida, persistencia de datos, documentación de endpoints, pruebas de integración y validación mediante Maven, JUnit y Postman.
