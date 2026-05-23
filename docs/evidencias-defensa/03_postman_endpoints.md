# Evidencia Postman — Endpoints REST

## Objetivo

Demostrar que los endpoints REST principales del proyecto EcoMarket SPA fueron validados con Postman.

Postman permite verificar que los endpoints reciben JSON correctamente, aplican validaciones, devuelven códigos HTTP adecuados y responden con estructuras consistentes.

---

## Microservicio validado por Ignacio Valeria

MS Usuarios e Identidad

---

## HU relacionada

HU-3 — Actualización de perfil de cliente

---

## Evidencia individual HU-3

Archivo específico de evidencia:

```text
docs/postman/evidencia-s4-ignacio-usuarios.md
```

Este archivo documenta las pruebas realizadas sobre los endpoints de registro, consulta y actualización de perfil de cliente.

---

## Endpoints del MS Usuarios e Identidad

| Método | Endpoint                                    | Propósito                      |
| ------ | ------------------------------------------- | ------------------------------ |
| POST   | `/api/usuarios/registro`                    | Registrar un nuevo Cliente Web |
| GET    | `/api/usuarios/clientes/{idCliente}/perfil` | Consultar perfil de cliente    |
| PUT    | `/api/usuarios/clientes/{idCliente}/perfil` | Actualizar perfil de cliente   |

---

## Casos validados para HU-3

| Caso                             | Método | Endpoint                                    | Resultado esperado |
| -------------------------------- | ------ | ------------------------------------------- | ------------------ |
| Registro de cliente válido       | POST   | `/api/usuarios/registro`                    | 201 Created        |
| Consulta de perfil válido        | GET    | `/api/usuarios/clientes/{idCliente}/perfil` | 200 OK             |
| Actualización de perfil válido   | PUT    | `/api/usuarios/clientes/{idCliente}/perfil` | 200 OK             |
| Correo inválido                  | PUT    | `/api/usuarios/clientes/{idCliente}/perfil` | 400 Bad Request    |
| Cliente inexistente              | GET    | `/api/usuarios/clientes/{idCliente}/perfil` | 404 Not Found      |
| Correo ya usado por otro usuario | PUT    | `/api/usuarios/clientes/{idCliente}/perfil` | 409 Conflict       |

---

# 1. Registro de cliente

## Endpoint

```text
POST http://localhost:8081/api/usuarios/registro
```

## Body enviado

```json
{
  "nombre": "Ignacio Valeria",
  "correo": "ignacio.valeria@duocuc.cl",
  "password": "clave123"
}
```

## Resultado esperado

```text
201 Created
```

## Validaciones esperadas

- El sistema registra un nuevo usuario con rol `CLIENTE`.
- La respuesta no expone el campo `password`.
- La respuesta incluye datos básicos del usuario registrado.
- La respuesta incluye enlaces HATEOAS.

## Evidencia/captura

```text
docs/evidencias-defensa/capturas/postman_registro_cliente.png
```

---

# 2. Consulta de perfil de cliente

## Endpoint

```text
GET http://localhost:8081/api/usuarios/clientes/1/perfil
```

## Body enviado

No aplica.

## Resultado esperado

```text
200 OK
```

## Respuesta esperada

```json
{
  "id": 1,
  "nombre": "Ignacio Valeria",
  "correo": "ignacio.valeria@duocuc.cl",
  "telefono": "+56912345678",
  "direccionEnvio": "Santiago Centro, Chile",
  "medioPago": "Tarjeta terminada en 1234",
  "rol": "CLIENTE",
  "activo": true,
  "fechaRegistro": "2026-05-22T01:00:00",
  "_links": {
    "self": {
      "href": "http://localhost:8081/api/usuarios/clientes/1/perfil"
    },
    "actualizarPerfil": {
      "href": "http://localhost:8081/api/usuarios/clientes/1/perfil"
    }
  }
}
```

## Validaciones esperadas

- El sistema retorna el perfil del cliente.
- La respuesta no expone contraseña.
- El usuario debe tener rol `CLIENTE`.
- La respuesta contiene enlaces HATEOAS.

## Evidencia/captura

```text
docs/evidencias-defensa/capturas/postman_get_perfil_cliente.png
```

---

# 3. Actualización de perfil de cliente

## Endpoint

```text
PUT http://localhost:8081/api/usuarios/clientes/1/perfil
```

## Body enviado

```json
{
  "nombre": "Ignacio Valeria",
  "correo": "ignacio.valeria.actualizado@duocuc.cl",
  "telefono": "+56912345678",
  "direccionEnvio": "Santiago Centro, Chile",
  "medioPago": "Tarjeta terminada en 1234"
}
```

## Resultado esperado

```text
200 OK
```

## Validaciones esperadas

- El sistema actualiza nombre, correo, teléfono, dirección de envío y medio de pago.
- La respuesta no expone contraseña.
- La respuesta mantiene el rol `CLIENTE`.
- Se registra modificación mediante campos de auditoría disponibles.
- La respuesta contiene enlaces HATEOAS.

## Evidencia/captura

```text
docs/evidencias-defensa/capturas/postman_put_perfil_cliente.png
```

---

# 4. Validación de correo inválido

## Endpoint

```text
PUT http://localhost:8081/api/usuarios/clientes/1/perfil
```

