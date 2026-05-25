# Evidencia Jira — Sprints, Épicas, HU y Tareas

## Objetivo

Respaldar la planificación, asignación y seguimiento del proyecto EcoMarket SPA mediante Jira.

Jira se utiliza para organizar el trabajo del equipo mediante épicas, historias de usuario, tareas, prioridades, responsables y sprints.

---

## Sprint documentado

Sprint 4 — Admin y Reportes

Este sprint considera el cierre académico del proyecto, preparación de evidencias técnicas, documentación y funcionalidades finales necesarias para la entrega.

### Estado de cierre del sprint

| Campo | Detalle |
|---|---|
| Sprint | S4 - Admin y Reportes |
| Estado del sprint | Listo para completarse |
| Total de issues del sprint | 14 |
| Estado de issues | Todas finalizadas |
| Rama esperada en GitHub | `develop` |

El Sprint 4 queda reflejado como cerrado funcionalmente desde el punto de vista académico, con sus issues finalizadas en Jira y con la evidencia técnica preparada para respaldar el avance en GitHub `develop`.

---

## HU/Tareas del Sprint 4

| ID | Tipo | Título | Responsable | Estado |
|---|---|---|---|---|
| HU-3 | Historia | Actualización de perfil de cliente | Ignacio Valeria | Finalizada |
| HU-9 | Historia | Publicación de reseña de producto | Benjamín Espinoza | Finalizada |
| HU-12 | Historia | Consulta de estado del pedido | Benjamín Flores | Finalizada |
| HU-15 | Historia | Atención de devoluciones y reclamaciones | Benjamín Flores | Finalizada |
| HU-21 | Historia | Optimización de rutas de entrega | Benjamín Espinoza | Finalizada |
| HU-25 | Historia | Monitorización del sistema | Ignacio Valeria | Finalizada |
| HU-26 | Historia | Respaldo y restauración de datos | Ignacio Valeria | Finalizada |
| HU-27 | Historia | Configuración de tienda | Ignacio Valeria | Finalizada |
| HU-28 | Historia | Generación de reportes de negocio | Benjamín Palma | Finalizada |
| HU-29 | Historia | Solicitud de soporte | Ignacio Valeria | Finalizada |
| HU-56 | Tarea | TT-08 Preparar evidencias técnicas para entrega | Ignacio Valeria | Finalizada |
| HU-57 | Tarea | TT-13 Documentar facturación electrónica y flujo comercial | Benjamín Flores | Finalizada |
| HU-58 | Tarea | TT-17 Implementar consultas base del MS Reportes | Benjamín Palma | Finalizada |
| HU-59 | Tarea | TT-21 Documentar aspectos éticos del proyecto | Benjamín Espinoza | Finalizada |

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

## HU-56 — Preparar evidencias técnicas para entrega

### Descripción

Preparar las evidencias técnicas del proyecto EcoMarket SPA para la entrega final.

### Actividades principales

- Reunir capturas de Jira con épicas, historias y sprints.
- Reunir evidencias de GitHub.
- Reunir evidencias de Postman.
- Reunir evidencias de JUnit.
- Reunir evidencias de REST, CRUD y HATEOAS.
- Ordenar evidencias según la rúbrica.
- Preparar evidencias técnicas para la entrega.

### Evidencia técnica relacionada

- Carpeta `docs/evidencias-tecnicas/`.
- Índice de evidencias.
- Evidencia de Jira.
- Evidencia de GitHub.
- Evidencia de Postman.
- Evidencia de JUnit.
- Evidencia REST, CRUD y HATEOAS.
- Evidencia de arquitectura de microservicios.
- Evidencias técnicas para la entrega.

---

## Capturas requeridas

Guardar capturas dentro de:

`docs/evidencias-tecnicas/capturas/`

| Evidencia | Archivo sugerido | Relación con rúbrica |
|---|---|---|
| Backlog general | `capturas/jira_backlog_general.png` | Organización del trabajo |
| Sprint 3 cerrado o en revisión | `capturas/jira_sprint3.png` | Seguimiento de avance |
| Sprint 4 listo para completarse | `capturas/jira_sprint4.png` | Gestión del cierre del Sprint 4 |
| Épicas por microservicio | `capturas/jira_epicas_microservicios.png` | Separación por dominio |
| HU asignadas por integrante | `capturas/jira_hu_asignadas.png` | Participación colaborativa |
| Detalle de HU-3 | `capturas/jira_hu3_perfil_cliente.png` | Evidencia de trabajo individual |
| Detalle de HU-56 | `capturas/jira_hu56_evidencias.png` | Evidencia técnica |

---

## Evidencia técnica validada

Jira permite demostrar que el equipo organizó el trabajo mediante una planificación ágil. Cada historia de usuario se relaciona con una necesidad del sistema, un microservicio o un entregable técnico. También permite evidenciar la participación individual de cada integrante.

Las HU asignadas fueron HU-3 y HU-56. La HU-3 se relaciona con el MS Usuarios e Identidad y la actualización del perfil del Cliente Web. La HU-56 corresponde a la preparación y organización de evidencias técnicas para la entrega final.

Para el Sprint 4, la evidencia registra que el sprint `S4 - Admin y Reportes` contiene 14 issues, todas finalizadas en Jira, y que el avance esperado para la entrega queda reflejado en la rama `develop` de GitHub.

