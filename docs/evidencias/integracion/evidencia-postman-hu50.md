# EcoMarket SPA - Evidencia Postman HU-50

## HU-50 — TT-06 Validar comunicación REST entre microservicios

## Objetivo

Registrar evidencia de validación REST entre microservicios de EcoMarket SPA mediante Postman y API Gateway.

Esta evidencia respalda la HU-50, cuyo propósito es demostrar que las comunicaciones principales entre microservicios están identificadas, documentadas y preparadas para ser validadas técnicamente.

---

## Contexto de la validación

EcoMarket SPA utiliza una arquitectura basada en microservicios, donde cada servicio posee una responsabilidad específica y una base de datos PostgreSQL propia.

La comunicación externa se centraliza mediante el **API Gateway**, mientras que la comunicación entre dominios se representa mediante endpoints REST documentados y validados con Postman.

---

## Ambiente de prueba

| Elemento | Valor |
|---|---|
| API Gateway | `http://localhost:8080` |
| MS Usuarios e Identidad | `http://localhost:8081` |
| MS Catálogo | `http://localhost:8082` |
| MS Inventario y Abastecimiento | `http://localhost:8083` |
| MS Pedidos y Ventas | `http://localhost:8084` |
| MS Logística de Envíos | `http://localhost:8085` |
| MS Administración y Soporte | `http://localhost:8086` |
| MS Reportes | `http://localhost:8087` |

---

## Colección Postman utilizada

Para esta validación se considera la colección principal del proyecto:

```txt
postman/EcoMarket-SPA.postman_collection.json
```

Y el ambiente local:

```txt
postman/EcoMarket-SPA-local.postman_environment.json
```

---

## Variables principales del ambiente

| Variable | Valor |
|---|---|
| `base_url` | `http://localhost:8080` |
| `token` | Token JWT o token de sesión si corresponde |
| `api_gateway_port` | `8080` |
| `ms_usuarios_port` | `8081` |
| `ms_catalogo_port` | `8082` |
| `ms_inventario_port` | `8083` |
| `ms_pedidos_port` | `8084` |
| `ms_logistica_port` | `8085` |
| `ms_admin_port` | `8086` |
| `ms_reportes_port` | `8087` |

---

## Validaciones principales

| N° | Comunicación | Método | Endpoint | Resultado esperado | Estado |
|---|---|---|---|---|---|
| 1 | API Gateway Health | GET | `/actuator/health` | `200 OK` | Pendiente / Validado |
| 2 | Pedidos → Inventario | GET | `/stock?idProducto=1&idTienda=1` | `200 OK` o respuesta documentada | Pendiente / Validado |
| 3 | Pedidos → Logística | POST | `/envios` | `201 Created` o respuesta documentada | Pendiente / Validado |
| 4 | Reportes → Ventas | GET | `/ventas` | `200 OK` o respuesta documentada | Pendiente / Validado |
| 5 | Reportes → Pedidos | GET | `/pedidos` | `200 OK` o respuesta documentada | Pendiente / Validado |
| 6 | Reportes → Inventario | GET | `/inventario` | `200 OK` o respuesta documentada | Pendiente / Validado |
| 7 | Reportes → Administración | GET | `/admin/tiendas` | `200 OK` o respuesta documentada | Pendiente / Validado |
| 8 | Reportes → Soporte | GET | `/soporte/tickets` | `200 OK` o respuesta documentada | Pendiente / Validado |
| 9 | Reportes → KPI | GET | `/kpi` | `200 OK` o respuesta documentada | Pendiente / Validado |

---

# 1. Evidencia — Health API Gateway

## Objetivo

Confirmar que el API Gateway se encuentra activo y disponible para recibir solicitudes REST.

## Request

```http
GET http://localhost:8080/actuator/health
```

## Response esperado

```json
{
  "status": "UP"
}
```

## Código HTTP esperado

```txt
200 OK
```

## Resultado

```txt
Pendiente de captura / Validado en Postman
```

## Observación