## Body enviado

```json
{
  "nombre": "Ignacio Valeria",
  "correo": "correo-invalido",
  "telefono": "+56912345678",
  "direccionEnvio": "Santiago Centro, Chile",
  "medioPago": "Tarjeta terminada en 1234"
}
```

## Resultado esperado

```text
400 Bad Request
```

## Validaciones esperadas

- Bean Validation rechaza el correo inválido.
- El sistema responde con JSON de error.
- No se actualiza el perfil.
- La respuesta incluye detalle de validaciones.

## Ejemplo de respuesta esperada

```json
{
  "timestamp": "2026-05-22T01:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Error de validación",
  "validaciones": {
    "correo": "El correo debe tener un formato válido"
  }
}
```

## Evidencia/captura

```text
docs/evidencias-defensa/capturas/postman_error_correo_invalido.png
```

---

# 5. Validación de cliente inexistente

## Endpoint

```text
GET http://localhost:8081/api/usuarios/clientes/999/perfil
```

## Body enviado

No aplica.

## Resultado esperado

```text
404 Not Found
```

## Validaciones esperadas

- El sistema valida que el cliente no existe.
- Se lanza `UsuarioNoEncontradoException`.
- `GlobalExceptionHandler` responde con código 404.
- La respuesta mantiene estructura JSON consistente.

## Ejemplo de respuesta esperada

```json
{
  "timestamp": "2026-05-22T01:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Cliente no encontrado con id: 999"
}
```

## Evidencia/captura

```text
docs/evidencias-defensa/capturas/postman_error_cliente_inexistente.png
```

---

# 6. Validación de correo ya utilizado

## Endpoint

```text
PUT http://localhost:8081/api/usuarios/clientes/1/perfil
```

## Body enviado

```json
{
  "nombre": "Ignacio Valeria",
  "correo": "correo.usado@duocuc.cl",
  "telefono": "+56912345678",
  "direccionEnvio": "Santiago Centro, Chile",
  "medioPago": "Tarjeta terminada en 1234"
}
```

## Resultado esperado

```text
409 Conflict
```

## Validaciones esperadas

- El sistema verifica si el correo ya pertenece a otro usuario.
- Se lanza `UsuarioYaExisteException`.
- `GlobalExceptionHandler` responde con código 409.
- No se actualiza el perfil.

## Ejemplo de respuesta esperada

```json
{
  "timestamp": "2026-05-22T01:00:00",
  "status": 409,
  "error": "Conflict",
  "message": "Ya existe otra cuenta registrada con este correo"
}
```

## Evidencia/captura

```text
docs/evidencias-defensa/capturas/postman_error_correo_duplicado.png
```

---

## Relación con REST

Los endpoints siguen principios REST:

| Acción            | Método HTTP     | Justificación                     |
| ----------------- | --------------- | --------------------------------- |
| Registrar cliente | POST            | Crea un nuevo recurso             |
| Consultar perfil  | GET             | Consulta un recurso existente     |
| Actualizar perfil | PUT             | Actualiza un recurso existente    |
| Validaciones      | 400 / 404 / 409 | Devuelven códigos HTTP coherentes |

---

## Relación con HATEOAS

Las respuestas principales usan Spring HATEOAS mediante:

```java
EntityModel
linkTo
methodOn
```

Esto permite entregar enlaces relacionados en las respuestas, como:

- `self`
- `actualizarPerfil`

---

## Relación con Bean Validation

Los DTOs aplican validaciones con:

```java
@NotBlank
@Email
@Size
```

Estas validaciones permiten controlar datos inválidos antes de ejecutar la lógica de negocio.

---

## Relación con manejo de errores

El microservicio utiliza `GlobalExceptionHandler` con `@RestControllerAdvice` para centralizar errores:

| Excepción                         | Código HTTP               |
| --------------------------------- | ------------------------- |
| `UsuarioNoEncontradoException`    | 404 Not Found             |
| `UsuarioYaExisteException`        | 409 Conflict              |
| `MethodArgumentNotValidException` | 400 Bad Request           |
| `IllegalArgumentException`        | 400 Bad Request           |
| `Exception`                       | 500 Internal Server Error |

---

## Capturas sugeridas

Guardar capturas en:

```text
docs/evidencias-defensa/capturas/
```

| Evidencia                    | Archivo sugerido                                 |
| ---------------------------- | ------------------------------------------------ |
| Registro cliente Postman     | `capturas/postman_registro_cliente.png`          |
| Consulta perfil Postman      | `capturas/postman_get_perfil_cliente.png`        |
| Actualización perfil Postman | `capturas/postman_put_perfil_cliente.png`        |
| Error correo inválido        | `capturas/postman_error_correo_invalido.png`     |
| Error cliente inexistente    | `capturas/postman_error_cliente_inexistente.png` |
| Error correo duplicado       | `capturas/postman_error_correo_duplicado.png`    |

---

## Qué explicar en defensa

Postman permite demostrar que los endpoints REST funcionan correctamente, reciben JSON válido, devuelven códigos HTTP adecuados y aplican validaciones de negocio.

En la HU-3, Postman permite evidenciar que el Cliente Web puede consultar y actualizar su perfil, además de comprobar que el sistema responde correctamente ante errores como correo inválido, cliente inexistente o correo duplicado.
