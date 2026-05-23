# Preparación Defensa Técnica Individual — Ignacio Valeria

## Responsable

Ignacio Andrés Valeria Núñez

## Sprint

Sprint 4 — Admin y Reportes

## HU/Tareas asignadas

| ID    | Tipo     | Título                                          |
| ----- | -------- | ----------------------------------------------- |
| HU-3  | Historia | Actualización de perfil de cliente              |
| HU-56 | Tarea    | TT-08 Preparar evidencias técnicas para defensa |

---

## Objetivo

Preparar una guía personal para defender técnicamente el trabajo realizado en el proyecto EcoMarket SPA, explicando arquitectura, código, flujo Git, validaciones, pruebas y evidencias.

---

## Qué debo saber explicar

1. Arquitectura general de EcoMarket SPA.
2. Por qué se migró desde monolito a microservicios.
3. Qué hace el MS Usuarios e Identidad.
4. Cómo se implementó la actualización de perfil.
5. Flujo Controller → Service → Repository.
6. Uso de DTOs para entrada y salida.
7. Validaciones con Bean Validation.
8. Manejo global de errores.
9. Uso de HATEOAS.
10. Uso de MySQL para ejecución local.
11. Uso de H2 para tests.
12. Flujo Git: feature → commit → push → PR → develop.
13. Evidencias de Jira, GitHub, Postman y JUnit.

---

## Contexto del proyecto

EcoMarket SPA es una empresa chilena de productos ecológicos con tiendas en Santiago, Valdivia y Antofagasta.

El sistema original era monolítico y presentaba problemas de rendimiento, disponibilidad, acoplamiento y punto único de fallo.

La solución propuesta consiste en una arquitectura de microservicios con API Gateway, donde cada microservicio tiene una responsabilidad específica.

---

## Microservicio trabajado

MS Usuarios e Identidad.

Responsabilidades principales:

- Registro de clientes.
- Login.
- Gestión de usuarios.
- Roles y permisos.
- Verificación de acceso.
- Actualización de perfil de cliente.

---

## HU-3 — Actualización de perfil de cliente

### Descripción

Como Cliente Web quiero modificar mi información personal, dirección de envío y medios de pago para mantener mis datos actualizados sin tener que crear una cuenta nueva.

### Objetivo técnico

Permitir que un Cliente Web pueda consultar y actualizar su perfil desde el MS Usuarios e Identidad.

---

## Flujo técnico HU-3

1. Cliente consulta su perfil.
2. `UsuarioController` recibe la solicitud.
3. `UsuarioService` busca el usuario por ID.
4. El service valida que exista, no esté eliminado y tenga rol `CLIENTE`.
5. El service retorna un `PerfilClienteResponseDTO`.
6. El controller responde usando `EntityModel` con enlaces HATEOAS.

---

## Flujo de actualización de perfil

1. Cliente envía datos actualizados mediante `PUT`.
2. El DTO valida nombre, correo, teléfono y dirección.
3. `UsuarioService` normaliza el correo.
4. El service verifica que el correo no esté usado por otro usuario.
5. Se actualizan nombre, correo, teléfono, dirección de envío y medio de pago.
6. Se registra `modificadoPor`.
7. Se registra `fechaModificacionAcceso`.
8. Se guarda el usuario con `UsuarioRepository`.
9. Se retorna respuesta mediante `PerfilClienteResponseDTO`.
10. La respuesta no expone contraseña.

---

## Clases modificadas o creadas

| Archivo                                    | Propósito                                           |
| ------------------------------------------ | --------------------------------------------------- |
| `Usuario.java`                             | Agrega teléfono, dirección de envío y medio de pago |
| `ActualizarPerfilClienteRequestDTO.java`   | DTO de entrada para actualizar perfil               |
| `PerfilClienteResponseDTO.java`            | DTO de salida para responder perfil                 |
| `UsuarioService.java`                      | Lógica de negocio de consulta y actualización       |
| `UsuarioController.java`                   | Endpoints REST con HATEOAS                          |
| `GlobalExceptionHandler.java`              | Manejo centralizado de errores                      |
| `MsUsuariosIdentidadApplicationTests.java` | Test con H2 en memoria                              |
| `pom.xml`                                  | Agrega H2 para pruebas                              |

---

## Endpoints implementados

| Método | Endpoint                                    | Propósito                     |
| ------ | ------------------------------------------- | ----------------------------- |
| POST   | `/api/usuarios/registro`                    | Registrar Cliente Web         |
| GET    | `/api/usuarios/clientes/{idCliente}/perfil` | Consultar perfil del cliente  |
| PUT    | `/api/usuarios/clientes/{idCliente}/perfil` | Actualizar perfil del cliente |

---

## DTOs usados

### `ActualizarPerfilClienteRequestDTO`

Recibe:

- nombre
- correo
- teléfono
- dirección de envío
- medio de pago

Validaciones:

- `@NotBlank`
- `@Email`
- `@Size`

### `PerfilClienteResponseDTO`

Responde:

- id
- nombre
- correo
- teléfono
- dirección de envío
- medio de pago
- rol
- activo
- fechaRegistro

No responde:

