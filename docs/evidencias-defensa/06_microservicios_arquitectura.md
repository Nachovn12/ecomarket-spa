# Arquitectura de Microservicios — EcoMarket SPA

## Objetivo

Describir la arquitectura backend propuesta para EcoMarket SPA y su relación con la solución técnica desarrollada.

Esta evidencia permite explicar por qué el sistema fue separado en microservicios, cómo se organizan las responsabilidades y cómo se relaciona la arquitectura con la solución implementada durante el proyecto.

---

## Contexto del caso EcoMarket SPA

EcoMarket SPA es una empresa chilena dedicada a la venta de productos ecológicos y sostenibles.

Cuenta con tiendas en:

- Santiago, sector Lastarria.
- Valdivia.
- Antofagasta.

El sistema original de EcoMarket SPA era monolítico y presentaba problemas técnicos que afectaban la operación y la escalabilidad del negocio.

---

## Problema original

El sistema monolítico presentaba los siguientes problemas:

- Bajo rendimiento.
- Baja disponibilidad.
- Alto acoplamiento.
- Punto único de fallo.
- Dificultad para escalar módulos específicos.
- Dificultad para mantener funcionalidades separadas.
- Riesgo de que un error afectara a todo el sistema.
- Complejidad para integrar nuevas funcionalidades.

---

## Solución propuesta

La solución propuesta consiste en migrar hacia una arquitectura basada en microservicios con API Gateway.

Esta arquitectura permite dividir el sistema en servicios independientes, cada uno con una responsabilidad clara dentro del dominio del negocio.

---

## Objetivos de la arquitectura

- Separar responsabilidades por dominio funcional.
- Reducir el acoplamiento entre módulos.
- Mejorar mantenibilidad.
- Facilitar pruebas por microservicio.
- Permitir escalabilidad por componente.
- Evitar que un error en un módulo detenga todo el sistema.
- Centralizar el acceso externo mediante API Gateway.
- Mantener persistencia independiente por microservicio.

---

## Microservicios definidos

| Microservicio                  | Responsabilidad                                                 |
| ------------------------------ | --------------------------------------------------------------- |
| MS Usuarios e Identidad        | Login, registro, roles, permisos y perfiles                     |
| MS Catálogo                    | Productos, categorías, reseñas, imágenes y búsqueda             |
| MS Inventario y Abastecimiento | Stock, reservas, movimientos y alertas                          |
| MS Pedidos y Ventas            | Carrito, pedidos, pagos, facturas, devoluciones y reclamaciones |
| MS Logística de Envíos         | Envíos, rutas, proveedores y reabastecimiento                   |
| MS Administración y Soporte    | Tiendas, tickets, alertas, métricas y respaldos                 |
| MS Reportes                    | Reportes, KPIs y exportaciones                                  |
| API Gateway                    | Punto único de entrada y enrutamiento REST                      |
| Frontend Web                   | Tienda online y caja POS                                        |

---

## API Gateway

El API Gateway funciona como punto único de entrada para el frontend y clientes externos.

Rutas principales consideradas:

| Ruta          | Microservicio destino          |
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

## Stack tecnológico

El proyecto utiliza el siguiente stack:

| Tecnología        | Uso                                   |
| ----------------- | ------------------------------------- |
| Java 21           | Lenguaje principal backend            |
| Spring Boot 4.0.6 | Framework backend                     |
| Maven             | Gestión de dependencias y build       |
| JPA/Hibernate     | Persistencia ORM                      |
| MySQL             | Base de datos local                   |
| XAMPP/phpMyAdmin  | Administración local de base de datos |
| H2                | Base de datos en memoria para tests   |
| Spring HATEOAS    | Respuestas navegables                 |
| JUnit             | Pruebas                               |
| Postman           | Validación de endpoints               |
| Git/GitHub        | Control de versiones y PR             |
| Jira              | Gestión de HU, tareas y sprints       |

---

## Base de datos

Para ejecución académica local se utiliza:

```text
MySQL con XAMPP/phpMyAdmin
```

Cada microservicio debe mantener su propia base de datos lógica.

Ejemplo:

| Microservicio                  | Base de datos   |
| ------------------------------ | --------------- |
| MS Usuarios e Identidad        | `bd_usuarios`   |
| MS Catálogo                    | `bd_catalogo`   |
| MS Inventario y Abastecimiento | `bd_inventario` |
| MS Pedidos y Ventas            | `bd_ventas`     |
| MS Logística de Envíos         | `bd_logistica`  |
| MS Administración y Soporte    | `bd_admin`      |
| MS Reportes                    | `bd_reportes`   |

Para pruebas automatizadas se puede utilizar:

```text
H2 en memoria
```

Esto permite ejecutar pruebas sin depender de una base MySQL activa.

---

## Patrón CSR

Cada microservicio mantiene separación por capas.

| Capa         | Responsabilidad                                 |
| ------------ | ----------------------------------------------- |
| Controller   | Expone endpoints REST y recibe solicitudes HTTP |
| Service      | Contiene lógica de negocio                      |
| Repository   | Accede a datos mediante JPA                     |
| Model/Entity | Representa tablas y entidades del dominio       |
| DTO          | Transporta datos de entrada y salida            |
| Exception    | Maneja errores de forma controlada              |

