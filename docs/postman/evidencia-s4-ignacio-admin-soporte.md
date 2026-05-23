# Evidencia Postman — Sprint 4 — MS Administración y Soporte

## Proyecto

EcoMarket SPA — Desarrollo Fullstack I / DSY1103

## Responsable

Ignacio Andrés Valeria Núñez

## Microservicio

MS Administración y Soporte

## Rama de trabajo

`feature/s4-admin-soporte-ignacio`

---

## HU cubiertas

| ID    | Historia                         | Funcionalidad implementada                                                |
| ----- | -------------------------------- | ------------------------------------------------------------------------- |
| HU-25 | Monitorización del sistema       | Registro de métricas, generación de alertas y consulta de alertas activas |
| HU-26 | Respaldo y restauración de datos | Programación, ejecución, restauración y consulta de respaldos             |
| HU-27 | Configuración de tienda          | Creación, actualización, consulta de tienda y asignación de personal      |
| HU-29 | Solicitud de soporte             | Creación, consulta, cambio de estado y respuestas de tickets              |

---

## Base local

```text
MySQL con XAMPP/phpMyAdmin
```

Base de datos:

```text
bd_admin
```

Puerto del microservicio:

```text
8086
```

Base URL:

```text
http://localhost:8086
```

Carpeta de capturas:

```text
docs/evidencias-defensa/capturas/admin-soporte/
```

---

# HU-27 — Configuración de tienda

## 1. Crear tienda

### Método y URL

```text
POST http://localhost:8086/api/admin/tiendas
```

### Body

```json
{
  "nombre": "EcoMarket Lastarria",
  "ciudad": "Santiago",
  "horarioApertura": "09:00:00",
  "horarioCierre": "20:00:00",
  "politicasLocales": "Atención presencial, retiro en tienda y venta POS."
}
```

### Resultado esperado

```text
201 Created
```

### Validaciones esperadas

- Se crea una tienda activa.
- Se registra `idTienda` generado automáticamente por MySQL.
- Se registran horarios de apertura y cierre.
- Se registran políticas locales.
- Se registra `fechaCreacion`.
- La respuesta contiene enlaces HATEOAS:
  - `self`
  - `tiendas`
  - `personal`

### Evidencia asociada

```text
docs/evidencias-defensa/capturas/admin-soporte/postman_admin_crear_tienda.png
```

---

## 2. Listar tiendas

### Método y URL

```text
GET http://localhost:8086/api/admin/tiendas
```

### Resultado esperado

```text
200 OK
```

### Validaciones esperadas

- Se listan las tiendas registradas.
- La respuesta contiene `CollectionModel`.
- La tienda creada aparece dentro de `_embedded`.
- La respuesta incluye enlace `self`.
- Cada tienda mantiene enlaces HATEOAS.

### Evidencia asociada

```text
docs/evidencias-defensa/capturas/admin-soporte/postman_admin_listar_tiendas.png
```

---

## 3. Consultar tienda

### Método y URL

```text
GET http://localhost:8086/api/admin/tiendas/1
```

### Resultado esperado

```text
200 OK
```

### Validaciones esperadas

- Se consulta el detalle de la tienda.
- Se muestra `idTienda`.
- Se muestran horarios, ciudad, estado activo y políticas locales.
- La respuesta contiene enlaces HATEOAS:
  - `self`
  - `tiendas`
  - `personal`

### Evidencia asociada

```text
docs/evidencias-defensa/capturas/admin-soporte/postman_admin_consultar_tienda.png
```

---

## 4. Actualizar tienda

### Método y URL

```text
PUT http://localhost:8086/api/admin/tiendas/1
```

### Body

```json
{
  "nombre": "EcoMarket Lastarria Actualizada",
  "ciudad": "Santiago",
  "horarioApertura": "10:00:00",
  "horarioCierre": "21:00:00",
  "politicasLocales": "Horario extendido, retiro en tienda y soporte presencial."
}
```

### Resultado esperado

```text
200 OK
```

### Validaciones esperadas

- Se actualiza el nombre de la tienda.
- Se actualizan horarios y políticas locales.
- Se actualiza `fechaActualizacion`.
- La respuesta contiene enlaces HATEOAS:
  - `self`
  - `tiendas`
  - `personal`

### Evidencia asociada

```text
docs/evidencias-defensa/capturas/admin-soporte/postman_admin_actualizar_tienda.png
```

---

## 4.1. Consultar tienda actualizada

### Método y URL

```text
GET http://localhost:8086/api/admin/tiendas/1
```

