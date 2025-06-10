let clienteServicios = null;
let areaActual = null;

// Variables para estadísticas
let estadisticas = {
    totalSolicitudes: 0,
    estudiantesPazYSalvo: 0,
    estudiantesConDeudas: 0
};

function setConectado(conectado) {
    document.getElementById('btnConectarAreaDeportes').disabled = conectado;
    document.getElementById('btnConectarAreaFinanciera').disabled = conectado;
    document.getElementById('btnConectarAreaLaboratorio').disabled = conectado;
    document.getElementById('btnDesconectar').disabled = !conectado;
    document.getElementById('btnEliminarDeudas').disabled = !conectado;
}

function conectar(area) {
    if (clienteServicios !== null) {
        console.log('Ya existe una conexión activa');
        return;
    }

    try {
        console.log('Iniciando conexión WebSocket...');
        const socket = new SockJS('http://localhost:5004/ws');
        clienteServicios = Stomp.over(socket);
        areaActual = area;

        // Configurar headers de conexión
        const headers = {
            'area': area
        };

        clienteServicios.connect(headers, 
            () => {
                console.log('Conectado exitosamente');
                suscripcionAChat(area);
                setConectado(true);
               
            },
            (error) => {
                console.error('Error de conexión:', error);
                setConectado(false);
                
                clienteServicios = null;
            }
        );
    } catch (error) {
        console.error('Error al crear conexión:', error);
        
    }
}

function desconectar() {
    if (clienteServicios !== null && clienteServicios.connected) {
        clienteServicios.disconnect(() => {
            console.log('Desconectado exitosamente');
            setConectado(false);
            areaActual = null;
            document.getElementById('divMensajeSolicitud').innerHTML = '';
            document.getElementById('divMensajeDeudas').innerHTML = '';
        });
        clienteServicios = null;
    }
}

function suscripcionAChat(area) {
    console.log('Suscribiendo a canales para área:', area);

    // Suscripción a notificaciones generales de solicitudes
    clienteServicios.subscribe('/notificaciones/solicitud', (message) => {
        console.log('Notificación de solicitud recibida:', message.body);
        procesarNotificacionSolicitud(message.body);
    });

    // Suscripción a notificaciones específicas del área
    const canalArea = `/notificaciones/deudas/${area}`;
    clienteServicios.subscribe(canalArea, (message) => {
        console.log(`Notificación de ${area} recibida:`, message.body);
        procesarNotificacionArea(message.body);
    });

    console.log(`Suscrito a notificaciones generales y de ${area}`);
}

function procesarNotificacionSolicitud(mensaje) {
    try {
        // Intentar parsear como JSON primero
        let data;
        try {
            data = JSON.parse(mensaje);
            // Si es JSON, mostrar el mensaje formateado
            const referenciaDivMensajes = document.getElementById('divMensajeSolicitud');
            const nuevoParrafo = document.createElement('p');
            nuevoParrafo.className = 'estado-mensaje';
            nuevoParrafo.textContent = `El estudiante con código ${data.codigo} y nombres ${data.nombres} ha realizado una nueva solicitud de paz y salvo`;
            referenciaDivMensajes.appendChild(nuevoParrafo);
        } catch (e) {
            // Si no es JSON, mostrar el mensaje tal cual
            const referenciaDivMensajes = document.getElementById('divMensajeSolicitud');
            const nuevoParrafo = document.createElement('p');
            nuevoParrafo.className = 'estado-mensaje';
            nuevoParrafo.textContent = mensaje;
            referenciaDivMensajes.appendChild(nuevoParrafo);
        }

        // Actualizar estadísticas
        estadisticas.totalSolicitudes++;
        actualizarEstadisticas();

        // Mostrar notificación del sistema
    } catch (error) {
        console.error('Error al procesar mensaje de solicitud:', error);
    }
}

