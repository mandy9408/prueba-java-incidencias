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

- **Arquitectura por capas (Controller, Service y Repository):** para separar responsabilidades, facilitar el mantenimiento del código y permitir realizar pruebas unitarias de la lógica de negocio de forma aislada.
- **Spring Data JPA:** para simplificar el acceso a la base de datos y evitar implementar manualmente las operaciones básicas de persistencia.
- **H2 en memoria:** para facilitar la ejecución y evaluación de la aplicación sin necesidad de instalar ni configurar una base de datos externa.
- **Inicialización mediante schema.sql:** para disponer de una estructura de datos y registros de ejemplo reproducibles desde el primer arranque.
- **Enums para estados y prioridades:** para limitar los valores permitidos y evitar estados inconsistentes.
- **DTOs de entrada y validaciones:** para controlar los datos recibidos por la API y separar las peticiones HTTP de las entidades de persistencia.
- **Manejo centralizado de excepciones:** para proporcionar respuestas de error consistentes y evitar duplicar su tratamiento en los controladores.
- **Lógica de negocio en el servicio:** la regla que impide reabrir incidencias cerradas se centraliza en el servicio para garantizar su cumplimiento independientemente del cliente que utilice la API.
- **Cambio de estado idempotente:** solicitar el estado CERRADA para una incidencia que ya está cerrada no genera un error, ya que no modifica su situación.
- **Validación del usuario asignado:** si se proporciona un identificador de usuario inexistente, se devuelve un error 400 para evitar asignaciones inválidas.
- **Restricciones de datos:** el email del usuario es único y la descripción de la incidencia está limitada a 2000 caracteres para mantener la integridad de los datos.
- **Frontend con HTML, CSS y JavaScript nativo:** dado el alcance reducido de la aplicación, no se ha considerado necesario incorporar un framework frontend. Se utiliza fetch para consumir la API REST de forma asíncrona.
- **Frontend servido por Spring Boot:** permite ejecutar toda la aplicación desde un único proyecto, evitando configuraciones adicionales y problemas de CORS.
- **JUnit 5 y Mockito:** para comprobar las reglas de negocio de forma aislada, simulando el comportamiento de los repositorios sin depender de una base de datos real.

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

Con más tiempo, incorporaría las siguientes mejoras:

* Ampliar la cobertura de tests unitarios, incluyendo la creación de incidencias, las validaciones y el filtrado por estado.
* Añadir tests de integración para comprobar los endpoints REST y la persistencia de datos.
* Incorporar paginación y ordenación para gestionar un mayor volumen de incidencias.
* Controlar las actualizaciones simultáneas mediante mecanismos de bloqueo optimista para garantizar el cumplimiento de las reglas de negocio ante peticiones concurrentes.
* Incorporar una base de datos persistente, como PostgreSQL, para un posible entorno de producción.
* Añadir autenticación y autorización si la aplicación evolucionase hacia un sistema multiusuario.
* Incorporar una pipeline de integración continua para automatizar la ejecución de los tests.

## Uso de IA

He utilizado ChatGPT como herramienta de apoyo durante el desarrollo de la aplicación, principalmente para:

- Orientar la estructura y organización del proyecto.
- Generar y explicar partes del código del backend y del frontend.
- Preparar y revisar los tests y la documentación.

El código se ha revisado y adaptado a los requisitos de la prueba técnica. El funcionamiento de la aplicación se ha comprobado mediante peticiones manuales a la API, pruebas en el navegador y ejecución de los tests.
