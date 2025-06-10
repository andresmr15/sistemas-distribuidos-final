let clienteServicios = null;
let areaActual = null;

function setConectado(conectado) {
    document.getElementById('btnConectarAreaDeportes').disabled = conectado;
    document.getElementById('btnConectarAreaFinanciera').disabled = conectado;
    document.getElementById('btnConectarAreaLaboratorio').disabled = conectado;
    document.getElementById('btnDesconectar').disabled = !conectado;
    document.getElementById('btnEliminarDeudas').disabled = !conectado;
    document.getElementById('btnLimpiarDeudas').disabled = !conectado;
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

        const headers = { 'area': area };

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

    clienteServicios.subscribe('/notificaciones/solicitud', (message) => {
        console.log('Notificación de solicitud recibida:', message.body);
        procesarNotificacionSolicitud(message.body);
    });

    const canalArea = `/notificaciones/deudas/${area}`;
    clienteServicios.subscribe(canalArea, (message) => {
        console.log(`Notificación de ${area} recibida:`, message.body);
        procesarNotificacionArea(message.body);
    });

    console.log(`Suscrito a notificaciones generales y de ${area}`);
}

function procesarNotificacionSolicitud(mensaje) {
    try {
        let data;
        try {
            data = JSON.parse(mensaje);
            mostrarNotificacionSolicitud(`El estudiante con código ${data.codigo} y nombres ${data.nombres} ha realizado una nueva solicitud de paz y salvo`);
        } catch (e) {
            mostrarNotificacionSolicitud(mensaje);
        }
    } catch (error) {
        console.error('Error al procesar mensaje de solicitud:', error);
    }
}

function procesarNotificacionArea(mensaje) {
    try {
        let data;
        try {
            data = JSON.parse(mensaje);
            mostrarNotificacionAreaJSON(data);
        } catch (e) {
            const mensajeFormateado = formatearMensajeDeudas(mensaje);
            mostrarMensajeFormateado(mensajeFormateado);
        }
    } catch (error) {
        console.error('Error al procesar mensaje de área:', error);
    }
}

function mostrarNotificacionSolicitud(texto) {
    const referenciaDivMensajes = document.getElementById('divMensajeSolicitud');
    const nuevoParrafo = document.createElement('p');
    nuevoParrafo.className = 'estado-mensaje';
    nuevoParrafo.textContent = texto;
    referenciaDivMensajes.appendChild(nuevoParrafo);
}

function mostrarNotificacionAreaJSON(data) {
    const referenciaDivMensajes = document.getElementById('divMensajeDeudas');
    const notificacionContainer = document.createElement('div');
    notificacionContainer.className = 'notificacion-container';
    
    const estadoParrafo = document.createElement('p');
    const esPazYSalvo = data.estado === 'APROBADO';
    estadoParrafo.className = `estado-mensaje ${esPazYSalvo ? 'estado-exito' : 'estado-error'}`;
    estadoParrafo.textContent = `Estado: ${esPazYSalvo ? 'A Paz y Salvo' : 'No A Paz y Salvo'}`;
    notificacionContainer.appendChild(estadoParrafo);

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

    const separador = document.createElement('hr');
    notificacionContainer.appendChild(separador);

    referenciaDivMensajes.insertBefore(notificacionContainer, referenciaDivMensajes.firstChild);
}

function formatearMensajeDeudas(mensaje) {
    try {
        const regexEstudiante = /El estudiante (\d+) tiene deudas en (\w+):/;
        const matchEstudiante = mensaje.match(regexEstudiante);
        
        if (!matchEstudiante) {
            console.log('No se pudo extraer información del estudiante del mensaje:', mensaje);
            return { mensajeOriginal: mensaje, mostrarFormateado: true };
        }

        const codigoEstudiante = matchEstudiante[1];
        const area = matchEstudiante[2];
        
        console.log('Parseando mensaje - Estudiante:', codigoEstudiante, 'Área:', area);
        
        const deudas = [];
        
        if (area === 'laboratorios') {
            extraerDeudasLaboratorio(mensaje, deudas);
        } else if (area === 'deportes') {
            extraerDeudasDeportes(mensaje, deudas);
        } else if (area === 'financiera' || area === 'finanzas') {
            extraerDeudasFinancieras(mensaje, deudas);
        }
        
        console.log('Deudas extraídas:', deudas);
        
        return {
            codigoEstudiante,
            area,
            deudas,
            mensajeOriginal: mensaje,
            mostrarFormateado: true
        };
    } catch (error) {
        console.error('Error al formatear mensaje:', error);
        return { mensajeOriginal: mensaje, mostrarFormateado: true };
    }
}

