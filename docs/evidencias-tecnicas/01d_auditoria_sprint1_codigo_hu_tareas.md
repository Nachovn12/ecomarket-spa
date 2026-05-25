# Auditoría Sprint 1 — Código, HU y Tareas

## EcoMarket SPA — DSY1103

Este documento registra la revisión documental y técnica del Sprint 1 desde la rama `develop`, consolidando la relación entre issues de Jira, código fuente, arquitectura, repositorio Git, estructura Maven/Spring Boot, criterios de calidad y documentación técnica.

## Sprint revisado

| Campo | Detalle |
|---|---|
| Sprint | S1 - Base e Identidad |
| Objetivo | Construir la base inicial de EcoMarket SPA, definiendo arquitectura de microservicios, repositorio Git, estructura Spring Boot + Maven y criterios académicos de calidad, junto con funcionalidades iniciales de usuarios, catálogo e inventario |
| Rama revisada | `develop` |
| Resultado Jira | 11 issues finalizadas |
| Criterio de revisión | Cada HU/Tarea debe tener respaldo en código, arquitectura, estructura de proyecto, Git Flow, criterios de calidad o documentación técnica |

---

## Criterios usados para validar cada HU/Tarea

Para considerar una HU/Tarea como reflejada se revisó:

1. Código real en `develop`.
2. Entidades, DTO, repositorios, servicios y controladores.
3. Endpoints REST.
4. Configuración Maven/Spring Boot.
5. Arquitectura documentada.
6. Git Flow documentado.
7. Criterios académicos de calidad.
8. Evidencia técnica/documental.

---

## Matriz Sprint 1 — Validación por código y evidencia

| Issue | Tipo | Microservicio/Componente | Validación en código develop | Evidencia/documentación | Estado técnico |
|---|---|---|---|---|---|
| HU-1 | Historia | MS Usuarios e Identidad | `UsuarioService`, `UsuarioController`, `UsuarioRequestDTO`, `UsuarioResponseDTO`, `Usuario` y `UsuarioRepository` respaldan registro de cuenta de cliente | Código fuente de `ms-usuarios-identidad`; `README.md` | Reflejada |
| HU-2 | Historia | MS Usuarios e Identidad | `AuthService`, `AuthController`, `LoginRequestDTO` y `LoginResponseDTO` respaldan inicio de sesión académico mediante credenciales | Código fuente de `ms-usuarios-identidad`; colección Postman principal | Reflejada |
| HU-4 | Historia | MS Usuarios e Identidad | `UsuarioInternoService`, `UsuarioInternoController` y DTOs de usuario interno respaldan gestión de cuentas internas | Código fuente de `ms-usuarios-identidad` | Reflejada |
| HU-5 | Historia | MS Usuarios e Identidad | `RolPermisoService`, `RolPermisoController`, `RolPermisosRequestDTO`, `RolPermisosResponseDTO` y `VerificacionAccesoResponseDTO` respaldan asignación y validación de permisos | Código fuente de `ms-usuarios-identidad` | Reflejada |
| HU-6 | Historia | MS Catálogo | `ProductoController`, `CatalogoService`, `Producto`, `Categoria` y `ProductoRepository` respaldan búsqueda y gestión base de productos ecológicos | Código fuente de `ms-catalogo`; `docs/postman/evidencia-postman.md` | Reflejada |
| HU-16 | Historia | MS Inventario y Abastecimiento | `ProductoService`, `ProductoController`, `Producto`, `ProductoRequestDTO`, `ProductoResponseDTO` y `ProductoRepository` respaldan creación de productos de inventario | Código fuente de `ms-inventario-abastecimiento` | Reflejada |
| HU-19 | Historia | MS Inventario y Abastecimiento | `ProductoService`, `ProductoController` y `ProductoRepository` respaldan consulta de inventario y stock disponible | Código fuente de `ms-inventario-abastecimiento` | Reflejada |
| HU-39 | Tarea | Arquitectura | La arquitectura de microservicios, bases de datos MySQL y diagramas de despliegue, clases y casos de uso documentan la estrategia base | `docs/arquitectura/arquitectura-microservicios.md`; `docs/arquitectura/bases-datos-mysql.md`; `docs/diagramas/` | Reflejada |
| HU-40 | Tarea | Git / GitHub | El flujo colaborativo queda documentado mediante Git Flow, ramas `main`, `develop`, `feature/*` y registro de commits | `docs/git-flow.md`; `README.md`; historial de commits | Reflejada |
| HU-41 | Tarea | Spring Boot + Maven | Cada microservicio y el API Gateway tienen `pom.xml`, `src/main/java`, `src/main/resources/application.properties` y `src/test/java` | Estructura de carpetas del repositorio; `README.md` | Reflejada |
| HU-42 | Tarea | Calidad académica | Los criterios académicos para historias y microservicios quedan documentados y vinculados a la evidencia técnica | `docs/calidad/criterios-academicos-historias-microservicios.md`; `README.md`; `docs/evidencias-tecnicas/` | Reflejada |

---

## Veredicto Sprint 1

Sprint 1 queda reflejado en GitHub `develop` a nivel de código fuente, estructura Spring Boot + Maven, arquitectura, Git Flow, criterios académicos de calidad, endpoints iniciales de Usuarios, Catálogo e Inventario, y documentación técnica.

Resultado:

```text
Sprint 1 validado técnicamente en develop.
11 issues revisadas.
11 issues con respaldo en código, documentación técnica o evidencia de configuración.
```
