# Comunicación REST entre Microservicios — EcoMarket SPA

## Proyecto

| Campo | Detalle |
| --- | --- |
| Proyecto | EcoMarket SPA |
| Asignatura | Desarrollo Full Stack I — DSY1103 |
| Sección | 003D |
| Entrega | EP2, Entrega de Encargo grupal Parte 1 |
| Arquitectura | Microservicios independientes con API Gateway |
| Estilo de comunicación | REST API |

---

## 1. Objetivo

Este documento describe la estrategia de comunicación REST entre los microservicios del backend de **EcoMarket SPA**.

La solución está basada en una arquitectura distribuida donde cada microservicio mantiene su propia responsabilidad de negocio, su propia base de datos MySQL y expone endpoints REST para permitir la integración controlada con otros componentes del sistema.

---

## 2. Enfoque de comunicación

EcoMarket SPA utiliza comunicación basada en servicios REST para permitir el intercambio de información entre microservicios.

Cada microservicio expone endpoints HTTP utilizando controladores Spring Boot, los cuales reciben solicitudes, validan datos de entrada, delegan la lógica de negocio a los servicios correspondientes y responden utilizando estructuras JSON.

El flujo general de comunicación es:

```text
Cliente o Frontend
        |
        v
API Gateway
        |
        v
Microservicio correspondiente
        |
        v
Base de datos propia del microservicio
```

El API Gateway actúa como punto único de entrada y enruta las solicitudes hacia el microservicio correspondiente.

---

## 3. Principios aplicados

La comunicación entre microservicios se basa en los siguientes principios:

- Cada microservicio expone su propia API REST.
- Cada microservicio mantiene su propia base de datos.
- Ningún microservicio debe acceder directamente a la base de datos de otro.
- La integración se realiza mediante endpoints HTTP.
- Las respuestas se estructuran en formato JSON.
- Se utilizan códigos HTTP adecuados para representar el resultado de cada operación.
- Se mantiene separación entre Controller, Service y Repository.
- Se incorporan validaciones, manejo de excepciones y logs para mejorar la trazabilidad.

---

## 4. API Gateway

El API Gateway centraliza el acceso a los microservicios y permite definir rutas públicas organizadas.

Rutas principales del sistema:

| Ruta Gateway  | Microservicio destino          | Propósito                                |
| ------------- | ------------------------------ | ---------------------------------------- |
| `/auth`       | MS Usuarios e Identidad        | Autenticación y acceso                   |
| `/usuarios`   | MS Usuarios e Identidad        | Gestión de usuarios                      |
| `/productos`  | MS Catálogo                    | Gestión y consulta de productos          |
| `/categorias` | MS Catálogo                    | Gestión de categorías                    |
| `/inventario` | MS Inventario y Abastecimiento | Consulta y control de inventario         |
| `/stock`      | MS Inventario y Abastecimiento | Stock disponible, reservas y movimientos |
| `/pedidos`    | MS Pedidos y Ventas            | Gestión de pedidos                       |
| `/ventas`     | MS Pedidos y Ventas            | Ventas presenciales y facturación        |
| `/envios`     | MS Logística de Envíos         | Gestión de envíos                        |
| `/rutas`      | MS Logística de Envíos         | Gestión de rutas de entrega              |
| `/admin`      | MS Administración y Soporte    | Administración interna                   |
| `/soporte`    | MS Administración y Soporte    | Tickets y atención de soporte            |
| `/reportes`   | MS Reportes                    | Reportes ejecutivos                      |
| `/kpi`        | MS Reportes                    | Indicadores clave de negocio             |

---

## 5. Comunicación interna principal

Los flujos internos más relevantes son:

| Microservicio origen   | Microservicio destino          | Propósito                                                   |
| ---------------------- | ------------------------------ | ----------------------------------------------------------- |
| MS Pedidos y Ventas    | MS Inventario y Abastecimiento | Consultar stock antes de confirmar un pedido                |
| MS Pedidos y Ventas    | MS Inventario y Abastecimiento | Reservar o descontar stock asociado a una venta o pedido    |
| MS Pedidos y Ventas    | MS Logística de Envíos         | Solicitar despacho posterior a la confirmación de un pedido |
| MS Logística de Envíos | MS Inventario y Abastecimiento | Gestionar recepción o reabastecimiento de mercadería        |
| MS Reportes            | MS Pedidos y Ventas            | Obtener información de ventas, pedidos y facturación        |
| MS Reportes            | MS Inventario y Abastecimiento | Obtener información de stock, alertas y rotación            |
| MS Reportes            | MS Administración y Soporte    | Obtener información de tiendas, métricas y rendimiento      |

