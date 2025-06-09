package co.edu.unicauca.servicio_orquestador.capaFachadaServices.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import co.edu.unicauca.servicio_orquestador.capaFachadaServices.DTO.Respuesta.RespuestaDeportesDTO;
import co.edu.unicauca.servicio_orquestador.capaFachadaServices.DTO.Respuesta.RespuestaFinancieraDTO;
import co.edu.unicauca.servicio_orquestador.capaFachadaServices.DTO.Respuesta.RespuestaLaboratorioDTO;

@Service
public class NotificacionPazYSalvoService {
    
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public void notificarDeudaLaboratorio(int codigoEstudiante, RespuestaLaboratorioDTO respuesta) {
        String mensaje;
        if (respuesta.getPrestamos().isEmpty()) {
            mensaje = String.format("El estudiante %d está a paz y salvo en laboratorios", codigoEstudiante);
        } else {
            mensaje = String.format("El estudiante %d tiene deudas en laboratorios: %s", 
                codigoEstudiante, respuesta.getPrestamos());
        }
        // Notificación privada para el área de laboratorios
        simpMessagingTemplate.convertAndSend("/notificaciones/deudas/laboratorios", mensaje);
    }

    public void notificarDeudaFinanciera(int codigoEstudiante, RespuestaFinancieraDTO respuesta) {
        String mensaje;
        if (respuesta.getDeudas().isEmpty()) {
            mensaje = String.format("El estudiante %d está a paz y salvo en finanzas", codigoEstudiante);
        } else {
            mensaje = String.format("El estudiante %d tiene deudas en finanzas: %s", 
                codigoEstudiante, respuesta.getDeudas());
        }
        // Notificación privada para el área financiera
        simpMessagingTemplate.convertAndSend("/notificaciones/deudas/financiera", mensaje);
    }

    public void notificarDeudaDeportes(int codigoEstudiante, RespuestaDeportesDTO respuesta) {
        String mensaje;
        if (respuesta.getDeudas().isEmpty()) {
            mensaje = String.format("El estudiante %d está a paz y salvo en deportes", codigoEstudiante);
        } else {
            mensaje = String.format("El estudiante %d tiene deudas en deportes: %s", 
                codigoEstudiante, respuesta.getDeudas());
        }
        // Notificación privada para el área de deportes
        simpMessagingTemplate.convertAndSend("/notificaciones/deudas/deportes", mensaje);
    }
} 