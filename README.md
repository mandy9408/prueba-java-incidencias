
# Gestión de incidencias

Aplicación web para registrar, consultar y gestionar incidencias técnicas de un equipo de soporte.

## Funcionalidades

- Consulta de incidencias en una tabla.
- Filtro por estado sin recargar la página.
- Formulario para crear incidencias, con asignación opcional de usuario.
- Mensajes de carga, ausencia de resultados y errores.
- Cambio de estado mediante la API.
- Restricción que impide reabrir incidencias cerradas.

## Tecnologías

- Java 17
- Spring Boot 3.5.0
- Spring Data JPA
- H2 en memoria
- HTML, CSS y JavaScript
- Maven
- JUnit 5 y Mockito

## Requisitos

Tener instalado JDK 17.

No es necesario instalar Maven, ya que el proyecto incluye Maven Wrapper.

La primera ejecución necesita conexión a Internet para descargar las dependencias.

## Arrancar la aplicación

Desde la raíz del proyecto, en Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

En Linux o macOS:

```bash
./mvnw spring-boot:run
```

Abrir en el navegador:

http://localhost:8080/

La base de datos se crea y carga con datos de ejemplo mediante el archivo `src/main/resources/schema.sql`.

Los datos se pierden al detener la aplicación porque H2 está configurada en memoria.

## Ejecutar los tests

En Windows:

```powershell
.\mvnw.cmd test
```

En Linux o macOS:

```bash
./mvnw test
```

Se incluyen dos tests unitarios de la capa de servicio:

- Comprobar que una incidencia abierta puede pasar a estado EN_CURSO.
- Comprobar que una incidencia cerrada no puede reabrirse.

También se incluye un test que comprueba que el contexto de Spring Boot se carga correctamente.

## API REST

| Método | Ruta                            | Función              |
| ------- | ------------------------------- | --------------------- |
| POST    | /api/incidencias                | Crear una incidencia  |
| GET     | /api/incidencias                | Consultar incidencias |
| GET     | /api/incidencias?estado=ABIERTA | Filtrar por estado    |
| PATCH   | /api/incidencias/{id}/estado    | Cambiar el estado     |

Ejemplo de creación de una incidencia:

```json
{
  "titulo": "Error de impresora",
  "descripcion": "No imprime documentos",
  "prioridad": "ALTA",
  "usuarioId": 1
}
```

La descripción y el usuario asignado son opcionales.

Todas las incidencias se crean inicialmente en estado ABIERTA y la fecha de creación se establece automáticamente.

Ejemplo de cambio de estado:

```json
{
  "estado": "EN_CURSO"
}
```

## Decisiones técnicas

- **Arquitectura por capas:** he separado el proyecto en Controller, Service y Repository para organizar el código, facilitar su mantenimiento y separar responsabilidades.
- **Spring Data JPA:** permite consultar y guardar datos sin tener que escribir manualmente todas las consultas SQL.
- **H2 en memoria:** facilita la ejecución del proyecto sin necesidad de instalar una base de datos externa.
- **Archivo schema.sql:** contiene la creación de las tablas, sus restricciones y los datos de ejemplo.
- **Enums:** permiten definir los valores válidos para los estados y las prioridades de las incidencias.
- **DTOs y validaciones:** permiten controlar qué datos recibe la API y comprobar que sean correctos antes de procesarlos.
- **Gestión centralizada de excepciones:** permite devolver los errores de la API con un formato común.
- **Reglas de negocio en el Service:** la lógica que impide reabrir incidencias cerradas se encuentra en la capa de servicio.
- **Incidencias cerradas:** solicitar nuevamente el estado CERRADA para una incidencia que ya está cerrada está permitido, ya que su estado no cambia.
- **Usuario asignado:** si se proporciona un identificador de usuario, se comprueba que exista. En caso contrario, se devuelve un error 400.
- **Restricciones de datos:** el email de los usuarios es único y la descripción de las incidencias tiene una longitud máxima de 2000 caracteres.
- **Frontend sin frameworks:** he utilizado HTML, CSS y JavaScript, tal como solicita el enunciado. La comunicación con el backend se realiza mediante fetch.
- **Frontend servido por Spring Boot:** permite ejecutar el frontend y el backend desde un único proyecto.
- **JUnit 5 y Mockito:** permiten comprobar la lógica de negocio utilizando repositorios simulados, sin depender de una base de datos real en los tests unitarios.

## Gestión de errores

Los errores gestionados por la API siguen un formato JSON común.

Por ejemplo:

```json
{
  "status": 409,
  "mensaje": "Una incidencia cerrada no puede reabrirse"
}
```

Códigos HTTP utilizados para los errores:

- 400: datos o parámetros incorrectos.
- 404: incidencia no encontrada.
- 405: método HTTP no permitido.
- 409: intento de reabrir una incidencia cerrada.
- 500: error interno del servidor.

## Mejoras pendientes

Con más tiempo, añadiría las siguientes mejoras:

- Crear más tests para comprobar la creación de incidencias, el filtrado y otros casos de error.
- Añadir pruebas de integración para comprobar el funcionamiento conjunto de la API y la base de datos.
- Incorporar filtros adicionales por prioridad y usuario asignado.
- Permitir ordenar las incidencias por fecha de creación.
- Incorporar paginación para mostrar las incidencias por páginas cuando haya muchos registros.
- Utilizar una base de datos persistente, como PostgreSQL, para conservar los datos al detener la aplicación.
- Añadir un sistema de inicio de sesión y permisos de usuario.

## Uso de IA

He utilizado ChatGPT como herramienta de apoyo durante el desarrollo de la aplicación, principalmente para:

- Orientar la estructura y organización del proyecto.
- Generar y explicar partes del código del backend y del frontend.
- Preparar y revisar los tests.
- Mejorar el diseño del frontend mediante CSS.
- Revisar la documentación del proyecto.

El código se ha revisado y adaptado a los requisitos de la prueba técnica. El funcionamiento de la aplicación se ha comprobado mediante peticiones manuales a la API, pruebas en el navegador y ejecución de los tests.