### Resultado esperado

```text
200 OK
```

### Validaciones esperadas

- Se consulta nuevamente la tienda después de ejecutar el `PUT`.
- Se confirma que el nombre fue actualizado a `EcoMarket Lastarria Actualizada`.
- Se confirma que el horario de apertura fue actualizado a `10:00:00`.
- Se confirma que el horario de cierre fue actualizado a `21:00:00`.
- Se confirma que `fechaActualizacion` contiene un valor.
- Se confirma que los cambios fueron persistidos correctamente.
- La respuesta mantiene enlaces HATEOAS:
  - `self`
  - `tiendas`
  - `personal`

### Evidencia asociada

```text
docs/evidencias-defensa/capturas/admin-soporte/postman_admin_consultar_tienda_actualizada.png
```

---

## 5. Asignar personal a tienda

### Método y URL

```text
POST http://localhost:8086/api/admin/tiendas/1/personal
```

### Body

```json
{
  "idUsuarioInterno": 10,
  "idTienda": 1,
  "cargo": "Gerente de Tienda"
}
```

### Resultado esperado

```text
201 Created
```

### Validaciones esperadas

- Se asigna personal a una tienda existente.
- La asignación queda activa.
- Se registra `fechaAsignacion`.
- La respuesta contiene enlaces hacia:
  - tienda
  - personal de tienda

### Evidencia asociada

```text
docs/evidencias-defensa/capturas/admin-soporte/postman_admin_asignar_personal.png
```

---

## 6. Listar personal por tienda

### Método y URL

```text
GET http://localhost:8086/api/admin/tiendas/1/personal
```

### Resultado esperado

```text
200 OK
```

### Validaciones esperadas

- Se listan las asignaciones activas de la tienda.
- Se visualiza `idUsuarioInterno`.
- Se visualiza `cargo`.
- Se visualiza `idTienda`.
- La respuesta contiene enlaces HATEOAS.

### Evidencia asociada

```text
docs/evidencias-defensa/capturas/admin-soporte/postman_admin_listar_personal_tienda.png
```

---

# HU-29 — Solicitud de soporte

## 7. Crear ticket de soporte

### Método y URL

```text
POST http://localhost:8086/api/soporte/tickets
```

### Body

```json
{
  "asunto": "Problema con pedido web",
  "descripcion": "El cliente informa que no puede revisar el estado de su pedido desde la plataforma.",
  "nombreContacto": "Cliente Web EcoMarket",
  "correoContacto": "cliente.web@correo.cl",
  "prioridad": "MEDIA"
}
```

### Resultado esperado

```text
201 Created
```

### Validaciones esperadas

- Se crea un ticket en estado `ABIERTO`.
- Se registra prioridad `MEDIA`.
- Se registra correo de contacto.
- Se registra `fechaCreacion`.
- La respuesta contiene enlaces HATEOAS:
  - `self`
  - `tickets`
  - `respuestas`

### Evidencia asociada

```text
docs/evidencias-defensa/capturas/admin-soporte/postman_soporte_crear_ticket.png
```

---

## 8. Listar tickets

### Método y URL

```text
GET http://localhost:8086/api/soporte/tickets
```

### Resultado esperado

```text
200 OK
```

### Validaciones esperadas

- Se listan tickets de soporte.
- El ticket creado aparece dentro de la colección.
- La respuesta contiene enlace `self`.
- Cada ticket mantiene enlaces HATEOAS.

### Evidencia asociada

```text
docs/evidencias-defensa/capturas/admin-soporte/postman_soporte_listar_tickets.png
```

---

## 9. Consultar ticket

### Método y URL

```text
GET http://localhost:8086/api/soporte/tickets/1
```

### Resultado esperado

```text
200 OK
```

### Validaciones esperadas

- Se consulta el detalle del ticket.
- Se muestra estado, prioridad y datos de contacto.
- Se muestra `fechaCreacion`.
- La respuesta contiene enlaces HATEOAS:
  - `self`
  - `tickets`
  - `respuestas`

### Evidencia asociada

```text
docs/evidencias-defensa/capturas/admin-soporte/postman_soporte_consultar_ticket.png
```

---

## 10. Actualizar estado del ticket

### Método y URL

```text
PATCH http://localhost:8086/api/soporte/tickets/1/estado?estado=EN_ATENCION
```

### Resultado esperado

```text
200 OK
```

### Validaciones esperadas

- Se actualiza el estado del ticket a `EN_ATENCION`.
- Se registra `fechaActualizacion`.
- La respuesta mantiene estructura HATEOAS.