function procesarNotificacionArea(mensaje) {
    try {
        // Intentar parsear como JSON primero
        let data;
        try {
            data = JSON.parse(mensaje);
            const referenciaDivMensajes = document.getElementById('divMensajeDeudas');
            
            // Crear un contenedor para esta notificación
            const notificacionContainer = document.createElement('div');
            notificacionContainer.className = 'notificacion-container';
            
            // Crear mensaje de estado
            const estadoParrafo = document.createElement('p');
            const esPazYSalvo = data.estado === 'APROBADO';
            estadoParrafo.className = `estado-mensaje ${esPazYSalvo ? 'estado-exito' : 'estado-error'}`;
            estadoParrafo.textContent = `Estado: ${esPazYSalvo ? 'A Paz y Salvo' : 'No A Paz y Salvo'}`;
            notificacionContainer.appendChild(estadoParrafo);

            // Si hay deudas, mostrarlas
            if (data.deudas && data.deudas.length > 0) {
                const deudasTitulo = document.createElement('p');
                deudasTitulo.textContent = 'Deudas pendientes:';
                notificacionContainer.appendChild(deudasTitulo);

                data.deudas.forEach(deuda => {
                    const deudaParrafo = document.createElement('p');
                    deudaParrafo.className = 'estado-mensaje estado-error';
                    deudaParrafo.textContent = `- ${deuda.descripcion}: ${deuda.valor}`;
                    notificacionContainer.appendChild(deudaParrafo);
                });
            }

            // Agregar un separador
            const separador = document.createElement('hr');
            notificacionContainer.appendChild(separador);

            // Agregar la nueva notificación al inicio del contenedor
            referenciaDivMensajes.insertBefore(notificacionContainer, referenciaDivMensajes.firstChild);

            // Actualizar estadísticas
            if (esPazYSalvo) {
                estadisticas.estudiantesPazYSalvo++;
            } else {
                estadisticas.estudiantesConDeudas++;
            }
        } catch (e) {
            // Si no es JSON, mostrar el mensaje tal cual
            const referenciaDivMensajes = document.getElementById('divMensajeDeudas');
            const notificacionContainer = document.createElement('div');
            notificacionContainer.className = 'notificacion-container';
            
            const nuevoParrafo = document.createElement('p');
            nuevoParrafo.className = 'estado-mensaje';
            nuevoParrafo.textContent = mensaje;
            notificacionContainer.appendChild(nuevoParrafo);
            
            // Agregar un separador
            const separador = document.createElement('hr');
            notificacionContainer.appendChild(separador);
            
            // Agregar la nueva notificación al inicio del contenedor
            referenciaDivMensajes.insertBefore(notificacionContainer, referenciaDivMensajes.firstChild);
        }

        actualizarEstadisticas();
    } catch (error) {
        console.error('Error al procesar mensaje de área:', error);
    }
}

function limpiarNotificacionesDeudas() {
    const referenciaDivMensajes = document.getElementById('divMensajeDeudas');
    referenciaDivMensajes.innerHTML = '';
}

function actualizarEstadisticas() {
    console.log('Estadísticas actualizadas:', estadisticas);
}

function enviarMensajePrivadoServidor() {
    if (clienteServicios && clienteServicios.connected && areaActual) {
        const mensaje = {
            area: areaActual,
            tipo: 'PAGO_DEUDA',
            timestamp: new Date().toISOString()
        };

        clienteServicios.send("/app/procesarPago", {}, JSON.stringify(mensaje));
        const referenciaDivMensajes = document.getElementById('divMensajeDeudas');
        referenciaDivMensajes.innerHTML = '';
    }
}

function eliminarDeudas() {
    const codigoEstudiante = document.getElementById('inputEliminarDeuda').value;
    if (!codigoEstudiante) {
        return;
    }

    if (!areaActual) {
        return;
    }

    // Construir la URL según el área seleccionada
    let url;
    switch (areaActual) {
        case 'deportes':
            url = `http://localhost:5001/api/deudasDeportivo/${codigoEstudiante}`;
            break;
        case 'financiera':
            url = `http://localhost:5002/api/deudasFinanciera/${codigoEstudiante}`;
            break;
        case 'laboratorios':
            url = `http://localhost:5003/api/prestamosLaboratorio/${codigoEstudiante}`;
            break;
        default:
            return;
    }

    // Realizar la petición DELETE
    fetch(url, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.text())
    .then(data => {
        // Limpiar el input
        document.getElementById('inputEliminarDeuda').value = '';
        // Actualizar la vista de deudas
        document.getElementById('divMensajeDeudas').innerHTML = '';
    })
    .catch(error => {
        console.error('Error al eliminar las deudas:', error);
    });
}

// Agregar el evento al botón
document.getElementById('btnEliminarDeudas').addEventListener('click', eliminarDeudas);

// Agregar el evento al botón de limpiar notificaciones de deudas
document.getElementById('btnLimpiarDeudas').addEventListener('click', limpiarNotificacionesDeudas);