function extraerDeudasLaboratorio(mensaje, deudas) {
    const regexPrestamo = /PrestamoLaboratorio\(([^)]+)\)/g;
    let match;
    
    while ((match = regexPrestamo.exec(mensaje)) !== null) {
        const campos = extraerCamposDeTexto(match[1]);
        deudas.push({
            tipo: 'PrestamoLaboratorio',
            campos: campos
        });
    }
}

function extraerDeudasDeportes(mensaje, deudas) {
    const regexDeuda = /DeudaImplemento\(([^)]+)\)/g;
    let match;
    
    while ((match = regexDeuda.exec(mensaje)) !== null) {
        const campos = extraerCamposDeTexto(match[1]);
        deudas.push({
            tipo: 'DeudaImplemento',
            campos: campos
        });
    }
}

function extraerDeudasFinancieras(mensaje, deudas) {
    console.log('Procesando área financiera/finanzas');
    const patronesFinancieros = [
        /DeudaFinanciera\(([^)]+)\)/g,
        /DeudaAcademica\(([^)]+)\)/g,
        /DeudaMatricula\(([^)]+)\)/g,
        /DeudaPension\(([^)]+)\)/g,
        /Deuda\(([^)]+)\)/g
    ];
    
    patronesFinancieros.forEach(patron => {
        let match;
        while ((match = patron.exec(mensaje)) !== null) {
            console.log('Encontrada deuda financiera:', match[0]);
            const campos = extraerCamposDeTexto(match[1]);
            console.log('Campos extraídos:', campos);
            
            let tipoDeuda = 'DeudaFinanciera';
            if (patron.source.includes('DeudaAcademica')) tipoDeuda = 'DeudaAcademica';
            else if (patron.source.includes('DeudaMatricula')) tipoDeuda = 'DeudaMatricula';
            else if (patron.source.includes('DeudaPension')) tipoDeuda = 'DeudaPension';
            else if (patron.source.includes('Deuda\\(')) tipoDeuda = 'Deuda';
            
            deudas.push({
                tipo: tipoDeuda,
                campos: campos
            });
        }
    });
}

function extraerCamposDeTexto(camposTexto) {
    const campos = {};
    const regexCampos = /(\w+)=([^,]+)/g;
    let campoMatch;
    
    while ((campoMatch = regexCampos.exec(camposTexto)) !== null) {
        const nombreCampo = campoMatch[1];
        let valorCampo = campoMatch[2].trim();
        
        if (valorCampo === 'null') {
            valorCampo = null;
        }
        
        campos[nombreCampo] = valorCampo;
    }
    
    return campos;
}

function mostrarMensajeFormateado(data) {
    const referenciaDivMensajes = document.getElementById('divMensajeDeudas');
    const notificacionContainer = document.createElement('div');
    notificacionContainer.className = 'notificacion-container';
    
    if (data.codigoEstudiante && data.area && data.mostrarFormateado) {
        crearTituloEstudiante(notificacionContainer, data.codigoEstudiante, data.area);
        
        if (data.deudas && data.deudas.length > 0) {
            data.deudas.forEach((deuda, index) => {
                crearTarjetaDeuda(notificacionContainer, deuda, index);
            });
        } else {
            crearMensajeSinDeudas(notificacionContainer, data.mensajeOriginal);
        }
    } else {
        crearMensajeOriginal(notificacionContainer, data.mensajeOriginal);
    }
    
    agregarTimestamp(notificacionContainer);
    agregarSeparador(notificacionContainer);
    
    referenciaDivMensajes.insertBefore(notificacionContainer, referenciaDivMensajes.firstChild);
}