### Evidencia asociada

```text
docs/evidencias-defensa/capturas/admin-soporte/postman_soporte_actualizar_estado_ticket.png
```

---

## 11. Responder ticket

### Método y URL

```text
POST http://localhost:8086/api/soporte/tickets/1/respuestas
```

### Body

```json
{
  "idTicket": 1,
  "mensaje": "Se revisa el caso y se deriva a soporte técnico para seguimiento.",
  "respondidoPor": "Equipo de Soporte"
}
```

### Resultado esperado

```text
201 Created
```

### Validaciones esperadas

- Se registra una respuesta asociada al ticket.
- Se registra `fechaRespuesta`.
- Si el ticket estaba `ABIERTO`, pasa a `EN_ATENCION`.
- La respuesta contiene enlaces al ticket y a sus respuestas.

### Evidencia asociada

```text
docs/evidencias-defensa/capturas/admin-soporte/postman_soporte_responder_ticket.png
```

---

## 12. Listar respuestas del ticket

### Método y URL

```text
GET http://localhost:8086/api/soporte/tickets/1/respuestas
```

### Resultado esperado

```text
200 OK
```

### Validaciones esperadas

- Se listan respuestas asociadas al ticket.
- Se visualiza el mensaje registrado.
- Se visualiza `respondidoPor`.
- La respuesta contiene enlaces HATEOAS.

### Evidencia asociada

```text
docs/evidencias-defensa/capturas/admin-soporte/postman_soporte_listar_respuestas_ticket.png
```

---

# HU-25 — Monitorización del sistema

## 13. Registrar métrica de microservicio disponible

### Método y URL

```text
POST http://localhost:8086/api/admin/monitorizacion/metricas
```

### Body

```json
{
  "microservicio": "ms-catalogo",
  "disponible": true,
  "tiempoRespuestaMs": 120,
  "erroresDetectados": 0
}
```

### Resultado esperado

```text
201 Created
```

### Validaciones esperadas

- Se registra una métrica de rendimiento.
- Se registra disponibilidad `true`.
- Se registra tiempo de respuesta.
- No se genera alerta si está disponible y sin errores.
- La respuesta contiene enlaces a métricas y alertas.

### Evidencia asociada

```text
docs/evidencias-defensa/capturas/admin-soporte/postman_monitorizacion_metrica_disponible.png
```

---

## 14. Registrar métrica con fallo

### Método y URL

```text
POST http://localhost:8086/api/admin/monitorizacion/metricas
```

### Body

```json
{
  "microservicio": "ms-inventario-abastecimiento",
  "disponible": false,
  "tiempoRespuestaMs": 0,
  "erroresDetectados": 3
}
```

### Resultado esperado

```text
201 Created
```

### Validaciones esperadas

- Se registra una métrica con fallo.
- Se registra disponibilidad `false`.
- Se registran errores detectados.
- El sistema genera una alerta automáticamente.
- La alerta queda activa como `resuelta=false`.

### Evidencia asociada

```text
docs/evidencias-defensa/capturas/admin-soporte/postman_monitorizacion_metrica_fallo.png
```

---

## 15. Listar métricas

### Método y URL

```text
GET http://localhost:8086/api/admin/monitorizacion/metricas
```

### Resultado esperado

```text
200 OK
```

### Validaciones esperadas

- Se listan métricas registradas.
- Se visualiza disponibilidad.
- Se visualiza tiempo de respuesta.
- Se visualizan errores detectados.
- La respuesta contiene enlaces HATEOAS.

### Evidencia asociada

```text
docs/evidencias-defensa/capturas/admin-soporte/postman_monitorizacion_listar_metricas.png
```

---

## 16. Registrar alerta manual

### Método y URL

```text
POST http://localhost:8086/api/admin/monitorizacion/alertas
```

### Body

```json
{
  "microservicio": "ms-pedidos-ventas",
  "tipoAlerta": "ALTA_LATENCIA",
  "descripcion": "El microservicio de pedidos presenta tiempos de respuesta elevados."
}
```

### Resultado esperado

```text
201 Created
```

### Validaciones esperadas

- Se registra alerta activa.
- Se guarda fecha de generación.
- La alerta queda como `resuelta=false`.
- La respuesta contiene enlace para resolver alerta.

### Evidencia asociada

```text
docs/evidencias-defensa/capturas/admin-soporte/postman_monitorizacion_registrar_alerta.png
```

---

## 17. Listar alertas activas

### Método y URL

```text
GET http://localhost:8086/api/admin/monitorizacion/alertas
```