---

## Flujo general Controller → Service → Repository

```text
Cliente / Postman / Frontend
        ↓
Controller
        ↓
Service
        ↓
Repository
        ↓
Base de datos
```

Este flujo permite separar responsabilidades y evitar que la lógica de negocio quede mezclada dentro de los controladores.

---

## Comunicación interna entre microservicios

La comunicación interna considerada para EcoMarket SPA es:

| Origen              | Destino                        | Propósito                           |
| ------------------- | ------------------------------ | ----------------------------------- |
| MS Pedidos y Ventas | MS Inventario y Abastecimiento | Consultar y reservar stock          |
| MS Pedidos y Ventas | MS Logística de Envíos         | Solicitar despacho                  |
| MS Reportes         | MS Pedidos y Ventas            | Obtener datos de ventas             |
| MS Reportes         | MS Inventario y Abastecimiento | Obtener datos de stock              |
| MS Reportes         | MS Administración y Soporte    | Obtener datos de tiendas y métricas |

---

## MS Usuarios e Identidad

Dentro del MS Usuarios e Identidad se implementan funcionalidades relacionadas con:

- Registro de clientes.
- Login.
- Roles.
- Permisos.
- Usuarios internos.
- Verificación de acceso.
- Actualización de perfil de cliente.

---

## HU-3 dentro del MS Usuarios e Identidad

La HU-3 corresponde a:

```text
Historia: Actualización de perfil de cliente
```

Su objetivo es permitir que un Cliente Web pueda modificar:

- Información personal.
- Correo.
- Teléfono.
- Dirección de envío.
- Medio de pago.

---

## Implementación técnica de HU-3

Archivos principales:

| Archivo                                    | Propósito                     |
| ------------------------------------------ | ----------------------------- |
| `Usuario.java`                             | Entidad principal de usuarios |
| `UsuarioRepository.java`                   | Acceso a datos con JPA        |
| `UsuarioService.java`                      | Lógica de negocio             |
| `UsuarioController.java`                   | Endpoints REST                |
| `ActualizarPerfilClienteRequestDTO.java`   | DTO de entrada                |
| `PerfilClienteResponseDTO.java`            | DTO de salida                 |
| `GlobalExceptionHandler.java`              | Manejo global de errores      |
| `MsUsuariosIdentidadApplicationTests.java` | Test de contexto con H2       |

---

## Endpoints HU-3

| Método | Endpoint                                    | Propósito             |
| ------ | ------------------------------------------- | --------------------- |
| POST   | `/api/usuarios/registro`                    | Registrar Cliente Web |
| GET    | `/api/usuarios/clientes/{idCliente}/perfil` | Consultar perfil      |
| PUT    | `/api/usuarios/clientes/{idCliente}/perfil` | Actualizar perfil     |

---

## Buenas prácticas aplicadas

- Uso de DTOs para evitar exponer directamente la entidad.
- No se expone la contraseña en respuestas.
- Validaciones con Bean Validation.
- Manejo centralizado de errores.
- Uso de HATEOAS.
- Uso de logs SLF4J.
- Pruebas con H2 en memoria.
- Separación Controller, Service y Repository.
- Commits técnicos en Git.
- Evidencia Postman documentada.

---

## Relación con la rúbrica

Esta evidencia respalda:

- Arquitectura de microservicios.
- Separación por capas.
- Persistencia con JPA/Hibernate.
- API REST.
- CRUD.
- HATEOAS.
- Validaciones.
- Manejo de errores.
- Pruebas.
- Git y trabajo colaborativo.
- Preparación de defensa técnica.

---

## Evidencias recomendadas

Guardar capturas dentro de:

```text
docs/evidencias-defensa/capturas/
```

| Evidencia                    | Archivo sugerido                         |
| ---------------------------- | ---------------------------------------- |
| Diagrama de despliegue       | `capturas/diagrama_despliegue.png`       |
| Diagrama de clases global    | `capturas/diagrama_clases_global.png`    |
| Diagrama MS Usuarios         | `capturas/diagrama_ms_usuarios.png`      |
| Estructura del proyecto      | `capturas/estructura_microservicios.png` |
| API Gateway o rutas          | `capturas/api_gateway_rutas.png`         |
| Configuración MySQL usuarios | `capturas/config_mysql_usuarios.png`     |

---

## Qué explicar en defensa

La arquitectura de microservicios permite dividir el sistema por dominios funcionales, reduciendo el acoplamiento y mejorando la mantenibilidad.

En EcoMarket SPA, cada microservicio tiene una responsabilidad clara, lo que permite desarrollar, probar y mantener funcionalidades de forma independiente.

La HU-3 se implementa dentro del MS Usuarios e Identidad porque corresponde a la gestión del perfil del Cliente Web. Esta funcionalidad se expone mediante endpoints REST, valida datos mediante DTOs, utiliza JPA para persistencia, HATEOAS para respuestas navegables y manejo global de errores para respuestas controladas.