Este endpoint permite confirmar que el API Gateway está levantado antes de probar rutas hacia microservicios.

---

# 2. Evidencia — Consulta de stock

## Comunicación validada

```txt
MS Pedidos y Ventas → MS Inventario y Abastecimiento
```

## Objetivo

Validar que el flujo de pedidos pueda consultar disponibilidad de stock antes de confirmar una compra.

## Request

```http
GET http://localhost:8080/stock?idProducto=1&idTienda=1
```

## Response esperado

```json
{
  "idProducto": 1,
  "idTienda": 1,
  "stockDisponible": 50,
  "stockReservado": 0,
  "stockMinimo": 10
}
```

## Códigos HTTP esperados

| Código | Significado |
|---|---|
| 200 OK | Stock consultado correctamente |
| 404 Not Found | No existe inventario para el producto o tienda |
| 500 Internal Server Error | Error interno del microservicio |

## Resultado

```txt
Pendiente de captura / Validado en Postman
```

## Observación

Esta comunicación es clave porque el MS Pedidos y Ventas no debe administrar stock directamente.  
La disponibilidad se consulta al MS Inventario y Abastecimiento.

---

# 3. Evidencia — Solicitud de envío

## Comunicación validada

```txt
MS Pedidos y Ventas → MS Logística de Envíos
```

## Objetivo

Validar que un pedido confirmado pueda generar una solicitud de despacho hacia el MS Logística de Envíos.

## Request

```http
POST http://localhost:8080/envios
Content-Type: application/json
```

## Body

```json
{
  "idPedido": 1,
  "direccionDestino": "Barrio Lastarria, Santiago",
  "idCliente": 1
}
```

## Response esperado

```json
{
  "idEnvio": 1,
  "idPedido": 1,
  "estado": "PREPARADO",
  "direccionDestino": "Barrio Lastarria, Santiago"
}
```

## Códigos HTTP esperados

| Código | Significado |
|---|---|
| 201 Created | Envío creado correctamente |
| 400 Bad Request | Datos inválidos |
| 500 Internal Server Error | Error interno del microservicio |

## Resultado

```txt
Pendiente de captura / Validado en Postman
```

## Observación

Esta comunicación permite conectar el flujo comercial con el flujo logístico del sistema.

---

# 4. Evidencia — Consulta de ventas

## Comunicación validada

```txt
MS Reportes → MS Pedidos y Ventas
```

## Objetivo

Validar que el MS Reportes pueda consumir datos comerciales generados por el MS Pedidos y Ventas.

## Request

```http
GET http://localhost:8080/ventas
```

## Response esperado

```json
[
  {
    "idVenta": 1,
    "idPedido": 1,
    "total": 24990,
    "metodoPago": "TARJETA",
    "fechaVenta": "2026-05-14T09:30:00"
  }
]
```

## Códigos HTTP esperados

| Código | Significado |
|---|---|
| 200 OK | Datos de ventas obtenidos correctamente |
| 204 No Content | No existen ventas disponibles |
| 500 Internal Server Error | Error interno del microservicio |

## Resultado

```txt
Pendiente de captura / Validado en Postman
```

## Observación

Esta comunicación permite construir reportes de ventas, KPIs comerciales e indicadores ejecutivos.

---

# 5. Evidencia — Consulta de pedidos

## Comunicación validada

```txt
MS Reportes → MS Pedidos y Ventas
```

## Objetivo

Validar que el MS Reportes pueda consultar información de pedidos para generar reportes de estado, volumen y trazabilidad comercial.

## Request

```http
GET http://localhost:8080/pedidos
```

## Response esperado

```json
[
  {
    "idPedido": 1,
    "idCliente": 1,
    "estado": "CONFIRMADO",
    "total": 24990,
    "fechaCreacion": "2026-05-14T09:00:00"
  }
]
```

## Códigos HTTP esperados

| Código | Significado |
|---|---|
| 200 OK | Pedidos obtenidos correctamente |
| 204 No Content | No existen pedidos disponibles |
| 500 Internal Server Error | Error interno del microservicio |

