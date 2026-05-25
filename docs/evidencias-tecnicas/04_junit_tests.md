# Evidencia JUnit — Pruebas del Proyecto

## Objetivo

Demostrar que los microservicios pueden ser compilados y probados con Maven y JUnit.

JUnit permite validar que el contexto de Spring Boot levante correctamente y que la configuración del microservicio sea consistente antes de integrar cambios al proyecto.

---

## Microservicio validado por Ignacio Valeria

MS Usuarios e Identidad

---

## HU relacionada

HU-3 — Actualización de perfil de cliente

---

## Comando de compilación

```powershell
mvn -f ms-usuarios-identidad/pom.xml clean package -DskipTests
```

## Resultado esperado

```text
BUILD SUCCESS
```

## Resultado obtenido

```text
BUILD SUCCESS
```

---

## Comando de pruebas

```powershell
mvn -f ms-usuarios-identidad/pom.xml test
```

## Resultado esperado

```text
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Resultado obtenido

```text
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

---

## Configuración de pruebas

El microservicio usa MySQL con XAMPP/phpMyAdmin en ejecución local.

Para pruebas automatizadas se utiliza H2 en memoria mediante `@TestPropertySource`, evitando depender de una conexión MySQL activa durante los tests.

---

## Configuración H2 usada

```text
jdbc:h2:mem:usuarios_test;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE
```

---

## Dependencia H2 en `pom.xml`

```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

---

## Configuración del test

Archivo:

```text
ms-usuarios-identidad/src/test/java/com/ecomarket/usuarios/MsUsuariosIdentidadApplicationTests.java
```

Configuración aplicada:

```java
@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:usuarios_test;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false",
        "spring.jpa.properties.hibernate.format_sql=false",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.sql.init.mode=never"
})
class MsUsuariosIdentidadApplicationTests {

    @Test
    void contextLoads() {
    }
}
```

---

## Evidencias requeridas

Guardar capturas en:

```text
docs/evidencias-tecnicas/capturas/
```

| Evidencia             | Archivo sugerido                      |
| --------------------- | ------------------------------------- |
| Maven package exitoso | `capturas/maven_package_usuarios.png` |
| Maven test exitoso    | `capturas/maven_test_usuarios.png`    |
| H2 cargando en tests  | `capturas/h2_test_usuarios.png`       |

---

## Evidencia técnica validada

JUnit valida que el microservicio levanta correctamente dentro del contexto de Spring Boot.

H2 permite ejecutar pruebas sin depender de MySQL local, manteniendo MySQL/XAMPP como configuración oficial para ejecución académica.

Esto permite que los tests sean más estables, rápidos y fáciles de ejecutar durante la validación técnica del proyecto.

---

## Relación con la rúbrica

Esta evidencia respalda los siguientes criterios:

- Pruebas automatizadas con JUnit.
- Configuración de entorno de pruebas.
- Separación entre base de datos local y base de datos de test.
- Correcta compilación del microservicio.
- Evidencia técnica para la entrega.

---

## Checklist JUnit

- [x] Se agregó H2 con scope `test`.
- [x] Se configuró `@TestPropertySource`.
- [x] El microservicio compila con Maven.
- [x] Los tests pasan correctamente.
- [x] La configuración local sigue usando MySQL/XAMPP.