### Resultado esperado

```text
200 OK
```

### Validaciones esperadas

- Se muestran alertas no resueltas.
- Se visualiza el historial de eventos activos.
- La respuesta contiene enlaces HATEOAS.
- Se incluyen alertas generadas automáticamente y/o manualmente.

### Evidencia asociada

```text
docs/evidencias-defensa/capturas/admin-soporte/postman_monitorizacion_listar_alertas.png
```

---

## 18. Resolver alerta

### Método y URL

```text
PATCH http://localhost:8086/api/admin/monitorizacion/alertas/1/resolver
```

### Resultado esperado

```text
200 OK
```

### Validaciones esperadas

- Se marca la alerta como resuelta.
- Se registra `fechaResolucion`.
- La alerta deja de aparecer como activa si se consulta nuevamente.
- La respuesta mantiene enlaces HATEOAS.

### Evidencia asociada

```text
docs/evidencias-defensa/capturas/admin-soporte/postman_monitorizacion_resolver_alerta.png
```

---

# HU-26 — Respaldo y restauración de datos

## 19. Programar respaldo

### Método y URL

```text
POST http://localhost:8086/api/admin/respaldos
```

### Body

```json
{
  "origenDatos": "bd_admin",
  "frecuencia": "DIARIA",
  "responsable": "Administrador del Sistema",
  "fechaProgramada": "2026-05-23T09:00:00"
}
```

### Resultado esperado

```text
201 Created
```

### Validaciones esperadas

- Se programa un respaldo.
- Estado inicial: `PROGRAMADO`.
- Se registra origen de datos.
- Se registra frecuencia.
- Se registra responsable.
- Se registra fecha programada.
- La respuesta contiene enlaces HATEOAS.

### Evidencia asociada

```text
docs/evidencias-defensa/capturas/admin-soporte/postman_respaldo_programar.png
```

---

## 20. Listar respaldos

### Método y URL

```text
GET http://localhost:8086/api/admin/respaldos
```

### Resultado esperado

```text
200 OK
```

### Validaciones esperadas

- Se listan respaldos registrados.
- Se muestran estados y resultados.
- La respuesta contiene enlaces HATEOAS.

### Evidencia asociada

```text
docs/evidencias-defensa/capturas/admin-soporte/postman_respaldo_listar.png
```

---

## 21. Ejecutar respaldo

### Método y URL

```text
PATCH http://localhost:8086/api/admin/respaldos/1/ejecutar
```

### Resultado esperado

```text
200 OK
```

### Validaciones esperadas

- El respaldo cambia a estado `EJECUTADO`.
- Se registra `fechaEjecucion`.
- Se actualiza el resultado.
- La respuesta contiene enlaces HATEOAS.

### Evidencia asociada

```text
docs/evidencias-defensa/capturas/admin-soporte/postman_respaldo_ejecutar.png
```

---

## 22. Restaurar respaldo

### Método y URL

```text
PATCH http://localhost:8086/api/admin/respaldos/1/restaurar
```

### Resultado esperado

```text
200 OK
```

### Validaciones esperadas

- Solo se puede restaurar un respaldo ejecutado.
- El respaldo cambia a estado `RESTAURADO`.
- Se registra `fechaRestauracion`.
- La respuesta contiene enlaces HATEOAS.

### Evidencia asociada

```text
docs/evidencias-defensa/capturas/admin-soporte/postman_respaldo_restaurar.png
```

---

# Validaciones de error recomendadas

## Error por horario inválido

### Método y URL

```text
POST http://localhost:8086/api/admin/tiendas
```

### Body

```json
{
  "nombre": "EcoMarket Error",
  "ciudad": "Valdivia",
  "horarioApertura": "21:00:00",
  "horarioCierre": "09:00:00",
  "politicasLocales": "Horario inválido"
}
```

### Resultado esperado

```text
400 Bad Request
```

### Validaciones esperadas

- El sistema rechaza horarios inválidos.
- El horario de apertura no puede ser posterior al horario de cierre.
- La respuesta se entrega mediante `GlobalExceptionHandler`.

### Evidencia asociada

```text
docs/evidencias-defensa/capturas/admin-soporte/postman_admin_error_horario_invalido.png
```

---

## Error por email inválido

### Método y URL

```text
POST http://localhost:8086/api/soporte/tickets
```

### Body

```json
{
  "asunto": "Correo inválido",
  "descripcion": "Prueba de validación de correo.",
  "nombreContacto": "Cliente Web",
  "correoContacto": "correo-invalido",
  "prioridad": "MEDIA"
}
```

