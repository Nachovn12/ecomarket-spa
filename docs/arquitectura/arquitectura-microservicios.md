# Arquitectura de Microservicios — EcoMarket SPA

## Proyecto

| Campo | Detalle |
| --- | --- |
| Proyecto | EcoMarket SPA |
| Asignatura | Desarrollo Full Stack I — DSY1103 |
| Sección | 003D |
| Entrega | EP2, Entrega de Encargo grupal Parte 1 |
| Arquitectura | Microservicios independientes con API Gateway |
| Backend | Java, Spring Boot, Maven, JPA/Hibernate, MySQL, HATEOAS |

---

## 1. Objetivo

Este documento describe la arquitectura backend definida para el proyecto **EcoMarket SPA**, correspondiente a la evaluación EP2 de la asignatura Desarrollo Full Stack I.

La solución propone una arquitectura basada en microservicios independientes, cada uno con responsabilidad de negocio específica, base de datos propia y exposición de servicios REST. El acceso externo se centraliza mediante un API Gateway, permitiendo ordenar las rutas de entrada y desacoplar al cliente de la ubicación interna de cada servicio.

---

## 2. Contexto del caso

EcoMarket SPA es una empresa chilena dedicada a la venta de productos ecológicos y sostenibles, con tiendas físicas en Santiago, Valdivia y Antofagasta, además de operación mediante tienda online.

El sistema monolítico original presenta problemas de rendimiento, baja disponibilidad, alto acoplamiento y riesgo de punto único de fallo. Para responder a estos problemas, se propone una migración hacia una arquitectura distribuida basada en microservicios.

---

## 3. Problema identificado

El sistema monolítico original presenta las siguientes dificultades:

- Alto acoplamiento entre módulos.
- Baja disponibilidad ante fallos.
- Dificultad para escalar componentes específicos.
- Riesgo de punto único de fallo.
- Complejidad para mantener y evolucionar funcionalidades.
- Dificultad para integrar nuevos canales como tienda online o caja POS.
- Bajo nivel de separación entre dominios de negocio.

---

## 4. Solución propuesta

La solución consiste en dividir el backend de EcoMarket SPA en microservicios independientes, cada uno orientado a un dominio funcional del negocio.

Cada microservicio implementa:

- Controladores REST.
- Servicios con lógica de negocio.
- Repositorios JPA.
- Entidades persistentes.
- DTOs para transferencia de datos.
- Validaciones.
- Manejo de excepciones.
- Logs de operaciones relevantes.
- Base de datos MySQL propia.

El API Gateway actúa como punto único de entrada y enruta las solicitudes hacia el microservicio correspondiente.

---

## 5. Vista general de arquitectura

```text
Frontend Web / Cliente REST / Postman
              |
              v
        API Gateway
              |
              v
+-------------+----------------+----------------+----------------+
|             |                |                |                |
v             v                v                v                v
MS Usuarios   MS Catálogo      MS Inventario    MS Pedidos       MS Logística
e Identidad                                    y Ventas          de Envíos
|             |                |                |                |
v             v                v                v                v
BD Usuarios   BD Catálogo      BD Inventario    BD Ventas        BD Logística

              |
              v
+-----------------------------+-----------------------------+
|                             |                             |
v                             v                             v
MS Administración             MS Reportes                   Otros clientes
y Soporte
|                             |
v                             v
BD Admin                      BD Reportes
```

---

## 6. Microservicios definidos

| Microservicio                  | Responsabilidad principal                                       | Base de datos             |
| ------------------------------ | --------------------------------------------------------------- | ------------------------- |
| MS Usuarios e Identidad        | Login, registro, roles, permisos y usuarios internos            | `bd_usuarios`   |
| MS Catálogo                    | Productos, categorías, reseñas, búsqueda y atributos ecológicos | `bd_catalogo`   |
| MS Inventario y Abastecimiento | Stock, reservas, movimientos y alertas de inventario            | `bd_inventario` |
| MS Pedidos y Ventas            | Carrito, pedidos, ventas, pagos, facturas y devoluciones        | `bd_ventas`     |
| MS Logística de Envíos         | Envíos, rutas, proveedores y seguimiento                        | `bd_logistica`  |
| MS Administración y Soporte    | Tiendas, tickets, alertas, métricas y respaldos                 | `bd_admin`      |
| MS Reportes                    | Reportes, KPIs, exportaciones y auditoría                       | `bd_reportes`   |
| API Gateway                    | Enrutamiento REST y punto único de entrada                      | No aplica                 |

---

## 7. API Gateway

