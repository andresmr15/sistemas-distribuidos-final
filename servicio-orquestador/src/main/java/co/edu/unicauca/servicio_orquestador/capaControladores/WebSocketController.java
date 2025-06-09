package co.edu.unicauca.servicio_orquestador.capaControladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import co.edu.unicauca.servicio_orquestador.capaFachadaServices.DTO.Peticion.PeticionEstudianteDTO;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/solicitudPazYSalvo")
    public void notificarSolicitud(PeticionEstudianteDTO peticion) {
        // Notificación global de nueva solicitud
        String mensaje = String.format("El estudiante con código %d ha realizado una nueva solicitud de paz y salvo", 
            peticion.getCodigoEstudiante());
        simpMessagingTemplate.convertAndSend("/notificaciones/solicitud", mensaje);
    }
} 