function crearTituloEstudiante(container, codigoEstudiante, area) {
    const titulo = document.createElement('h4');
    titulo.textContent = `Estudiante: ${codigoEstudiante} - Área: ${area.charAt(0).toUpperCase() + area.slice(1)}`;
    titulo.style.margin = '10px 0 5px 0';
    titulo.style.color = '#333';
    titulo.style.borderBottom = '2px solid #ddd';
    titulo.style.paddingBottom = '5px';
    container.appendChild(titulo);
}

function crearTarjetaDeuda(container, deuda, index) {
    const deudaDiv = document.createElement('div');
    deudaDiv.style.marginLeft = '15px';
    deudaDiv.style.marginBottom = '15px';
    deudaDiv.style.padding = '12px';
    deudaDiv.style.backgroundColor = '#f8f9fa';
    deudaDiv.style.border = '1px solid #dee2e6';
    deudaDiv.style.borderRadius = '6px';
    deudaDiv.style.boxShadow = '0 2px 4px rgba(0,0,0,0.1)';
    
    const tipoTitulo = document.createElement('h5');
    tipoTitulo.textContent = `${deuda.tipo} #${index + 1}`;
    tipoTitulo.style.margin = '0 0 10px 0';
    tipoTitulo.style.color = '#495057';
    tipoTitulo.style.fontSize = '16px';
    tipoTitulo.style.fontWeight = 'bold';
    deudaDiv.appendChild(tipoTitulo);
    
    Object.entries(deuda.campos).forEach(([campo, valor]) => {
        const campoDiv = document.createElement('div');
        campoDiv.style.marginBottom = '6px';
        campoDiv.style.display = 'flex';
        campoDiv.style.alignItems = 'flex-start';
        
        const etiqueta = document.createElement('span');
        etiqueta.innerHTML = `<strong>${formatearNombreCampo(campo)}:</strong>`;
        etiqueta.style.minWidth = '180px';
        etiqueta.style.color = '#495057';
        etiqueta.style.marginRight = '10px';
        
        const valorSpan = document.createElement('span');
        const valorFormateado = formatearValorCampo(campo, valor);
        valorSpan.innerHTML = valorFormateado.texto;
        valorSpan.style.color = valorFormateado.color;
        valorSpan.style.fontWeight = valorFormateado.negrita ? 'bold' : 'normal';
        valorSpan.style.flex = '1';
        
        campoDiv.appendChild(etiqueta);
        campoDiv.appendChild(valorSpan);
        deudaDiv.appendChild(campoDiv);
    });
    
    container.appendChild(deudaDiv);
}

function crearMensajeSinDeudas(container, mensajeOriginal) {
    const mensajeDiv = document.createElement('div');
    mensajeDiv.style.padding = '10px';
    mensajeDiv.style.backgroundColor = '#fff3cd';
    mensajeDiv.style.border = '1px solid #ffeaa7';
    mensajeDiv.style.borderRadius = '4px';
    mensajeDiv.style.marginTop = '10px';
    
    const mensajeP = document.createElement('p');
    mensajeP.textContent = mensajeOriginal;
    mensajeP.style.margin = '0';
    mensajeDiv.appendChild(mensajeP);
    
    container.appendChild(mensajeDiv);
}

function crearMensajeOriginal(container, mensajeOriginal) {
    const mensajeDiv = document.createElement('div');
    mensajeDiv.style.padding = '12px';
    mensajeDiv.style.backgroundColor = '#f8f9fa';
    mensajeDiv.style.border = '1px solid #dee2e6';
    mensajeDiv.style.borderRadius = '6px';
    mensajeDiv.style.fontFamily = 'monospace';
    mensajeDiv.style.fontSize = '13px';
    mensajeDiv.style.lineHeight = '1.4';
    mensajeDiv.style.wordBreak = 'break-word';
    
    const mensajeFormateado = mensajeOriginal
        .replace(/\[/g, '[\n  ')
        .replace(/\]/g, '\n]')
        .replace(/,\s*/g, ',\n  ')
        .replace(/\(/g, '(\n    ')
        .replace(/\)/g, '\n  )');
    
    const pre = document.createElement('pre');
    pre.textContent = mensajeFormateado;
    pre.style.margin = '0';
    pre.style.whiteSpace = 'pre-wrap';
    mensajeDiv.appendChild(pre);
    
    container.appendChild(mensajeDiv);
}