---

## 6. Flujo: Pedido consulta stock en Inventario

Antes de confirmar un pedido, el MS Pedidos y Ventas debe verificar disponibilidad de productos.

Flujo esperado:

```text
Cliente confirma carrito
        |
        v
MS Pedidos y Ventas recibe solicitud de creación de Pedido
        |
        v
MS Pedidos y Ventas consulta disponibilidad en MS Inventario y Abastecimiento
        |
        v
MS Inventario responde stock disponible
        |
        v
MS Pedidos y Ventas confirma o rechaza el Pedido
```

Ejemplo de consulta REST esperada:

```http
GET /inventario/productos/{idProducto}/tiendas/{idTienda}/stock
```

Respuesta esperada:

```json
{
  "idProducto": 10,
  "idTienda": 1,
  "stockDisponible": 25,
  "stockReservado": 3,
  "stockMinimo": 5
}
```

---

## 7. Flujo: Pedido solicita despacho a Logística

Cuando un pedido es confirmado, se puede generar una solicitud de envío al MS Logística de Envíos.

Flujo esperado:

```text
Pedido confirmado
        |
        v
MS Pedidos y Ventas solicita creación de Envio
        |
        v
MS Logística de Envíos registra Envio en estado PREPARADO
        |
        v
MS Logística de Envíos registra SeguimientoEnvio inicial
```

Ejemplo de solicitud REST esperada:

```http
POST /envios
Content-Type: application/json
```

Body de ejemplo:

```json
{
  "idPedido": 1001,
  "origen": "Tienda Lastarria",
  "destino": "Cliente Web - Santiago",
  "fechaEstimadaEntrega": "2026-05-26T18:00:00"
}
```

Respuesta esperada:

```json
{
  "id": 1,
  "idPedido": 1001,
  "origen": "Tienda Lastarria",
  "destino": "Cliente Web - Santiago",
  "estado": "PREPARADO",
  "fechaEstimadaEntrega": "2026-05-26T18:00:00"
}
```

---

## 8. Flujo: Reportes consume datos de otros microservicios

El MS Reportes consolida información para entregar estadísticas, KPIs y reportes ejecutivos.

Fuentes principales:

| Fuente                         | Datos utilizados                                 |
| ------------------------------ | ------------------------------------------------ |
| MS Pedidos y Ventas            | Ventas, pedidos, pagos, facturas y devoluciones  |
| MS Inventario y Abastecimiento | Stock, movimientos, alertas y productos críticos |
| MS Administración y Soporte    | Tiendas, métricas y rendimiento operativo        |

Ejemplo de flujo:

```text
Gerente solicita reporte de ventas
        |
        v
MS Reportes recibe solicitud
        |
        v
MS Reportes obtiene datos desde MS Pedidos y Ventas
        |
        v
MS Reportes genera ReporteVentas
        |
        v
MS Reportes responde resultado o exportación
```

Ejemplo de endpoint:

```http
GET /reportes/ventas?idTienda=1&fechaInicio=2026-05-01&fechaFin=2026-05-31
```

---

## 9. Formato de intercambio de datos

El formato principal de comunicación entre servicios es JSON.

Ejemplo de respuesta estándar:

```json
{
  "id": 1,
  "estado": "CONFIRMADO",
  "fechaCreacion": "2026-05-24T12:30:00",
  "mensaje": "Operación realizada correctamente"
}
```

En operaciones con error, se espera una respuesta controlada mediante manejo de excepciones:

```json
{
  "timestamp": "2026-05-24T12:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Recurso no encontrado",
  "path": "/api/envios/999"
}
```

---

## 10. Códigos HTTP utilizados