El API Gateway centraliza el acceso a los microservicios y permite que clientes externos, como frontend web, Postman o caja POS, consuman una única URL base.

Sus responsabilidades principales son:

- Recibir solicitudes externas.
- Redirigir cada ruta hacia el microservicio correspondiente.
- Ocultar la ubicación interna de los servicios.
- Simplificar el consumo de APIs desde el cliente.
- Facilitar la organización de rutas REST.
- Reducir el acoplamiento entre frontend y microservicios.

Rutas principales:

| Ruta Gateway  | Microservicio destino          |
| ------------- | ------------------------------ |
| `/auth`       | MS Usuarios e Identidad        |
| `/usuarios`   | MS Usuarios e Identidad        |
| `/productos`  | MS Catálogo                    |
| `/categorias` | MS Catálogo                    |
| `/inventario` | MS Inventario y Abastecimiento |
| `/stock`      | MS Inventario y Abastecimiento |
| `/pedidos`    | MS Pedidos y Ventas            |
| `/ventas`     | MS Pedidos y Ventas            |
| `/envios`     | MS Logística de Envíos         |
| `/rutas`      | MS Logística de Envíos         |
| `/admin`      | MS Administración y Soporte    |
| `/soporte`    | MS Administración y Soporte    |
| `/reportes`   | MS Reportes                    |
| `/kpi`        | MS Reportes                    |

---

## 8. Patrón Controller-Service-Repository

Cada microservicio utiliza una estructura basada en el patrón Controller-Service-Repository.

### Controller

La capa Controller recibe las solicitudes HTTP, valida los datos de entrada y delega la operación hacia la capa Service.

Responsabilidades:

- Exponer endpoints REST.
- Recibir parámetros, path variables y request bodies.
- Aplicar validaciones mediante `@Valid`.
- Retornar respuestas HTTP mediante `ResponseEntity`.
- Incorporar enlaces HATEOAS cuando corresponde.

### Service

La capa Service contiene la lógica de negocio del microservicio.

Responsabilidades:

- Aplicar reglas de negocio.
- Validar condiciones internas.
- Coordinar operaciones entre repositorios.
- Lanzar excepciones controladas.
- Registrar logs de operaciones relevantes.

### Repository

La capa Repository se encarga del acceso a datos mediante Spring Data JPA.

Responsabilidades:

- Extender `JpaRepository`.
- Ejecutar operaciones CRUD.
- Definir consultas derivadas cuando corresponde.
- Aislar la persistencia del resto de la aplicación.

---

## 9. Persistencia de datos

EcoMarket SPA utiliza el patrón **Database per Service**.

Cada microservicio posee su propia base de datos MySQL, evitando el acceso directo a tablas de otros dominios.

Esta decisión permite:

- Reducir el acoplamiento.
- Separar responsabilidades por dominio.
- Facilitar mantenimiento y evolución.
- Evitar dependencias directas entre esquemas.
- Preparar el sistema para despliegue independiente.

La comunicación entre dominios debe realizarse mediante servicios REST, no mediante consultas directas a bases de datos externas.

---

## 10. Comunicación entre microservicios

Los microservicios se comunican mediante servicios REST cuando el flujo de negocio lo requiere.

Flujos principales:

| Origen                 | Destino                        | Propósito                                               |
| ---------------------- | ------------------------------ | ------------------------------------------------------- |
| MS Pedidos y Ventas    | MS Inventario y Abastecimiento | Consultar stock disponible antes de confirmar un pedido |
| MS Pedidos y Ventas    | MS Inventario y Abastecimiento | Reservar o descontar stock                              |
| MS Pedidos y Ventas    | MS Logística de Envíos         | Solicitar despacho de un pedido confirmado              |
| MS Logística de Envíos | MS Inventario y Abastecimiento | Gestionar recepción o reabastecimiento                  |
| MS Reportes            | MS Pedidos y Ventas            | Obtener información de ventas y pedidos                 |
| MS Reportes            | MS Inventario y Abastecimiento | Obtener información de stock y alertas                  |
| MS Reportes            | MS Administración y Soporte    | Obtener información de tiendas y métricas               |

La integración se basa en contratos REST, intercambio de JSON y códigos HTTP adecuados.

---

## 11. Validaciones y reglas de negocio

Los microservicios incorporan validaciones y reglas de negocio para asegurar consistencia en las operaciones.

Ejemplos:

