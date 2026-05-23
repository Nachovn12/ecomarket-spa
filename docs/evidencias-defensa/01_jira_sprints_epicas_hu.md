# Evidencia Jira — Sprints, Épicas, HU y Tareas

## Objetivo

Respaldar la planificación, asignación y seguimiento del proyecto EcoMarket SPA mediante Jira.

Jira se utiliza para organizar el trabajo del equipo mediante épicas, historias de usuario, tareas, prioridades, responsables y sprints.

---

## Sprint documentado

Sprint 4 — Admin y Reportes

Este sprint considera el cierre académico del proyecto, preparación de evidencias técnicas, documentación y funcionalidades finales necesarias para la defensa.

---

## HU/Tareas asignadas a Ignacio Valeria

| ID | Tipo | Título | Estado |
|---|---|---|---|
| HU-3 | Historia | Actualización de perfil de cliente | En desarrollo |
| HU-56 | Tarea | TT-08 Preparar evidencias técnicas para defensa | En desarrollo |

---

## HU-3 — Actualización de perfil de cliente

### Descripción

Como Cliente Web quiero modificar mi información personal, dirección de envío y medios de pago para mantener mis datos actualizados sin tener que crear una cuenta nueva.

### Criterios de aceptación

- El cliente puede actualizar sus datos personales.
- El cliente puede actualizar su dirección de envío.
- El cliente puede modificar su medio de pago.
- El sistema valida datos inválidos.
- El sistema no expone datos sensibles como contraseña.
- El sistema responde con códigos HTTP adecuados.

### Microservicio relacionado

MS Usuarios e Identidad.

### Evidencia técnica relacionada

- Implementación en `ms-usuarios-identidad`.
- Endpoints REST para consultar y actualizar perfil.
- Validaciones con Bean Validation.
- Manejo global de errores.
- Respuestas con HATEOAS.
- Evidencia Postman en `docs/postman/evidencia-s4-ignacio-usuarios.md`.

---

## HU-56 — Preparar evidencias técnicas para defensa

### Descripción

Preparar las evidencias técnicas del proyecto EcoMarket SPA para la entrega y defensa final.

### Actividades principales

- Reunir capturas de Jira con épicas, historias y sprints.
- Reunir evidencias de GitHub.
- Reunir evidencias de Postman.
- Reunir evidencias de JUnit.
- Reunir evidencias de REST, CRUD y HATEOAS.
- Ordenar evidencias según la rúbrica.
- Preparar material de apoyo para defensa técnica individual.

### Evidencia técnica relacionada

- Carpeta `docs/evidencias-defensa/`.
- Índice de evidencias.
- Evidencia de Jira.
- Evidencia de GitHub.
- Evidencia de Postman.
- Evidencia de JUnit.
- Evidencia REST, CRUD y HATEOAS.
- Evidencia de arquitectura de microservicios.
- Guía de defensa técnica individual.

---

## Capturas requeridas

Guardar capturas dentro de:

`docs/evidencias-defensa/capturas/`

| Evidencia | Archivo sugerido | Relación con rúbrica |
|---|---|---|
| Backlog general | `capturas/jira_backlog_general.png` | Organización del trabajo |
| Sprint 3 cerrado o en revisión | `capturas/jira_sprint3.png` | Seguimiento de avance |
| Sprint 4 iniciado | `capturas/jira_sprint4.png` | Gestión del Sprint actual |
| Épicas por microservicio | `capturas/jira_epicas_microservicios.png` | Separación por dominio |
| HU asignadas por integrante | `capturas/jira_hu_asignadas.png` | Participación colaborativa |
| Detalle de HU-3 | `capturas/jira_hu3_perfil_cliente.png` | Evidencia de trabajo individual |
| Detalle de HU-56 | `capturas/jira_hu56_evidencias.png` | Evidencia de defensa |

---

## Qué explicar en defensa

Jira permite demostrar que el equipo organizó el trabajo mediante una planificación ágil. Cada historia de usuario se relaciona con una necesidad del sistema, un microservicio o un entregable técnico. También permite evidenciar la participación individual de cada integrante.

En mi caso, las HU asignadas fueron HU-3 y HU-56. La HU-3 se relaciona con el MS Usuarios e Identidad y la actualización del perfil del Cliente Web. La HU-56 corresponde a la preparación y organización de evidencias técnicas para la defensa final.
