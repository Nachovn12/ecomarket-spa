# Evidencia GitHub — Ramas, Commits y Pull Requests

## Objetivo

Demostrar el uso de Git y GitHub para control de versiones, trabajo colaborativo y revisión de cambios.

El proyecto EcoMarket SPA utiliza un flujo basado en ramas para separar el trabajo estable del desarrollo activo.

---

## Flujo Git utilizado

| Rama        | Propósito                                         |
| ----------- | ------------------------------------------------- |
| `main`      | Rama estable del proyecto                         |
| `develop`   | Rama de integración                               |
| `feature/*` | Ramas de desarrollo por microservicio, HU o tarea |
| `hotfix/*`  | Correcciones urgentes                             |

---

## Rama individual Sprint 4

```text
feature/s4-ignacio-actualizacion-perfil-evidencias
```

Esta rama fue creada desde `develop` para trabajar las HU asignadas a Ignacio Valeria:

- HU-3 — Actualización de perfil de cliente.
- HU-56 — Preparar evidencias técnicas para entrega.

---

## Commits realizados

| Commit    | Mensaje                                                       | Descripción                                            |
| --------- | ------------------------------------------------------------- | ------------------------------------------------------ |
| `b23ed45` | `feat(usuarios): implementar actualizacion de perfil cliente` | Implementa HU-3 en MS Usuarios e Identidad             |
| `89569f2` | `docs(postman): agregar evidencia hu3 perfil cliente`         | Agrega evidencia Postman de HU-3                       |
| `62c7124` | `docs(evidencias): preparar evidencias tecnicas sprint final` | Agrega documentación de evidencias técnicas para HU-56 |

---

## Comandos usados como evidencia

```powershell
git status
git branch
git log --oneline -5
git diff --stat
```

---

## Estado actual esperado

Antes de crear el Pull Request, el repositorio debe quedar limpio:

```powershell
git status
```

Resultado esperado:

```text
nothing to commit, working tree clean
```

---

## Pull Request esperado

| Campo   | Valor                                                               |
| ------- | ------------------------------------------------------------------- |
| Base    | `develop`                                                           |
| Compare | `feature/s4-ignacio-actualizacion-perfil-evidencias`                |
| Título  | `feat(s4): actualizar perfil cliente y preparar evidencias tecnicas` |

---

## Descripción sugerida para el PR

```md
## Resumen

Se implementan y documentan actividades del Sprint 4 asignadas a Ignacio Valeria.

## HU/Tareas incluidas

- HU-3 — Actualización de perfil de cliente
- HU-56 — TT-08 Preparar evidencias técnicas para entrega

## Cambios realizados

- Se agregan campos de perfil a la entidad Usuario.
- Se crean DTOs para actualizar y responder perfil de cliente.
- Se implementan endpoints para consultar y actualizar perfil.
- Se agrega HATEOAS en respuestas principales.
- Se agrega manejo global de errores.
- Se configura H2 para tests.
- Se agrega evidencia Postman de HU-3.
- Se agregan documentos de evidencias técnicas para la entrega.

## Validaciones

- Maven package MS Usuarios: BUILD SUCCESS
- Maven test MS Usuarios: BUILD SUCCESS
- PR dirigido a develop.

## Observación

No mergear a main. PR listo para revisión antes de integrar a develop.
```

---

## Evidencias recomendadas

Guardar capturas dentro de:

```text
docs/evidencias-tecnicas/capturas/
```

| Evidencia           | Archivo sugerido                    |
| ------------------- | ----------------------------------- |
| Rama feature activa | `capturas/git_branch_feature.png`   |
| Commits realizados  | `capturas/git_log_commits.png`      |
| Working tree limpio | `capturas/git_status_clean.png`     |
| Pull Request creado | `capturas/github_pr_s4_ignacio.png` |

---

## Evidencia técnica validada

El equipo trabaja usando Git Flow académico. Cada integrante crea una rama `feature`, realiza commits técnicos, sube cambios al repositorio remoto y abre un Pull Request hacia `develop`.

Este flujo evita modificar directamente `main`, permite revisión cruzada antes de integrar y deja trazabilidad clara del trabajo realizado por cada integrante.

---

## Relación con la rúbrica

Esta evidencia respalda los siguientes puntos:

- Trabajo colaborativo mediante GitHub.
- Uso correcto de ramas.
- Trazabilidad mediante commits.
- Revisión mediante Pull Request.
- Separación entre desarrollo e integración.
- Evitar cambios directos sobre `main`.

---

## Checklist GitHub

- [ ] Rama creada desde `develop`.
- [ ] Commits técnicos realizados.
- [ ] `git status` limpio antes del push.
- [ ] Push realizado a GitHub.
- [ ] Pull Request creado hacia `develop`.
- [ ] PR revisado antes del merge.
- [ ] No se modificó `main` directamente.