## Resultado

```txt
Pendiente de captura / Validado en Postman
```

---

# 6. Evidencia — Consulta de inventario

## Comunicación validada

```txt
MS Reportes → MS Inventario y Abastecimiento
```

## Objetivo

Validar que el MS Reportes pueda consumir información de inventario para reportes de stock bajo, rotación y disponibilidad.

## Request

```http
GET http://localhost:8080/inventario
```

## Response esperado

```json
[
  {
    "idProducto": 1,
    "idTienda": 1,
    "stockDisponible": 8,
    "stockReservado": 2,
    "stockMinimo": 10,
    "estado": "STOCK_BAJO"
  }
]
```

## Códigos HTTP esperados

| Código | Significado |
|---|---|
| 200 OK | Inventario obtenido correctamente |
| 204 No Content | No existen registros disponibles |
| 500 Internal Server Error | Error interno del microservicio |

## Resultado

```txt
Pendiente de captura / Validado en Postman
```

---

# 7. Evidencia — Consulta de tiendas

## Comunicación validada

```txt
MS Reportes → MS Administración y Soporte
```

## Objetivo

Validar que el MS Reportes pueda consultar tiendas para generar reportes de rendimiento por sucursal.

## Request

```http
GET http://localhost:8080/admin/tiendas
```

## Response esperado

```json
[
  {
    "idTienda": 1,
    "nombre": "EcoMarket Lastarria",
    "ciudad": "Santiago",
    "horarioApertura": "09:00",
    "estado": "ACTIVA"
  }
]
```

## Códigos HTTP esperados

| Código | Significado |
|---|---|
| 200 OK | Tiendas obtenidas correctamente |
| 204 No Content | No existen tiendas disponibles |
| 500 Internal Server Error | Error interno del microservicio |

## Resultado

```txt
Pendiente de captura / Validado en Postman
```

---

# 8. Evidencia — Consulta de tickets de soporte

## Comunicación validada

```txt
MS Reportes → MS Administración y Soporte
```

## Objetivo

Validar que el MS Reportes pueda consumir datos de soporte para generar métricas de atención y seguimiento.

## Request

```http
GET http://localhost:8080/soporte/tickets
```

## Response esperado

```json
[
  {
    "idTicket": 1,
    "idCliente": 1,
    "prioridad": "MEDIA",
    "estado": "ABIERTO",
    "asunto": "Consulta sobre pedido"
  }
]
```

## Códigos HTTP esperados

| Código | Significado |
|---|---|
| 200 OK | Tickets obtenidos correctamente |
| 204 No Content | No existen tickets disponibles |
| 500 Internal Server Error | Error interno del microservicio |

## Resultado

```txt
Pendiente de captura / Validado en Postman
```

---

# 9. Evidencia — Consulta de KPI

## Comunicación validada

```txt
Cliente REST / Administrador → API Gateway → MS Reportes
```

## Objetivo

Validar que el MS Reportes pueda exponer indicadores ejecutivos para la toma de decisiones.

## Request

```http
GET http://localhost:8080/kpi
```

## Response esperado

```json
[
  {
    "tipoKPI": "VENTAS_TOTALES",
    "valor": 1500000,
    "periodo": "MAYO_2026"
  },
  {
    "tipoKPI": "STOCK_BAJO",
    "valor": 8,
    "periodo": "MAYO_2026"
  }
]
```

## Códigos HTTP esperados

| Código | Significado |
|---|---|
| 200 OK | KPI obtenido correctamente |
| 204 No Content | No existen indicadores disponibles |
| 500 Internal Server Error | Error interno del microservicio |

## Resultado

```txt
Pendiente de captura / Validado en Postman
```

---

## Evidencias sugeridas para guardar

Las capturas o evidencias pueden guardarse en:

```txt
docs/evidencias/integracion/
```

Nombres recomendados:

