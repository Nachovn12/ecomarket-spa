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
- Se registran horarios de apertura y cierre.
- Se registran políticas locales.
- La respuesta contiene enlaces HATEOAS.

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
- La respuesta incluye enlace `self`.

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
- Se muestran horarios, ciudad, estado activo y políticas locales.
- La respuesta contiene enlaces HATEOAS.

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

- Se actualizan horarios y políticas locales.
- Se actualiza `fechaActualizacion`.
- La respuesta contiene enlaces HATEOAS.

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
- La respuesta contiene enlaces hacia tienda y personal.

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
- La respuesta contiene enlaces HATEOAS.

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
- Se registra prioridad.
- Se registra correo de contacto.
- La respuesta contiene enlaces HATEOAS.

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
- La respuesta contiene enlace `self`.

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
- La respuesta contiene enlaces HATEOAS.

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

- Se actualiza el estado del ticket.
- Se registra `fechaActualizacion`.
- La respuesta mantiene estructura HATEOAS.

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
- Si el ticket estaba `ABIERTO`, pasa a `EN_ATENCION`.
- La respuesta contiene enlaces al ticket.

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
- La respuesta contiene enlaces HATEOAS.

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
- No se genera alerta si está disponible y sin errores.
- La respuesta contiene enlaces a métricas y alertas.

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
- El sistema genera una alerta automáticamente.
- La alerta queda activa como `resuelta=false`.

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
- Se visualiza disponibilidad, tiempo de respuesta y errores.

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
- La respuesta contiene enlace para resolver alerta.

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
- La alerta deja de aparecer como activa.

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
- Se registra origen, frecuencia, responsable y fecha programada.

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

---

# Validaciones de error recomendadas

## Error por horario inválido

```text
POST http://localhost:8086/api/admin/tiendas
```

Body:

```json
{
  "nombre": "EcoMarket Error",
  "ciudad": "Valdivia",
  "horarioApertura": "21:00:00",
  "horarioCierre": "09:00:00",
  "politicasLocales": "Horario inválido"
}
```

Resultado esperado:

```text
400 Bad Request
```

---

## Error por email inválido

```text
POST http://localhost:8086/api/soporte/tickets
```

Body:

```json
{
  "asunto": "Correo inválido",
  "descripcion": "Prueba de validación de correo.",
  "nombreContacto": "Cliente Web",
  "correoContacto": "correo-invalido",
  "prioridad": "MEDIA"
}
```

Resultado esperado:

```text
400 Bad Request
```

---

## Error por enum inválido

```text
PATCH http://localhost:8086/api/soporte/tickets/1/estado?estado=NO_EXISTE
```

Resultado esperado:

```text
400 Bad Request
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
docs/evidencias-defensa/capturas/
```

| Evidencia          | Archivo sugerido                       |
| ------------------ | -------------------------------------- |
| Crear tienda       | `postman_admin_crear_tienda.png`       |
| Listar tiendas     | `postman_admin_listar_tiendas.png`     |
| Crear ticket       | `postman_soporte_crear_ticket.png`     |
| Responder ticket   | `postman_soporte_responder_ticket.png` |
| Registrar métrica  | `postman_monitorizacion_metrica.png`   |
| Generar alerta     | `postman_monitorizacion_alerta.png`    |
| Programar respaldo | `postman_respaldo_programar.png`       |
| Ejecutar respaldo  | `postman_respaldo_ejecutar.png`        |
| Restaurar respaldo | `postman_respaldo_restaurar.png`       |
| Error validación   | `postman_admin_error_validacion.png`   |

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
