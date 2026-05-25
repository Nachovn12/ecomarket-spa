# Evidencia REST, CRUD y HATEOAS

## Objetivo

Respaldar el uso de endpoints REST, operaciones CRUD y enlaces HATEOAS dentro del proyecto EcoMarket SPA.

Esta evidencia permite demostrar que el backend fue construido siguiendo buenas prácticas de APIs REST, separación por capas y respuestas navegables mediante Spring HATEOAS.

---

## Microservicio validado por Ignacio Valeria

MS Usuarios e Identidad

---

## HU relacionada

HU-3 — Actualización de perfil de cliente

---

## REST

El proyecto expone endpoints REST usando controladores Spring Boot con anotaciones como:

```java
@RestController
@RequestMapping
@GetMapping
@PostMapping
@PutMapping
@PatchMapping
@DeleteMapping
```

En el caso de la HU-3, el controlador principal es:

```text
ms-usuarios-identidad/src/main/java/com/ecomarket/usuarios/controller/UsuarioController.java
```

---

## Endpoints implementados para HU-3

| Operación                 | Método | Endpoint                                    |
| ------------------------- | ------ | ------------------------------------------- |
| Registrar usuario cliente | POST   | `/api/usuarios/registro`                    |
| Consultar perfil cliente  | GET    | `/api/usuarios/clientes/{idCliente}/perfil` |
| Actualizar perfil cliente | PUT    | `/api/usuarios/clientes/{idCliente}/perfil` |

---

## Relación con CRUD

Aunque la HU-3 no requiere eliminar usuarios, sí implementa operaciones relacionadas con CRUD:

| Acción            | Operación CRUD | Endpoint                                        |
| ----------------- | -------------- | ----------------------------------------------- |
| Registrar cliente | Create         | `POST /api/usuarios/registro`                   |
| Consultar perfil  | Read           | `GET /api/usuarios/clientes/{idCliente}/perfil` |
| Actualizar perfil | Update         | `PUT /api/usuarios/clientes/{idCliente}/perfil` |

No se implementa eliminación física del cliente en esta HU, ya que la historia se centra en actualización de perfil.

---

## HATEOAS

Se utiliza Spring HATEOAS para agregar enlaces navegables en las respuestas principales.

Clases usadas:

```java
EntityModel
linkTo
methodOn
```

Esto permite que la respuesta no solo entregue datos, sino también enlaces relacionados con acciones disponibles.

---

