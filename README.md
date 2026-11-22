# Gestión de incidencias

Aplicación web para registrar y consultar incidencias técnicas.

## Funcionalidades

- Consulta de incidencias en una tabla.
- Filtro por estado sin recargar la página.
- Formulario para crear incidencias, con asignación opcional de usuario.
- Mensajes de carga, ausencia de resultados y errores.
- Cambio de estado mediante la API, impidiendo reabrir incidencias cerradas.

## Tecnologías

- Java 17
- Spring Boot 3.5.0
- Spring Data JPA
- H2 en memoria
- HTML, CSS y JavaScript
- JUnit 5 y Mockito

## Requisitos

Tener instalado JDK 17. No es necesario instalar Maven:
el proyecto incluye Maven Wrapper.

La primera ejecución necesita conexión a Internet para descargar
las dependencias.

## Arrancar la aplicación

Desde la raíz del proyecto, en Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

En Linux o macOS:

```bash
./mvnw spring-boot:run
```

Abrir http://localhost:8080/

La base de datos se crea y carga con datos de ejemplo mediante
`src/main/resources/schema.sql`.

Los cambios se pierden al detener la aplicación porque H2
está configurada en memoria.

## Ejecutar los tests

En Windows:

```powershell
.\mvnw.cmd test
```

En Linux o macOS:

```bash
./mvnw test
```

Se incluyen dos tests unitarios del servicio:

- Cambio de una incidencia abierta a EN_CURSO.
- Rechazo de la reapertura de una incidencia cerrada.

También se incluye un test de carga del contexto de Spring.

## API

| Método | Ruta                            | Función              |
| ------- | ------------------------------- | --------------------- |
| POST    | /api/incidencias                | Crear una incidencia  |
| GET     | /api/incidencias                | Consultar incidencias |
| GET     | /api/incidencias?estado=ABIERTA | Filtrar por estado    |
| PATCH   | /api/incidencias/{id}/estado    | Cambiar el estado     |

Ejemplo de creación:

```json
{
  "titulo": "Error de impresora",
  "descripcion": "No imprime documentos",
  "prioridad": "ALTA",
  "usuarioId": 1
}
```

La descripción y el usuario asignado son opcionales.
El estado inicial es ABIERTA y la fecha se establece al crearla.

Ejemplo de cambio de estado:

```json
{
  "estado": "EN_CURSO"
}
```

## Decisiones técnicas

- Separación en controlador, servicio y repositorios.
- H2 en memoria para facilitar la ejecución sin instalar una base de datos.
- Un único script SQL para crear las tablas e insertar los ejemplos.
- Enums para los estados y las prioridades.
- DTOs de entrada para definir los datos aceptados y validarlos.
- Manejo centralizado de excepciones con formato JSON común.
- La regla que impide reabrir incidencias cerradas está en el servicio.
- Solicitar CERRADA para una incidencia ya cerrada se permite.
- Un usuario asignado inexistente se considera un dato inválido y devuelve 400.
- El email del usuario es único.
- La descripción tiene un límite de 2000 caracteres.
- Frontend servido por Spring Boot, con fetch y sin frameworks.
- Repositorios simulados con Mockito en los tests unitarios.

## Errores

Las respuestas gestionadas por el manejador de excepciones
tienen este formato:

```json
{
  "status": 409,
  "mensaje": "Una incidencia cerrada no puede reabrirse"
}
```

- 400: datos o parámetros inválidos.
- 404: incidencia inexistente al cambiar su estado.
- 409: intento de reabrir una incidencia cerrada.
- 500: error inesperado.
- 405: método HTTP no permitido.

## Mejoras pendientes

- Añadir tests de integración de los endpoints y la base de datos.
- Incorporar paginación si aumenta el volumen de incidencias.
- Controlar actualizaciones simultáneas para proteger la regla
  de cambio de estado ante peticiones concurrentes.

## Uso de IA

He utilizado ChatGPT como apoyo para preparar la estructura,
generar y explicar partes del código del backend, frontend y tests,
redactar documentación y resolver problemas durante el desarrollo.

La aplicación se ha comprobado mediante peticiones manuales,
pruebas en el navegador y ejecución de los tests.
