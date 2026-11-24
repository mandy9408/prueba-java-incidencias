// Busca el formulario, el botón y el mensaje por sus id en el HTML.
const formulario = document.getElementById('formulario');
const guardar = document.getElementById('guardar');
const mensaje = document.getElementById('mensaje');

// Ejecuta esta función cuando el usuario envía el formulario.
// async permite utilizar await para esperar la respuesta del servidor.
formulario.addEventListener('submit', async function (evento) {

    // Evita que el navegador envíe el formulario y recargue la página.
    // Nosotros enviaremos los datos mediante fetch.
    evento.preventDefault();

    // Recoge los valores introducidos.
    // trim() elimina los espacios del principio y del final.
    const titulo = document.getElementById('titulo').value.trim();
    const descripcion = document.getElementById('descripcion').value.trim();
    const prioridad = document.getElementById('prioridad').value;
    const usuarioId = document.getElementById('usuarioId').value;

    // Rechaza también un título compuesto únicamente por espacios.
    // return termina la función sin enviar ninguna petición.
    if (titulo === '') {
        mensaje.textContent = 'El título es obligatorio.';
        return;
    }

    // Construye el objeto con los campos que espera el DTO del backend:
    // CrearIncidenciaRequest.
    const datos = {
        titulo: titulo,

        // Si la descripción está vacía, envía null.
        descripcion: descripcion || null,

        prioridad: prioridad,

        // Los valores de los inputs se leen como texto.
        // Si hay un identificador, lo convierte a número.
        // Si está vacío, envía null para dejarla sin asignar.
        usuarioId: usuarioId === '' ? null : Number(usuarioId)
    };

    // Desactiva el botón para evitar envíos repetidos mientras espera.
    guardar.disabled = true;
    mensaje.textContent = 'Guardando incidencia...';

    try {
        // Envía la petición al endpoint de creación del backend.
        const respuesta = await fetch('/api/incidencias', {

            // POST indica que queremos crear una incidencia.
            method: 'POST',

            // Informa al servidor de que el cuerpo contiene JSON.
            headers: {
                'Content-Type': 'application/json'
            },

            // Convierte el objeto JavaScript en texto JSON para enviarlo.
            body: JSON.stringify(datos)
        });

        // Comprueba si el código HTTP está fuera del rango 200–299.
        // fetch no lanza un error automáticamente ante un 400 o un 500.
        if (!respuesta.ok) {
            let detalle = 'No se pudo crear la incidencia.';

            try {
                // Intenta leer el JSON de error enviado por el backend.
                const error = await respuesta.json();

                // Utiliza su mensaje si existe y no está vacío.
                // Ejemplo: "No existe el usuario con id 999".
                detalle = error.mensaje || detalle;
            } catch {
                // Si el cuerpo no se puede leer como JSON,
                // conserva el mensaje genérico anterior.
            }

            // Pasa al catch exterior con el mensaje seleccionado.
            throw new Error(detalle);
        }

        // Si la creación funciona, vuelve a la página principal.
        // Allí app.js consultará de nuevo la lista de incidencias.
        window.location.href = '/';

    } catch (error) {
        // Muestra el error sin abandonar el formulario.
        // Los valores escritos se conservan porque no lo hemos reiniciado.
        mensaje.textContent = error.message || 'No se pudo conectar con el servidor.';

    } finally {
        // Reactiva el botón tanto si funciona como si falla.
        guardar.disabled = false;
    }
});