function agregarTimestamp(container) {
    const timestamp = document.createElement('small');
    timestamp.textContent = `Recibido: ${new Date().toLocaleString()}`;
    timestamp.style.color = '#6c757d';
    timestamp.style.fontStyle = 'italic';
    timestamp.style.display = 'block';
    timestamp.style.marginTop = '10px';
    timestamp.style.textAlign = 'right';
    container.appendChild(timestamp);
}

function agregarSeparador(container) {
    const separador = document.createElement('hr');
    separador.style.margin = '15px 0';
    separador.style.border = 'none';
    separador.style.borderTop = '2px solid #e9ecef';
    container.appendChild(separador);
}

function formatearNombreCampo(campo) {
    const traducciones = {
        'codigoEstudiante': 'Código Estudiante',
        'fechaPrestamo': 'Fecha de Préstamo',
        'fechaDevolucionEstimada': 'Fecha Devolución Estimada',
        'fechaDevolucionReal': 'Fecha Devolución Real',
        'estadoPrestamo': 'Estado del Préstamo',
        'equipoPrestado': 'Equipo Prestado',
        'implementoDeportivo': 'Implemento Deportivo',
        'fechaVencimiento': 'Fecha de Vencimiento',
        'fechaCreacion': 'Fecha de Creación',
        'fechaPago': 'Fecha de Pago',
        'estadoDeuda': 'Estado de la Deuda',
        'montoAdeudado': 'Monto Adeudado',
        'motivoDeuda': 'Motivo de la Deuda',
        'fechaGeneracionDeuda': 'Fecha de Generación',
        'fechaLimitePago': 'Fecha Límite de Pago',
        'monto': 'Monto',
        'concepto': 'Concepto',
        'semestre': 'Semestre',
        'periodo': 'Período',
        'valor': 'Valor',
        'descripcion': 'Descripción',
        'fecha': 'Fecha',
        'tipo': 'Tipo',
        'estado': 'Estado'
    };
    
    return traducciones[campo] || campo.charAt(0).toUpperCase() + campo.slice(1);
}

function formatearValorCampo(campo, valor) {
    if (valor === null || valor === 'null') {
        return {
            texto: 'No registrado',
            color: '#495057',
            negrita: false
        };
    }
    
    switch (campo) {
        case 'estadoPrestamo':
            return {
                texto: valor,
                color: '#495057',
                negrita: true
            };
        case 'fechaPrestamo':
        case 'fechaDevolucionEstimada':
        case 'fechaDevolucionReal':
            return {
                texto: valor,
                color: '#495057',
                negrita: false
            };
        case 'equipoPrestado':
        case 'implementoDeportivo':
            return {
                texto: valor,
                color: '#495057',
                negrita: true
            };
        case 'valor':
        case 'monto':
        case 'montoAdeudado':
            return {
                texto: `$${valor}`,
                color: '#495057',
                negrita: true
            };
        default:
            return {
                texto: valor,
                color: '#495057',
                negrita: false
            };
    }
}

function limpiarNotificacionesDeudas() {
    const referenciaDivMensajes = document.getElementById('divMensajeDeudas');
    referenciaDivMensajes.innerHTML = '';
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

    const url = construirURLEliminacion(areaActual, codigoEstudiante);
    if (!url) {
        return;
    }

    fetch(url, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.text())
    .then(data => {
        document.getElementById('inputEliminarDeuda').value = '';
    })
    .catch(error => {
        console.error('Error al eliminar las deudas:', error);
    });
}

function construirURLEliminacion(area, codigoEstudiante) {
    switch (area) {
        case 'deportes':
            return `http://localhost:5001/api/deudasDeportivo/${codigoEstudiante}`;
        case 'financiera':
            return `http://localhost:5002/api/deudasFinanciera/${codigoEstudiante}`;
        case 'laboratorios':
            return `http://localhost:5003/api/prestamosLaboratorio/${codigoEstudiante}`;
        default:
            return null;
    }
}

document.getElementById('btnEliminarDeudas').addEventListener('click', eliminarDeudas);
document.getElementById('btnLimpiarDeudas').addEventListener('click', limpiarNotificacionesDeudas);

document.addEventListener('DOMContentLoaded', function() {
    setConectado(false);
});

setConectado(false);