- password

---

## Manejo de errores

Se implementa `GlobalExceptionHandler` usando `@RestControllerAdvice`.

Errores principales:

| Caso                 | Excepción                         | Código |
| -------------------- | --------------------------------- | -----: |
| Cliente inexistente  | `UsuarioNoEncontradoException`    |    404 |
| Correo duplicado     | `UsuarioYaExisteException`        |    409 |
| Datos inválidos      | `MethodArgumentNotValidException` |    400 |
| Acceso no autorizado | `AccesoNoAutorizadoException`     |    403 |
| Error interno        | `Exception`                       |    500 |

---

## HATEOAS

Se utiliza Spring HATEOAS mediante:

```java
EntityModel
linkTo
methodOn
```

Esto permite que la respuesta incluya enlaces relacionados como:

- `self`
- `actualizarPerfil`

---

## Persistencia

La persistencia se realiza mediante JPA/Hibernate.

Entidad principal:

```text
Usuario
```

Repositorio:

```text
UsuarioRepository
```

Métodos usados:

```java
boolean existsByCorreo(String correo);
Optional<Usuario> findByCorreo(String correo);
```

Base de datos local:

```text
MySQL con XAMPP/phpMyAdmin
```

Base de datos de pruebas:

```text
H2 en memoria
```

---

## Pruebas

### Compilación

```powershell
mvn -f ms-usuarios-identidad/pom.xml clean package -DskipTests
```

Resultado:

```text
BUILD SUCCESS
```

### Tests

```powershell
mvn -f ms-usuarios-identidad/pom.xml test
```

Resultado:

```text
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

---

## Flujo Git usado

Rama creada:

```text
feature/s4-ignacio-actualizacion-perfil-evidencias
```

Commits realizados:

| Commit    | Mensaje                                                       |
| --------- | ------------------------------------------------------------- |
| `b23ed45` | `feat(usuarios): implementar actualizacion de perfil cliente` |
| `89569f2` | `docs(postman): agregar evidencia hu3 perfil cliente`         |

Flujo esperado:

```text
feature/s4-ignacio-actualizacion-perfil-evidencias
        ↓
Pull Request hacia develop
        ↓
Revisión
        ↓
Merge a develop
        ↓
Eliminación de rama feature
```

---

## Preguntas probables de defensa

### ¿Dónde está la lógica de negocio?

En `UsuarioService`.

### ¿Qué hace el controller?

Recibe las solicitudes REST, valida el request con `@Valid`, llama al service y responde con `ResponseEntity` y `EntityModel`.

### ¿Por qué se usan DTOs?

Para separar la entrada/salida de datos de la entidad JPA y evitar exponer información sensible como la contraseña.

### ¿Qué pasa si el cliente no existe?

Se lanza `UsuarioNoEncontradoException` y `GlobalExceptionHandler` responde con 404 Not Found.

### ¿Qué pasa si el correo es inválido?

Bean Validation detecta el error y responde 400 Bad Request.

### ¿Qué pasa si el correo ya pertenece a otro usuario?

Se lanza `UsuarioYaExisteException` y responde 409 Conflict.

### ¿Por qué no se expone password?

Porque se responde con DTOs de salida que no incluyen el campo `password`.

### ¿Por qué se usa H2 en tests?

Para ejecutar pruebas sin depender de MySQL local, manteniendo MySQL/XAMPP como configuración oficial de ejecución.

### ¿Por qué no modificar main?

Porque el flujo correcto es trabajar en una rama feature y crear PR hacia develop.

### ¿Qué evidencia demuestra que funciona?

- Maven package con BUILD SUCCESS.
- Maven test con BUILD SUCCESS.
- Evidencia Postman.
- Código en GitHub.
- Commits técnicos.
- Pull Request hacia develop.
- Jira con HU asignadas y actualizadas.

---

## Resumen breve para exponer

En la HU-3 implementé la actualización del perfil de cliente dentro del MS Usuarios e Identidad.

Agregué campos de perfil a la entidad `Usuario`, creé DTOs para actualizar y responder datos del perfil, implementé endpoints REST para consultar y actualizar perfil, agregué validaciones con Bean Validation, manejo global de errores con `GlobalExceptionHandler`, respuestas HATEOAS y configuración de H2 para pruebas.

También preparé evidencia técnica para la defensa final, incluyendo Jira, GitHub, Postman, JUnit, REST, CRUD, HATEOAS y arquitectura de microservicios.

---

## Checklist personal de defensa

- [ ] Puedo explicar la arquitectura general.
- [ ] Puedo explicar MS Usuarios e Identidad.
- [ ] Puedo explicar HU-3.
- [ ] Puedo mostrar el código de `UsuarioController`.
- [ ] Puedo mostrar el código de `UsuarioService`.
- [ ] Puedo explicar DTOs.
- [ ] Puedo explicar validaciones.
- [ ] Puedo explicar manejo de errores.
- [ ] Puedo explicar HATEOAS.
- [ ] Puedo mostrar Postman.
- [ ] Puedo mostrar Maven test.
- [ ] Puedo explicar Git y PR.
- [ ] Puedo explicar Jira.
