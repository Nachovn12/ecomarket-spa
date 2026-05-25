# Índice de Evidencias Técnicas — EcoMarket SPA

## 1. Proyecto

EcoMarket SPA corresponde al proyecto backend desarrollado para la asignatura Desarrollo Full Stack I, sección 003D.

El sistema implementa una arquitectura basada en microservicios independientes, con API Gateway, persistencia de datos en MySQL, endpoints REST, validaciones, manejo de excepciones, logs, pruebas y documentación técnica asociada.

## 2. Objetivo del documento

Este documento organiza las evidencias técnicas del proyecto EcoMarket SPA, con el propósito de respaldar los criterios evaluados en la entrega EP2.

Las evidencias incluidas permiten demostrar:

- Organización del trabajo en Jira.
- Control de versiones mediante Git y GitHub.
- Implementación de microservicios con patrón CSR.
- Persistencia con JPA/Hibernate y bases de datos separadas.
- Endpoints REST funcionales.
- Pruebas con Postman.
- Validaciones y manejo de errores.
- Uso de HATEOAS.
- Ejecución de pruebas con Maven y JUnit.
- Arquitectura general de microservicios.

## 3. Estructura de evidencias técnicas

| Archivo | Contenido |
|---|---|
| `01_jira_sprints_epicas_hu.md` | Evidencia de planificación, sprints, historias de usuario, tareas y asignaciones en Jira |
| `02_git_github_pr_commits.md` | Evidencia de flujo Git, ramas, commits técnicos y Pull Requests |
| `03_postman_endpoints.md` | Evidencia de validación de endpoints REST mediante Postman |
| `04_junit_tests.md` | Evidencia de pruebas ejecutadas con Maven y JUnit |
| `05_rest_crud_hateoas.md` | Evidencia de implementación REST, operaciones CRUD y respuestas HATEOAS |
| `06_microservicios_arquitectura.md` | Evidencia de arquitectura de microservicios, separación funcional y responsabilidades |

## 4. Relación con la rúbrica EP2

Las evidencias técnicas se relacionan con los siguientes criterios evaluativos:

| Indicador | Evidencia relacionada |
|---|---|
| IE 1.2.1 — Patrón CSR | Código fuente por microservicio y evidencia de arquitectura |
| IE 2.1.1 — Modelado de base de datos | Documentación de bases de datos, entidades JPA y diagramas de clases |
| IE 2.1.2 — CRUD REST | Evidencia Postman, controladores, servicios y repositorios |
| IE 2.2.1 — Reglas de negocio | Implementación en la capa Service de cada microservicio |
| IE 2.2.2 — Validaciones | DTOs, Bean Validation y respuestas ante datos inválidos |
| IE 2.2.3 — Relaciones entre entidades | Entidades JPA, relaciones y documentación del modelo de datos |
| IE 2.3.1 — Manejo de excepciones | GlobalExceptionHandler y respuestas HTTP controladas |
| IE 2.3.2 — Logs estructurados | Registro de eventos relevantes en los flujos de servicio |
| IE 2.4.1 — Comunicación entre microservicios | Documentación de integración REST entre servicios |
| IE 2.4.2 — Endpoints REST remotos | API Gateway, rutas REST y consumo de datos entre servicios |
| IE 2.5.1 — GitHub | Commits, ramas, Pull Requests y flujo de integración |
| IE 2.5.2 — Jira/Trello | Sprints, tareas, historias de usuario y asignación de responsables |

## 5. Evidencias complementarias del repositorio

Además de esta carpeta, el repositorio incluye documentación técnica complementaria:

| Ruta | Propósito |
|---|---|
| `README.md` | Descripción general del proyecto, integrantes, instalación, ejecución y pruebas |
| `docs/arquitectura/arquitectura-microservicios.md` | Documentación de arquitectura distribuida |
| `docs/arquitectura/bases-datos-mysql.md` | Documentación de bases de datos por microservicio |
| `docs/integracion/comunicacion-rest-entre-servicios.md` | Documentación de comunicación REST interna |
| `docs/api-gateway-rutas.md` | Rutas configuradas en el API Gateway |
| `docs/git-flow.md` | Flujo de trabajo con Git y GitHub |
| `docs/hateoas/documentacion-hateoas-base.md` | Documentación base de HATEOAS |
| `docs/evidencias/evidencia-build-tests.md` | Evidencia de compilación y pruebas |
| `docs/postman/evidencia-postman.md` | Evidencia general de pruebas con Postman |
| `postman/EcoMarket-SPA.postman_collection.json` | Colección Postman del proyecto |
| `postman/EcoMarket-SPA-local.postman_environment.json` | Variables locales de Postman |

## 6. Diagramas incluidos

Los diagramas técnicos se encuentran organizados en:

| Carpeta | Contenido |
|---|---|
| `docs/diagramas/casos-uso/` | Diagramas de casos de uso por microservicio |
| `docs/diagramas/clases/` | Diagramas de clases por microservicio |
| `docs/diagramas/despliegue/` | Diagrama de despliegue backend |

Estos diagramas respaldan la comprensión del dominio, la separación funcional de los microservicios, las entidades principales y la arquitectura de despliegue.

## 7. Capturas de evidencia

Las capturas de evidencia se almacenan en:

```text
docs/evidencias-tecnicas/capturas/
```

Estas capturas respaldan pruebas realizadas en herramientas como Postman, GitHub, Jira u otras utilizadas durante el desarrollo del proyecto.

## 8. Alcance de las evidencias

Las evidencias técnicas tienen como finalidad respaldar el trabajo realizado en el proyecto backend EcoMarket SPA.

No reemplazan el código fuente ni las pruebas funcionales. Su función es complementar la revisión técnica, entregando trazabilidad sobre:

* Qué se implementó.
* Cómo se organizó el trabajo.
* Qué pruebas fueron realizadas.
* Qué herramientas se utilizaron.
* Cómo se relaciona el proyecto con los criterios de evaluación.

## 9. Estado esperado del repositorio

Antes de la entrega final, el repositorio debe cumplir con las siguientes condiciones:

* Código fuente versionado en GitHub.
* Rama final `main` actualizada.
* Rama `develop` integrada mediante Pull Request.
* Archivos `target/` eliminados.
* README actualizado.
* Documentación técnica disponible en `docs/`.
* Colección Postman incluida.
* Variables Postman incluidas.
* Evidencias técnicas organizadas.
* Commits técnicos y trazables.

## 10. Conclusión

La carpeta de evidencias técnicas permite respaldar formalmente los principales criterios de la evaluación EP2, demostrando implementación backend, persistencia, pruebas, integración, documentación y trabajo colaborativo en el proyecto EcoMarket SPA.