```txt
01-health-api-gateway.png
02-consulta-stock.png
03-crear-envio.png
04-consulta-ventas.png
05-consulta-pedidos.png
06-consulta-inventario.png
07-consulta-tiendas.png
08-consulta-tickets.png
09-consulta-kpi.png
```

---

## Evidencia textual alternativa

Si no se adjuntan imágenes, se puede dejar evidencia textual indicando:

```txt
Método:
URL:
Body:
Código HTTP obtenido:
Resultado:
Observación:
```

Ejemplo:

```txt
Método: GET
URL: http://localhost:8080/actuator/health
Código HTTP obtenido: 200 OK
Resultado: API Gateway activo
Observación: Endpoint validado correctamente desde Postman
```

---

## Consideración sobre integración real

Durante el Sprint 3, algunos microservicios pueden encontrarse en estado parcial o en corrección.

Por este motivo, la validación de comunicación REST se puede realizar de dos formas:

1. **Validación real:** cuando ambos microservicios están implementados y levantados localmente.
2. **Validación documentada:** cuando un microservicio aún no está completamente disponible, pero se documentan ruta, request, response esperado y código HTTP.

Esto permite mantener trazabilidad técnica sin bloquear el avance del equipo.

---

## Flujo recomendado de prueba local

Para una validación completa, levantar primero el API Gateway:

```powershell
cd api-gateway
.\mvnw.cmd spring-boot:run
```

Luego levantar cada microservicio necesario en una terminal distinta:

```powershell
cd ms-inventario-abastecimiento
.\mvnw.cmd spring-boot:run
```

```powershell
cd ms-pedidos-ventas
.\mvnw.cmd spring-boot:run
```

```powershell
cd ms-logistica-envios
.\mvnw.cmd spring-boot:run
```

```powershell
cd ms-reportes
.\mvnw.cmd spring-boot:run
```

```powershell
cd ms-administracion-soporte
.\mvnw.cmd spring-boot:run
```

Después probar las rutas desde Postman usando:

```txt
http://localhost:8080
```

---

## Criterios de aceptación cubiertos

| Criterio de aceptación | Estado |
|---|---|
| Se identifican las comunicaciones REST principales | Cumplido |
| Existen pruebas que demuestran consumo entre servicios | Pendiente de evidencia Postman / Validado según disponibilidad |
| Se documentan request, response y códigos HTTP | Cumplido |
| La integración puede explicarse en la defensa | Cumplido |

---

## Riesgos técnicos identificados

| Riesgo | Mitigación |
|---|---|
| Microservicio aún incompleto | Documentar request/response esperado |
| Endpoint no implementado todavía | Registrar como pendiente técnico |
| Puerto distinto al esperado | Revisar `application.properties` |
| Error de conexión entre servicios | Validar primero endpoint directo y luego vía Gateway |
| Datos inexistentes en BD local | Crear datos de prueba mediante Postman |
| API Gateway no levantado | Probar primero `/actuator/health` |
| Dependencias o versiones inconsistentes | Revisar `pom.xml` antes de ejecutar Maven |

---

## Resumen para defensa

La comunicación REST entre microservicios permite que EcoMarket SPA mantenga una arquitectura desacoplada y escalable.

Los flujos principales son:

1. **Pedidos y Ventas consulta Inventario** para validar stock.
2. **Pedidos y Ventas solicita Logística** para generar envíos.
3. **Reportes consume Ventas, Inventario y Administración** para generar KPIs y reportes ejecutivos.

El API Gateway centraliza el acceso externo y permite validar las rutas REST desde Postman usando un único punto de entrada.

---

## Conclusión

La comunicación REST entre microservicios en EcoMarket SPA permite desacoplar responsabilidades y mantener una arquitectura escalable.

Las integraciones más importantes son:

- Pedidos consulta stock en Inventario.
- Pedidos solicita despacho a Logística.
- Reportes consume datos de Ventas, Inventario y Administración.

Esta documentación respalda la HU-50 y sirve como evidencia técnica para la defensa del Sprint 3.