- Campos obligatorios mediante `@NotNull` y `@NotBlank`.
- Valores positivos mediante `@Positive`.
- Fechas válidas mediante `@FutureOrPresent`.
- Correos válidos mediante `@Email`.
- Estados controlados mediante enums.
- Reglas de cambio de estado en pedidos, envíos, rutas, devoluciones y tickets.
- Validaciones de stock antes de confirmar pedidos.
- Validaciones de existencia antes de actualizar recursos.

Estas validaciones ayudan a evitar datos inconsistentes y permiten responder con errores controlados.

---

## 12. Manejo de excepciones

El backend incorpora manejo de excepciones para controlar errores de forma consistente.

Casos esperados:

| Caso                        |     Código HTTP |
| --------------------------- | --------------: |
| Datos inválidos             | 400 Bad Request |
| Recurso no encontrado       |   404 Not Found |
| Regla de negocio incumplida |    409 Conflict |
| Creación exitosa            |     201 Created |
| Consulta exitosa            |          200 OK |
| Actualización exitosa       |          200 OK |

El manejo de excepciones permite mejorar la confiabilidad del sistema y entregar respuestas claras durante pruebas manuales, pruebas automatizadas y validación técnica.

---

## 13. Uso de HATEOAS

El proyecto incorpora HATEOAS en endpoints principales, permitiendo que las respuestas REST incluyan enlaces relacionados al recurso consultado.

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

El uso de HATEOAS permite mejorar la navegabilidad de la API y facilita la comprensión de operaciones relacionadas.

---

## 14. Logs estructurados

Los microservicios registran eventos relevantes mediante logs.

Ejemplos de eventos registrados:

- Creación de usuarios.
- Registro de productos.
- Movimientos de stock.
- Creación de pedidos.
- Registro de ventas.
- Generación de facturas.
- Registro de devoluciones.
- Creación de envíos.
- Cambios de estado de rutas.
- Registro de tickets de soporte.
- Generación de reportes y KPIs.

Los logs aportan trazabilidad, facilitan la depuración y respaldan la evidencia técnica del proyecto.

---

## 15. Seguridad básica

El proyecto considera seguridad básica a nivel de arquitectura y diseño backend.

Aspectos contemplados:

- Separación de responsabilidades por microservicio.
- Gestión de usuarios, roles y permisos en MS Usuarios e Identidad.
- Uso de DTOs para controlar datos expuestos.
- Validación de entradas.
- Manejo controlado de errores.
- Evitar exposición innecesaria de datos internos.
- Preparación para autenticación y autorización centralizada mediante el API Gateway.

---

## 16. Tecnologías utilizadas

| Categoría                  | Tecnología           |
| -------------------------- | -------------------- |
| Lenguaje                   | Java                 |
| Framework backend          | Spring Boot          |
| Gestión de dependencias    | Maven                |
| Persistencia               | JPA/Hibernate        |
| Base de datos principal    | MySQL                |
| Pruebas automatizadas      | JUnit                |
| Base de datos para pruebas | H2                   |
| API REST                   | Spring Web           |
| HATEOAS                    | Spring HATEOAS       |
| Control de versiones       | Git y GitHub         |
| Pruebas manuales           | Postman              |
| Gateway                    | Spring Cloud Gateway |

---

## 17. Beneficios de la arquitectura

La arquitectura propuesta permite:

- Separar dominios de negocio.
- Reducir el acoplamiento.
- Mejorar mantenibilidad.
- Facilitar pruebas por microservicio.
- Permitir evolución independiente.
- Mejorar organización del código.
- Disminuir el impacto de fallos parciales.
- Preparar la solución para escalabilidad futura.
- Facilitar integración con frontend web y caja POS.

---

## 18. Relación con la evaluación EP2

Esta arquitectura permite evidenciar los aspectos solicitados en la EP2:

- Proyecto backend compuesto por microservicios independientes.
- Persistencia de datos mediante repositorios.
- Base de datos relacional por microservicio.
- Comunicación entre servicios REST.
- Aplicación del patrón Controller-Service-Repository.
- Uso de entidades JPA y relaciones cuando corresponde.
- Validaciones y reglas de negocio.
- Manejo de excepciones.
- Logs estructurados.
- Preparación para pruebas con Postman y validación técnica.

---

## 19. Conclusión

La arquitectura backend de EcoMarket SPA representa una solución distribuida orientada a microservicios, con separación clara de responsabilidades, persistencia independiente por dominio y comunicación REST entre componentes.

El uso de API Gateway, Spring Boot, JPA/Hibernate, MySQL, validaciones, manejo de excepciones, logs y HATEOAS permite cumplir con los requerimientos técnicos de la evaluación EP2 y entregar una base sólida de evidencia técnica del proyecto.

