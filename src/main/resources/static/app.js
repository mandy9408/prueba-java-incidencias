const filtroEstado = document.getElementById('estado');
const tabla = document.getElementById('incidencias');
const mensaje = document.getElementById('mensaje');

async function cargarIncidencias() {
    tabla.replaceChildren();
    mensaje.textContent = 'Cargando incidencias...';
    filtroEstado.disabled = true;

    let url = '/api/incidencias';

    if (filtroEstado.value !== '') {
        url += '?estado=' + encodeURIComponent(filtroEstado.value);
    }

    try {
        const respuesta = await fetch(url);

        if (!respuesta.ok) {
            throw new Error('Error al consultar las incidencias');
        }

        const incidencias = await respuesta.json();

        if (incidencias.length === 0) {
            mensaje.textContent = 'No hay incidencias para este filtro.';
            return;
        }

        for (const incidencia of incidencias) {
            const fila = document.createElement('tr');

            const datos = [
                incidencia.id,
                incidencia.titulo,
                incidencia.prioridad,
                incidencia.estado,
                new Date(incidencia.fechaCreacion).toLocaleString('es-ES'),
                incidencia.usuario
                    ? incidencia.usuario.nombre
                    : 'Sin asignar'
            ];

            for (const dato of datos) {
                const celda = document.createElement('td');
                celda.textContent = dato;
                fila.appendChild(celda);
            }

            tabla.appendChild(fila);
        }

        mensaje.textContent = '';
    } catch (error) {
        mensaje.textContent =
            'No se pudieron cargar las incidencias. Inténtalo de nuevo.';
        console.error(error);
    } finally {
        filtroEstado.disabled = false;
    }
}

filtroEstado.addEventListener('change', cargarIncidencias);

cargarIncidencias();