## Ejemplo de respuesta esperada con HATEOAS

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
      "href": "http://localhost:8083/api/usuarios/clientes/1/perfil"
    },
    "actualizarPerfil": {
      "href": "http://localhost:8083/api/usuarios/clientes/1/perfil"
    }
  }
}
```

---

## DTOs utilizados

Para evitar exponer directamente la entidad `Usuario`, se usan DTOs de entrada y salida.

| DTO                                 | Propósito                                  |
| ----------------------------------- | ------------------------------------------ |
| `UsuarioRequestDTO`                 | Registrar cliente                          |
| `UsuarioResponseDTO`                | Responder datos básicos del usuario        |
| `ActualizarPerfilClienteRequestDTO` | Recibir datos para actualizar perfil       |
| `PerfilClienteResponseDTO`          | Responder perfil de cliente sin contraseña |

---

## Validaciones

Se usa Bean Validation para validar datos antes de ejecutar lógica de negocio.

Validaciones aplicadas:

```java
@NotBlank
@Email
@Size
@Valid
```

Ejemplos:

| Campo              | Validación                            |
| ------------------ | ------------------------------------- |
| nombre             | Obligatorio, entre 3 y 120 caracteres |
| correo             | Obligatorio, formato email            |
| teléfono           | Obligatorio, entre 8 y 30 caracteres  |
| dirección de envío | Obligatoria, máximo 255 caracteres    |
| medio de pago      | Opcional, máximo 80 caracteres        |

---

## Manejo de errores

Se implementa `GlobalExceptionHandler` con `@RestControllerAdvice` para responder errores de manera centralizada.

Archivo:

```text
ms-usuarios-identidad/src/main/java/com/ecomarket/usuarios/exception/GlobalExceptionHandler.java
```

Errores manejados:

| Excepción                         | Código HTTP | Caso                     |
| --------------------------------- | ----------: | ------------------------ |
| `UsuarioNoEncontradoException`    |         404 | Cliente inexistente      |
| `UsuarioYaExisteException`        |         409 | Correo ya registrado     |
| `CredencialesInvalidasException`  |         401 | Credenciales incorrectas |
| `AccesoNoAutorizadoException`     |         403 | Acceso no autorizado     |
| `MethodArgumentNotValidException` |         400 | Datos inválidos          |
| `IllegalArgumentException`        |         400 | Argumentos inválidos     |
| `Exception`                       |         500 | Error interno            |

---

## Ejemplo de error por validación

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

---

## Flujo Controller → Service → Repository

La HU-3 mantiene separación por capas:

| Capa       | Archivo                  | Responsabilidad                                        |
| ---------- | ------------------------ | ------------------------------------------------------ |
| Controller | `UsuarioController`      | Recibe solicitudes REST y construye respuestas HATEOAS |
| Service    | `UsuarioService`         | Contiene lógica de negocio                             |
| Repository | `UsuarioRepository`      | Accede a la base de datos mediante JPA                 |
| Entity     | `Usuario`                | Representa la tabla `usuarios`                         |
| DTO        | DTOs de usuario/perfil   | Transporta datos de entrada y salida                   |
| Exception  | `GlobalExceptionHandler` | Maneja errores de forma centralizada                   |

---

## Lógica de negocio implementada

La lógica de negocio está en `UsuarioService`.

Reglas aplicadas:

- No se permite consultar un cliente inexistente.
- No se permite consultar un usuario eliminado.
- No se permite tratar como cliente a un usuario con rol distinto de `CLIENTE`.
- No se permite actualizar el correo si ya pertenece a otro usuario.
- Se normaliza el correo a minúsculas.
- Se actualizan nombre, correo, teléfono, dirección y medio de pago.
- Se registra `modificadoPor`.
- Se registra `fechaModificacionAcceso`.
- No se expone la contraseña en las respuestas.

---

## Persistencia

La entidad `Usuario` se persiste mediante JPA/Hibernate.

Archivo:

```text
ms-usuarios-identidad/src/main/java/com/ecomarket/usuarios/entity/Usuario.java
```

Campos agregados para HU-3:

```java
private String telefono;
private String direccionEnvio;
private String medioPago;
```

Repositorio usado:

```text
ms-usuarios-identidad/src/main/java/com/ecomarket/usuarios/repository/UsuarioRepository.java
```

Métodos relevantes:

```java
boolean existsByCorreo(String correo);
Optional<Usuario> findByCorreo(String correo);
```

---

## Evidencias recomendadas

Guardar capturas dentro de:

```text
docs/evidencias-tecnicas/capturas/
```

| Evidencia                      | Archivo sugerido                               |
| ------------------------------ | ---------------------------------------------- |
| Código UsuarioController       | `capturas/codigo_usuario_controller.png`       |
| Código UsuarioService          | `capturas/codigo_usuario_service.png`          |
| Código GlobalExceptionHandler  | `capturas/codigo_global_exception_handler.png` |
| Respuesta HATEOAS en Postman   | `capturas/postman_hateoas_perfil_cliente.png`  |
| Error de validación en Postman | `capturas/postman_error_validacion.png`        |

---

## Relación con la rúbrica

Esta evidencia respalda:

- Uso correcto de REST API.
- Separación por capas CSR.
- Persistencia con JPA/Hibernate.
- Uso de DTOs.
- Validaciones con Bean Validation.
- Manejo centralizado de errores.
- Respuestas con HATEOAS.
- Buenas prácticas de arquitectura backend.

---

## Evidencia técnica validada

REST define cómo se exponen los recursos del sistema mediante endpoints y métodos HTTP.

CRUD permite crear, leer y actualizar información persistente.

HATEOAS permite que la API entregue enlaces relacionados dentro de sus respuestas, mejorando la navegabilidad.

En la HU-3, el Cliente Web puede consultar y actualizar su perfil mediante endpoints REST, con validaciones, manejo de errores y respuestas navegables.

