// Busca los elementos del HTML por su id.
const filtroEstado = document.getElementById('estado');
const tabla = document.getElementById('incidencias'); // El cuerpo de la tabla: <tbody>.
const mensaje = document.getElementById('mensaje');

// Consulta la API y muestra las incidencias.
// async permite utilizar await para esperar las operaciones asíncronas.
async function cargarIncidencias() {

    // Borra las filas de la consulta anterior.
    tabla.replaceChildren();
    // Informa al usuario y desactiva el filtro mientras carga.
    mensaje.textContent = 'Cargando incidencias...';
    filtroEstado.disabled = true;

    // Ruta del backend que devuelve todas las incidencias.
    let url = '/api/incidencias';

    // Si se ha seleccionado un estado, lo añade como parámetro.
    // Ejemplo: /api/incidencias?estado=ABIERTA
    // encodeURIComponent prepara el valor para incluirlo en una URL.
    if (filtroEstado.value !== '') {
        url += '?estado=' + encodeURIComponent(filtroEstado.value);
    }

    try {
        // Envía una petición GET al backend.
        // await espera la respuesta sin bloquear la página.
        const respuesta = await fetch(url);

        // fetch no lanza un error automáticamente ante un 400 o un 500.
        // Por eso comprobamos si el estado HTTP está entre 200 y 299.
        if (!respuesta.ok) {
            // Interrumpe este bloque y pasa al catch.
            throw new Error('Error al consultar las incidencias');
        }


        // Lee el cuerpo JSON y lo convierte en datos de JavaScript.
        // En nuestra API, obtenemos un array de incidencias.
        const incidencias = await respuesta.json();

          // Si no hay resultados, muestra un mensaje y termina la función.
        // El bloque finally se ejecuta igualmente.
         if (incidencias.length === 0) {
            mensaje.textContent = 'No hay incidencias para este filtro.';
            return;
        }

        // Recorre las incidencias para crear una fila por cada una.
        for (const incidencia of incidencias) {
            const fila = document.createElement('tr');

            // Prepara los valores en el mismo orden que las columnas del HTML.
            const datos = [
                incidencia.id,
                incidencia.titulo,
                incidencia.prioridad,
                incidencia.estado,
                                // Convierte la fecha recibida en una fecha y hora legibles.
                new Date(incidencia.fechaCreacion).toLocaleString('es-ES'),
                     // Si tiene usuario, muestra su nombre.
                // Si usuario es null, muestra "Sin asignar".
                incidencia.usuario
                    ? incidencia.usuario.nombre
                    : 'Sin asignar'
            ];
            // Crea una celda por cada valor de la incidencia.
            for (const dato of datos) {
                const celda = document.createElement('td');
                                // Inserta el valor como texto, sin interpretarlo como HTML.
                celda.textContent = dato;
                                // Añade la celda a la fila.
                fila.appendChild(celda);
            }
            // Añade la fila completa al cuerpo de la tabla.
            tabla.appendChild(fila);
        }
        // Elimina el mensaje de carga cuando termina correctamente.
        mensaje.textContent = '';
    } catch (error) {
         // Atiende errores de conexión, respuestas HTTP rechazadas arriba
        // y errores al procesar los datos.
        mensaje.textContent =
            'No se pudieron cargar las incidencias. Inténtalo de nuevo.';
                    // Muestra el detalle técnico en la consola del navegador.
        console.error(error);
    } finally {
                // Reactiva el filtro tanto si funciona como si falla.
        filtroEstado.disabled = false;
    }
}
// Vuelve a consultar la API cuando el usuario cambia el estado.
// Aquí pasamos la función sin ejecutarla: por eso no lleva paréntesis.
filtroEstado.addEventListener('change', cargarIncidencias);
// Ejecuta la primera consulta al abrir la página.
cargarIncidencias();