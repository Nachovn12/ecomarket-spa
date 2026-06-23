# Prompt de Implementación - HU-63

## Contexto General del Proyecto

- **Proyecto:** EcoMarket SPA (monorepo: Nachovn12/ecomarket-spa)
- **Sprint:** S5 - Pruebas Unitarias EP3
- **Rúbrica:** Rubrica_EP3_EcoMarket.md (indicadores IE 3.1.1, 3.1.2, 3.1.3)
- **Patrón de referencia del profesor:** https://github.com/profealejandroduoc/unitarias25
- **Referencia interna del equipo:** ms-pedidos-ventas/ (111 tests, JaCoCo 89.1%, mergeado en PR #41)
- **HU padre:** HU-60 EP-08 Pruebas Unitarias
- **Asignado en Jira:** Benjamin Espinoza
- **Reviewer en Jira:** Ignacio Valeria (Nachovn12)

## HU-63: Pruebas MS Catalogo

### Story Points: 5 (8h estimadas)

### Descripción
Como desarrollador del MS Catalogo quiero contar con pruebas unitarias de CatalogoService y de los controllers ProductoController, CategoriaController, ResenaController para garantizar las operaciones CRUD de productos, categorias y resenas, y validar las busquedas del catalogo.

### Dominio EcoMarket SPA
El Cliente del marketplace puede:
- Navegar y Buscar Productos con filtros (categoria, precio, eco-certificacion) y barra de busqueda.
- Dejar Resenas y Calificaciones sobre productos comprados.
- **Regla Crítica:** Solo clientes que compraron el producto pueden dejar resena.

---

## Plan de Trabajo (orden OBLIGATORIO)

**La IA y el desarrollador DEBEN seguir este orden exacto, alineado con las buenas prácticas del MS Pedidos.**

### Paso 0 - Sincronización y Rama Exclusiva (Git)

**OBLIGATORIO:** Siempre se debe traer la última actualización desde `develop` antes de empezar con el desarrollo.

Ejecutar en la terminal:
```bash
git checkout develop
git pull origin develop
git checkout -b feature/ep3-tests-ms-catalogo
```

### Paso 1 - Configurar JaCoCo en `pom.xml`

Agregar el plugin al `pom.xml` de `ms-catalogo`. **DEBE** incluir el bloque `<excludes>` para ignorar modelos, dtos y configuraciones:

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.13</version>
    <executions>
        <execution>
            <id>prepare-agent</id>
            <goals><goal>prepare-agent</goal></goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals><goal>report</goal></goals>
        </execution>
    </executions>
    <configuration>
        <excludes>
            <exclude>**/dto/**</exclude>
            <exclude>**/model/**</exclude>
            <exclude>**/exception/**</exclude>
            <exclude>**/config/**</exclude>
        </excludes>
    </configuration>
</plugin>
```

### Paso 2 - Crear el ServiceTest (Clase plana, SIN @Nested)

Estructura de `CatalogoServiceTest.java`:
- Usar **Clase plana** con `@ExtendWith(MockitoExtension.class)`. NO usar `@Nested`.
- Inyectar repositorios con `@Mock`.
- Inyectar service con `@InjectMocks`.

**Tests mínimos obligatorios:**
- CRUD de Productos, Categorias.
- `buscarProductos("bambu")` -> filtra por nombre.
- `guardarResena()` cliente NO ha comprado -> Lanza Excepción.
- `guardarResena()` cliente SI ha comprado -> Guarda.
- `calcularPromedioCalificaciones()` -> redondeo HALF_UP a 1 decimal.

### Paso 3 - Crear los ControllerTests con @WebMvcTest

Estructura de `ProductoControllerTest.java`, `CategoriaControllerTest.java`, `ResenaControllerTest.java`:
- Usar `@WebMvcTest(Controller.class)` (NO `MockMvcBuilders.standaloneSetup`).
- Usar `@MockitoBean CatalogoService`.
- **Validar JSON DTO Directo:** Garantizar que los JSON de respuesta NO traigan `_links` ni `_embedded` (HATEOAS fue removido).



### Paso 4 - Validación Final con JaCoCo

```bash
.\ms-catalogo\mvnw.cmd -f ms-catalogo/pom.xml clean test jacoco:report
```

**OJO:** Si el porcentaje de cobertura de JaCoCo en el reporte HTML es menor a 80%, la IA **NO DEBE** avanzar al siguiente paso. Debe iterar, analizando qué líneas faltan, y agregar más pruebas hasta superar el 80% real según las reglas de negocio de EcoMarket SPA.

### Paso 5 - Push, Pull Request y Documentación Jira (Cierre)

Una vez que se cumpla estrictamente con el umbral superior al 80% en JaCoCo:

1. Subir los cambios a la rama exclusiva:
```bash
git add .
git commit -m "test(ms-catalogo): pruebas unitarias con JaCoCo > 80%"
git push origin feature/ep3-tests-ms-catalogo
```

2. Crear el Pull Request (PR) en GitHub apuntando hacia `develop`.
3. **Asignar como Reviewer obligatorio a Ignacio Valeria (usuario GitHub: `Nachovn12`).**
4. En **Jira**: Cambiar el estado de la HU y sus Subtareas correspondientes de "En Curso" a "Finalizada".
5. **Documentación en Jira:** La IA (o el desarrollador) debe redactar un comentario en el ticket de Jira explicando detalladamente lo que se testeó, adjuntando el porcentaje oficial de cobertura obtenido con JaCoCo y confirmando que todas las reglas de negocio (como la regla de reseñas críticas) fueron validadas exitosamente.

---

## Criterios de Aceptación (formato Gherkin)

**AC-1: ServiceTest con MockitoExtension**
- **Dado** el service con repositorios mockeados
- **Cuando** se ejecuta el test
- **Entonces** todos los tests pasan en clase plana.

**AC-2: ControllerTests con `@WebMvcTest`**
- **Cuando** se ejecutan los 3 controller tests
- **Entonces** usan `@WebMvcTest` y validan el DTO sin `_links`.

**AC-3: Busqueda por texto**
- **Dado** un catalogo con productos eco bamboo
- **Cuando** se busca "bambu"
- **Entonces** se devuelven los productos bamboo

**AC-4: Regla "solo resenas de productos comprados"**
- **Dado** un cliente SIN pedido del producto X
- **Cuando** intenta dejar resena sobre X
- **Entonces** el sistema rechaza con excepcion de negocio

**AC-5: Promedio de calificaciones**
- **Dado** un producto con resenas (4, 5, 3 estrellas)
- **Cuando** se calcula el promedio
- **Entonces** el resultado es 4.0 con HALF_UP a 1 decimal

**DoD (Definition of Done):**
- Rama `feature/ep3-tests-ms-catalogo` subida con PR.
- PR asignado a Ignacio Valeria (`Nachovn12`) para revisión.
- Ticket en Jira finalizado y debidamente documentado con comentarios técnicos generados por la IA o el dev.