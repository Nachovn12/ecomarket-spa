# Criterios académicos de calidad - EcoMarket SPA

Este documento reúne los criterios mínimos que usaremos como equipo para saber cuándo una Historia de Usuario, una tarea técnica o un microservicio está listo para trabajarse y cuándo puede considerarse terminado.

La idea principal es que todos trabajemos con el mismo estándar, evitando avanzar tareas incompletas, código desordenado o funcionalidades sin evidencia.

---

## Objetivo

Definir una base común de calidad para el desarrollo de EcoMarket SPA, considerando el trabajo con microservicios, Spring Boot, Maven, GitHub, Jira y las evidencias solicitadas para la evaluación.

Estos criterios nos ayudan a:

- Saber cuándo una HU o tarea está lista para empezar.
- Saber cuándo una HU o tarea está realmente terminada.
- Mantener orden en los microservicios.
- Dejar evidencia clara para Jira y la entrega técnica.
- Evitar integrar a `develop` código incompleto o sin validar.

---

## Definition of Ready

Una Historia de Usuario o tarea técnica está lista para desarrollarse cuando el equipo puede entender claramente qué se debe hacer, para quién se hace y en qué microservicio se trabajará.

Antes de comenzar una HU o tarea, debe cumplir con lo siguiente:

- Tiene una descripción clara.
- Si es Historia de Usuario, está escrita con el formato: **Como / Quiero / Para**.
- Tiene criterios de aceptación entendibles.
- Está asociada a una épica, sprint o funcionalidad del proyecto.
- Tiene un responsable asignado.
- Tiene prioridad definida.
- Se identifica el actor relacionado del caso.
- Se identifica el microservicio responsable.
- Se conocen dependencias importantes, si existen.
- El alcance está claro, es decir, se entiende qué se hará y qué no se hará.

Si alguno de estos puntos no está claro, la tarjeta debería revisarse antes de empezar a programar.

---

## Definition of Done

Una Historia de Usuario o tarea técnica se considera terminada cuando no solo el código está hecho, sino también validado y documentado con evidencia.

Para cerrar una HU o tarea, debe cumplir con lo siguiente:

- La funcionalidad fue implementada en el microservicio correspondiente.
- El código está organizado por capas.
- Se respetó la estructura base del proyecto.
- El microservicio compila correctamente con Maven.
- Se aplicaron buenas prácticas básicas de Spring Boot.
- Se realizaron validaciones básicas cuando corresponde.
- Los endpoints fueron probados en Postman si aplica.
- Se creó una prueba JUnit cuando corresponde.
- Se consideró HATEOAS cuando la respuesta REST lo justifica.
- El código fue subido a la rama correspondiente.
- Se creó Pull Request hacia `develop`.
- Existe evidencia en Jira para explicar el trabajo realizado.

En resumen, una tarea no está terminada solo porque “funciona en mi computador”. Debe estar validada, subida correctamente y con evidencia.

---

## Criterios mínimos para cada microservicio

Cada microservicio debe mantener una estructura clara y fácil de entender. La idea es que cualquier integrante del equipo pueda revisar el código sin perderse.

La estructura base esperada es:

- `controller`
- `service`
- `repository`
- `entity`
- `dto`
- `exception`

Cada microservicio debe intentar cumplir con estos puntos:

- Tener una responsabilidad clara.
- Mantener nombres coherentes con el caso EcoMarket SPA.
- Separar correctamente la lógica por capas.
- Usar endpoints REST ordenados.
- Usar JPA/Repository cuando exista persistencia de datos.
- Aplicar validaciones básicas.
- Compilar correctamente con Maven.
- Tener evidencia relacionada en Jira.

---

## Evidencia esperada en Jira

Cada vez que se termine una HU o tarea técnica, se debe dejar evidencia en la tarjeta de Jira. No tiene que ser un texto enorme, pero sí debe dejar claro qué se hizo y cómo se validó.

Plantilla sugerida:

Se implementó:
[descripción breve del avance realizado]

Microservicio:  
ms-...

Rama:  
feature/ms-...

Validación Maven:
mvn clean package -DskipTests

Resultado:  
BUILD SUCCESS

Prueba Postman:  
[endpoint probado o captura si corresponde]

Pull Request:  
[link del PR]

Observaciones:  
[si aplica]

---

## Criterios para commits

Los commits deben ser claros, pequeños y relacionados con Jira. Esto permite entender el historial del proyecto y defender mejor el trabajo realizado.

Ejemplos correctos:

- feat(HU-1): implementar registro de cuenta de cliente
- feat(HU-2): implementar inicio de sesión
- fix(HU-5): corregir asignación de rol
- docs(TT-09): documentar criterios académicos de calidad

Evitar commits genéricos como:

- cambios
- avance
- cosas listas
- fix
- actualización

Un buen commit debe explicar qué se hizo y, si corresponde, a qué HU o tarea pertenece.

---

## Flujo de trabajo con Git

El equipo trabajará usando un flujo ordenado basado en ramas.

La regla principal es:

- No trabajar directo en `main`.
- No trabajar directo en `develop`.
- Cada integrante trabaja en su rama `feature/ms-*`.
- Los cambios se integran a `develop` mediante Pull Request.

Flujo esperado:

1. Actualizar `develop`.
2. Entrar a la rama del microservicio correspondiente.
3. Desarrollar la HU o tarea.
4. Hacer commits en la rama propia.
5. Validar con Maven y Postman si corresponde.
6. Hacer push a la rama feature.
7. Crear Pull Request hacia `develop`.
8. Dejar evidencia en Jira.

---

## Cuándo crear un Pull Request

Un Pull Request hacia `develop` debe crearse solo cuando el trabajo ya está listo para revisión.

Antes de crear el PR se debe verificar:

- La HU o tarea está terminada.
- El código está en la rama correcta.
- Maven muestra `BUILD SUCCESS`.
- Postman fue validado si corresponde.
- No se modificaron microservicios ajenos sin avisar.
- La evidencia está lista para pegar en Jira.

El Pull Request no debe usarse para subir código roto o incompleto.

---

## Cuándo mover una tarjeta a Hecho

Una tarjeta puede moverse a Hecho cuando el trabajo ya está completo, validado y documentado.

Debe cumplir con:

- Código terminado.
- Compilación correcta.
- Validación técnica realizada.
- Pull Request creado hacia `develop`.
- Evidencia agregada en Jira.
- El integrante deja evidencia técnica verificable del trabajo realizado.

Si falta alguno de estos puntos, la tarjeta no debería marcarse como finalizada todavía.

---

## Relación con la rúbrica

Estos criterios se definieron considerando los elementos técnicos solicitados para el proyecto:

- Spring Boot.
- Maven.
- Git y GitHub.
- Arquitectura basada en microservicios.
- Endpoints REST.
- CRUD cuando corresponde.
- Organización por capas.
- Validación con Postman.
- Pruebas JUnit cuando corresponde.
- HATEOAS cuando aplica.
- Evidencia técnica para la entrega.

---

## Regla de oro del equipo

No se integra a `develop` código incompleto, roto o sin validar.

Cada avance debe poder explicarse, compilar correctamente y quedar respaldado con evidencia en Jira.

