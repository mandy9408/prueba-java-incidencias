const formulario = document.getElementById('formulario');
const guardar = document.getElementById('guardar');
const mensaje = document.getElementById('mensaje');

formulario.addEventListener('submit', async function (evento) {
    evento.preventDefault();

    const titulo = document.getElementById('titulo').value.trim();
    const descripcion = document.getElementById('descripcion').value.trim();
    const prioridad = document.getElementById('prioridad').value;
    const usuarioId = document.getElementById('usuarioId').value;

    if (titulo === '') {
        mensaje.textContent = 'El título es obligatorio.';
        return;
    }

    const datos = {
        titulo: titulo,
        descripcion: descripcion || null,
        prioridad: prioridad,
        usuarioId: usuarioId === '' ? null : Number(usuarioId)
    };

    guardar.disabled = true;
    mensaje.textContent = 'Guardando incidencia...';

    try {
        const respuesta = await fetch('/api/incidencias', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(datos)
        });

        if (!respuesta.ok) {
            let detalle = 'No se pudo crear la incidencia.';

            try {
                const error = await respuesta.json();
                detalle = error.mensaje || detalle;
            } catch {
                // La respuesta puede no contener JSON.
            }

            throw new Error(detalle);
        }

        window.location.href = '/';
    } catch (error) {
        mensaje.textContent = error.message
            || 'No se pudo conectar con el servidor.';
    } finally {
        guardar.disabled = false;
    }
});