|               Código HTTP | Uso                                              |
| ------------------------: | ------------------------------------------------ |
|                    200 OK | Consulta o actualización realizada correctamente |
|               201 Created | Recurso creado correctamente                     |
|            204 No Content | Operación exitosa sin contenido de respuesta     |
|           400 Bad Request | Solicitud inválida o datos no permitidos         |
|             404 Not Found | Recurso no encontrado                            |
|              409 Conflict | Regla de negocio incumplida                      |
| 500 Internal Server Error | Error interno no controlado                      |

---

## 11. Uso de HATEOAS

Los endpoints principales incorporan HATEOAS cuando corresponde, permitiendo entregar enlaces relacionados junto con los recursos.

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

El uso de HATEOAS permite mejorar la navegabilidad de la API y facilitar la comprensión de las acciones disponibles sobre cada recurso.

---

## 12. Validaciones en comunicación REST

Los microservicios utilizan validaciones para controlar los datos recibidos antes de ejecutar reglas de negocio.

Ejemplos de validaciones aplicadas:

- Campos obligatorios mediante `@NotNull` o `@NotBlank`.
- Valores positivos mediante `@Positive`.
- Fechas válidas mediante `@FutureOrPresent`.
- Formato de correo mediante `@Email`.
- Estados permitidos mediante enums o validaciones de servicio.

Estas validaciones permiten evitar inconsistencias y responder con errores claros cuando la solicitud no cumple con las reglas definidas.

---

## 13. Manejo de excepciones

La comunicación REST incorpora manejo de excepciones para responder de forma controlada ante errores.

Ejemplos de casos controlados:

| Caso                        | Respuesta esperada |
| --------------------------- | ------------------ |
| Recurso inexistente         | 404 Not Found      |
| Estado inválido             | 400 Bad Request    |
| Regla de negocio incumplida | 409 Conflict       |
| Datos obligatorios ausentes | 400 Bad Request    |

El manejo de excepciones evita respuestas ambiguas y mejora la confiabilidad del backend.

---

## 14. Logs estructurados

Los servicios registran eventos importantes mediante logs.

Ejemplos de eventos registrados:

- Creación de pedidos.
- Registro de ventas.
- Creación de envíos.
- Cambio de estado de rutas.
- Registro de devoluciones.
- Registro de reclamaciones.
- Intentos de operación inválida.
- Consultas relevantes para reportes.

Los logs permiten trazabilidad durante pruebas, validación técnica y diagnóstico de errores.

---

## 15. Independencia de bases de datos

La comunicación REST no reemplaza el principio de independencia de datos.

Cada microservicio conserva su propia base MySQL:

| Microservicio                  | Base de datos             |
| ------------------------------ | ------------------------- |
| MS Usuarios e Identidad        | `bd_usuarios`   |
| MS Catálogo                    | `bd_catalogo`   |
| MS Inventario y Abastecimiento | `bd_inventario` |
| MS Pedidos y Ventas            | `bd_ventas`     |
| MS Logística de Envíos         | `bd_logistica`  |
| MS Administración y Soporte    | `bd_admin`      |
| MS Reportes                    | `bd_reportes`   |

La transferencia de datos entre dominios debe realizarse mediante contratos REST y no mediante acceso directo a tablas externas.

---

## 16. Beneficios de la integración REST

La integración REST entre microservicios permite:

- Separación clara de responsabilidades.
- Bajo acoplamiento entre dominios.
- Escalabilidad independiente por microservicio.
- Mayor mantenibilidad del backend.
- Claridad en los contratos de comunicación.
- Mejor trazabilidad mediante endpoints y logs.
- Preparación para integración con frontend web o caja POS.
- Alineación con arquitectura distribuida.

---

## 17. Conclusión

EcoMarket SPA implementa comunicación REST entre microservicios como mecanismo principal de integración backend.

El API Gateway centraliza el acceso, los microservicios mantienen independencia de dominio y base de datos, y los flujos entre Pedidos, Inventario, Logística, Administración y Reportes permiten representar una solución distribuida coherente para el caso de negocio.

Esta estrategia cumple con los requerimientos de la EP2, al evidenciar arquitectura distribuida, persistencia independiente, comunicación entre servicios, validaciones, manejo de excepciones, logs y estructura backend basada en Controller, Service y Repository.