### Resultado esperado

```text
400 Bad Request
```

### Validaciones esperadas

- El sistema rechaza correos con formato inválido.
- Se activa validación `@Email`.
- La respuesta se entrega mediante `GlobalExceptionHandler`.

### Evidencia asociada

```text
docs/evidencias-defensa/capturas/admin-soporte/postman_soporte_error_email_invalido.png
```

---

## Error por enum inválido

### Método y URL

```text
PATCH http://localhost:8086/api/soporte/tickets/1/estado?estado=NO_EXISTE
```

### Resultado esperado

```text
400 Bad Request
```

### Validaciones esperadas

- El sistema rechaza estados que no pertenezcan al enum `EstadoTicket`.
- La respuesta se entrega mediante `GlobalExceptionHandler`.
- Se informa que el parámetro posee un valor inválido.

### Evidencia asociada

```text
docs/evidencias-defensa/capturas/admin-soporte/postman_soporte_error_enum_invalido.png
```

---

# Relación técnica

## REST

El controller expone endpoints REST usando:

```java
@RestController
@PostMapping
@GetMapping
@PutMapping
@PatchMapping
```

## HATEOAS

Las respuestas principales usan:

```java
EntityModel
CollectionModel
linkTo
methodOn
```

## Validaciones

Los DTOs usan:

```java
@NotBlank
@NotNull
@Email
@Size
@Min
@FutureOrPresent
```

## Manejo de errores

El microservicio usa:

```java
@RestControllerAdvice
@ExceptionHandler
```

## Persistencia

La persistencia se realiza con:

```text
JPA/Hibernate + MySQL/XAMPP
```

Para pruebas:

```text
H2 en memoria
```

---

# Evidencias recomendadas

Guardar capturas en:

```text
docs/evidencias-defensa/capturas/admin-soporte/
```

| Evidencia                    | Archivo sugerido                                 |
| ---------------------------- | ------------------------------------------------ |
| Crear tienda                 | `postman_admin_crear_tienda.png`                 |
| Listar tiendas               | `postman_admin_listar_tiendas.png`               |
| Consultar tienda             | `postman_admin_consultar_tienda.png`             |
| Actualizar tienda            | `postman_admin_actualizar_tienda.png`            |
| Consultar tienda actualizada | `postman_admin_consultar_tienda_actualizada.png` |
| Asignar personal             | `postman_admin_asignar_personal.png`             |
| Listar personal por tienda   | `postman_admin_listar_personal_tienda.png`       |
| Crear ticket                 | `postman_soporte_crear_ticket.png`               |
| Listar tickets               | `postman_soporte_listar_tickets.png`             |
| Consultar ticket             | `postman_soporte_consultar_ticket.png`           |
| Actualizar estado ticket     | `postman_soporte_actualizar_estado_ticket.png`   |
| Responder ticket             | `postman_soporte_responder_ticket.png`           |
| Listar respuestas del ticket | `postman_soporte_listar_respuestas_ticket.png`   |
| Registrar métrica disponible | `postman_monitorizacion_metrica_disponible.png`  |
| Registrar métrica con fallo  | `postman_monitorizacion_metrica_fallo.png`       |
| Listar métricas              | `postman_monitorizacion_listar_metricas.png`     |
| Registrar alerta manual      | `postman_monitorizacion_registrar_alerta.png`    |
| Listar alertas activas       | `postman_monitorizacion_listar_alertas.png`      |
| Resolver alerta              | `postman_monitorizacion_resolver_alerta.png`     |
| Programar respaldo           | `postman_respaldo_programar.png`                 |
| Listar respaldos             | `postman_respaldo_listar.png`                    |
| Ejecutar respaldo            | `postman_respaldo_ejecutar.png`                  |
| Restaurar respaldo           | `postman_respaldo_restaurar.png`                 |
| Error horario inválido       | `postman_admin_error_horario_invalido.png`       |
| Error email inválido         | `postman_soporte_error_email_invalido.png`       |
| Error enum inválido          | `postman_soporte_error_enum_invalido.png`        |

---

# Resultado esperado general

Las pruebas en Postman deben demostrar que el MS Administración y Soporte cumple con:

- Configuración de tiendas.
- Asignación de personal.
- Solicitudes de soporte.
- Seguimiento de tickets.
- Monitorización de microservicios.
- Alertas del sistema.
- Respaldos.
- Restauración de datos.
- Validaciones.
- HATEOAS.
- Manejo global de errores.
- Persistencia en MySQL/XAMPP.
