let clienteServicios = null;
let areaActual = null;

function setConectado(conectado) {
  // Deshabilitar botones de conexión cuando está conectado
  document.getElementById('btnConectarAreaDeportes').disabled = conectado;
  document.getElementById('btnConectarAreaFinanciera').disabled = conectado;
  document.getElementById('btnConectarAreaLaboratorio').disabled = conectado;
  
  // Habilitar botón de desconexión cuando está conectado
  document.getElementById('btnDesconectar').disabled = !conectado;
  
  // Habilitar botón de eliminar deudas cuando está conectado
  document.getElementById('btnEliminarDeudas').disabled = !conectado;
}

function conectar(area) {
  if (clienteServicios !== null) {
    desconectar();
  }
  
  const socket = new SockJS('http://localhost:5004/ws');
  clienteServicios = Stomp.over(socket);
  areaActual = area;

  const headers = {
    'Access-Control-Allow-Origin': '*',
    'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, OPTIONS',
    'Access-Control-Allow-Headers': 'Content-Type, Authorization'
  };

  clienteServicios.connect(
    {},
    headers,
    () => {
      console.log('Conectado al servidor WebSocket');
      suscripcionAChat(area);
    },
    (error) => {
      console.error('Error al conectar:', error);
      alert('Error al conectar con el servidor');
      setConectado(false);
    }
  );
}

function desconectar() {
  if (clienteServicios !== null) {
    clienteServicios.disconnect(() => {          
      console.log('Desconectado del servidor WebSocket');
      setConectado(false);
      areaActual = null;
      
      // Limpiar mensajes
      document.getElementById('divMensajeSolicitud').innerHTML = '';
      document.getElementById('divMensajeDeudas').innerHTML = '';
    });
    clienteServicios = null;
  }
}

function suscripcionAChat(area) {     
  console.log('Suscribiendo a canales para área:', area);
  
  // Suscripción a las notificaciones de paz y salvo
  clienteServicios.subscribe('/topic/pazysalvo/solicitudes', (message) => {
    console.log('Nueva solicitud de paz y salvo recibida:', message);
    recibirSolicitudPazYSalvo(message);
  });

  // Suscripción a las respuestas específicas de cada área
  clienteServicios.subscribe(`/topic/pazysalvo/respuestas/${area}`, (message) => {
    console.log('Nueva respuesta de área recibida:', message);
    recibirRespuestaArea(message);
  });

  setConectado(true);
}

function recibirSolicitudPazYSalvo(message) {
  try {
    const data = JSON.parse(message.body);
    const referenciaDivMensajes = document.getElementById('divMensajeSolicitud');
    const nuevoParrafo = document.createElement('p');
    nuevoParrafo.className = 'estado-mensaje';
    nuevoParrafo.textContent = `El estudiante con código ${data.codigo} y nombres ${data.nombres} ha realizado una nueva solicitud de paz y salvo`;
    referenciaDivMensajes.appendChild(nuevoParrafo);
    console.log('Mensaje de solicitud mostrado correctamente');
  } catch (error) {
    console.error('Error al procesar mensaje de solicitud:', error);
  }
}

function recibirRespuestaArea(message) {
  try {
    const data = JSON.parse(message.body);
    const referenciaDivMensajes = document.getElementById('divMensajeDeudas');
    referenciaDivMensajes.innerHTML = ''; // Limpiar mensajes anteriores

    // Crear mensaje de estado
    const estadoParrafo = document.createElement('p');
    estadoParrafo.className = `estado-mensaje ${data.estado === 'APROBADO' ? 'estado-exito' : 'estado-error'}`;
    estadoParrafo.textContent = `Estado: ${data.estado === 'APROBADO' ? 'A Paz y Salvo' : 'No A Paz y Salvo'}`;
    referenciaDivMensajes.appendChild(estadoParrafo);

    // Si hay deudas, mostrarlas
    if (data.deudas && data.deudas.length > 0) {
      const deudasTitulo = document.createElement('p');
      deudasTitulo.textContent = 'Deudas pendientes:';
      referenciaDivMensajes.appendChild(deudasTitulo);

      data.deudas.forEach(deuda => {
        const deudaParrafo = document.createElement('p');
        deudaParrafo.className = 'estado-mensaje estado-error';
        deudaParrafo.textContent = `- ${deuda.descripcion}: ${deuda.valor}`;
        referenciaDivMensajes.appendChild(deudaParrafo);
      });
    }
    console.log('Mensaje de respuesta mostrado correctamente');
  } catch (error) {
    console.error('Error al procesar mensaje de respuesta:', error);
  }
}

function enviarMensajePrivadoServidor() {
  if (clienteServicios && clienteServicios.connected && areaActual) {
    const mensaje = {
      area: areaActual,
      tipo: 'PAGO_DEUDA',
      timestamp: new Date().toISOString()
    };

    clienteServicios.send("/app/procesarPago", {}, JSON.stringify(mensaje));
    console.log('Mensaje de pago enviado:', mensaje);
    
    // Limpiar la lista de deudas después de procesar el pago
    const referenciaDivMensajes = document.getElementById('divMensajeDeudas');
    referenciaDivMensajes.innerHTML = '';
  } else {
    alert("No estás conectado a ninguna área.");
  }
}