package co.edu.unicauca.servicio_orquestador.capaFachadaServices.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import co.edu.unicauca.servicio_orquestador.capaAccesoDatos.modelos.Estudiante;
import co.edu.unicauca.servicio_orquestador.capaFachadaServices.DTO.Peticion.PeticionEstudianteDTO;
import co.edu.unicauca.servicio_orquestador.capaFachadaServices.DTO.Respuesta.RespuestaDeportesDTO;
import co.edu.unicauca.servicio_orquestador.capaFachadaServices.DTO.Respuesta.RespuestaFinancieraDTO;
import co.edu.unicauca.servicio_orquestador.capaFachadaServices.DTO.Respuesta.RespuestaLaboratorioDTO;
import co.edu.unicauca.servicio_orquestador.capaFachadaServices.DTO.Respuesta.RespuestaPazYSalvoDTO;
import reactor.core.publisher.Mono;

@Service
public class GenerarPazYSalvoImpl implements GenerarPazYSalvoInt {
    @Autowired
    private WebClient webClient;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private NotificacionPazYSalvoService notificacionService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public RespuestaPazYSalvoDTO generarPazYSalvoSincrono(PeticionEstudianteDTO objPeticion) {
        System.out.println("Iniciando el proceso de generación de paz y salvo... de manera sincrona");
        RespuestaPazYSalvoDTO objRespuestaPazYSalvo = new RespuestaPazYSalvoDTO();
        Estudiante objPeticionConvertida = this.modelMapper.map(objPeticion, Estudiante.class);

        // Notificar la nueva solicitud
        String mensajeSolicitud = String.format("El estudiante con código %d ha realizado una nueva solicitud de paz y salvo", 
            objPeticionConvertida.getCodigoEstudiante());
        simpMessagingTemplate.convertAndSend("/notificaciones/solicitud", mensajeSolicitud);

        try {
            String urlServicioDeportes = "http://localhost:5001/api/deudasDeportivo";
            RespuestaDeportesDTO objRespuestaDeportes = webClient.post()
                    .uri(urlServicioDeportes)
                    .bodyValue(objPeticionConvertida)
                    .retrieve()
                    .bodyToMono(RespuestaDeportesDTO.class)
                    .block();
            objRespuestaPazYSalvo.setObjDeportes(objRespuestaDeportes);
            notificacionService.notificarDeudaDeportes(objPeticionConvertida.getCodigoEstudiante(), objRespuestaDeportes);

            String urlServicioFinanciera = "http://localhost:5002/api/deudasFinanciera";
            RespuestaFinancieraDTO objRespuestaFinanciera = webClient.post()
                    .uri(urlServicioFinanciera)
                    .bodyValue(objPeticionConvertida)
                    .retrieve()
                    .bodyToMono(RespuestaFinancieraDTO.class)
                    .block();
            objRespuestaPazYSalvo.setObjFinanciera(objRespuestaFinanciera);
            notificacionService.notificarDeudaFinanciera(objPeticionConvertida.getCodigoEstudiante(), objRespuestaFinanciera);

            String urlServicioLaboratorio = "http://localhost:5003/api/prestamosLaboratorio";
            RespuestaLaboratorioDTO objRespuestaLaboratorio = webClient.post()
                    .uri(urlServicioLaboratorio)
                    .bodyValue(objPeticionConvertida)
                    .retrieve()
                    .bodyToMono(RespuestaLaboratorioDTO.class)
                    .block();
            objRespuestaPazYSalvo.setObjLaboratorio(objRespuestaLaboratorio);
            notificacionService.notificarDeudaLaboratorio(objPeticionConvertida.getCodigoEstudiante(), objRespuestaLaboratorio);

            if (objRespuestaDeportes.getDeudas().isEmpty() && objRespuestaFinanciera.getDeudas().isEmpty()
                    && objRespuestaLaboratorio.getPrestamos().isEmpty()) {
                objRespuestaPazYSalvo
                        .setMensaje("El estudiante con código "
                                + objPeticionConvertida.getCodigoEstudiante() + " está a paz y salvo");
            } else {
                objRespuestaPazYSalvo.setMensaje("El estudiante no está a paz y salvo");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            objRespuestaPazYSalvo.setMensaje("Error al generar paz y salvo");
        }

        return objRespuestaPazYSalvo;
    }

    @Override
    public Mono<RespuestaPazYSalvoDTO> generarPazYSalvoAsincrono(PeticionEstudianteDTO objPeticion) {
        System.out.println("Iniciando el proceso de generación de paz y salvo... de manera asincrona");
        RespuestaPazYSalvoDTO objRespuestaPazYSalvo = new RespuestaPazYSalvoDTO();
        Estudiante objPeticionConvertida = this.modelMapper.map(objPeticion, Estudiante.class);

        // Notificar la nueva solicitud
        String mensajeSolicitud = String.format("El estudiante con código %d ha realizado una nueva solicitud de paz y salvo", 
            objPeticionConvertida.getCodigoEstudiante());
        simpMessagingTemplate.convertAndSend("/notificaciones/solicitud", mensajeSolicitud);

        String urlServicioDeportes = "http://localhost:5001/api/deudasDeportivo";
        Mono<RespuestaDeportesDTO> objRespuestaDeportes = webClient.post()
                .uri(urlServicioDeportes)
                .bodyValue(objPeticionConvertida)
                .retrieve()
                .bodyToMono(RespuestaDeportesDTO.class)
                .doOnNext(respuesta -> 
                    notificacionService.notificarDeudaDeportes(objPeticionConvertida.getCodigoEstudiante(), respuesta))
                .doOnError(e -> System.err
                        .println("Error generando el paz y salvo del área de deportes: " + e.getMessage()));

        String urlServicioFinanciera = "http://localhost:5002/api/deudasFinanciera";
        Mono<RespuestaFinancieraDTO> objRespuestaFinanciera = webClient.post()
                .uri(urlServicioFinanciera)
                .bodyValue(objPeticionConvertida)
                .retrieve()
                .bodyToMono(RespuestaFinancieraDTO.class)
                .doOnNext(respuesta -> 
                    notificacionService.notificarDeudaFinanciera(objPeticionConvertida.getCodigoEstudiante(), respuesta))
                .doOnError(e -> System.err
                        .println("Error generando el paz y salvo del área financiera" + e.getMessage()));

        String urlServicioLaboratorio = "http://localhost:5003/api/prestamosLaboratorio";
        Mono<RespuestaLaboratorioDTO> objRespuestaLaboratorio = webClient.post()
                .uri(urlServicioLaboratorio)
                .bodyValue(objPeticionConvertida)
                .retrieve()
                .bodyToMono(RespuestaLaboratorioDTO.class)
                .doOnNext(respuesta -> 
                    notificacionService.notificarDeudaLaboratorio(objPeticionConvertida.getCodigoEstudiante(), respuesta))
                .doOnError(e -> System.err
                        .println("Error generando el paz y salvo del área de laboratorio" + e.getMessage()));

        return Mono.zip(objRespuestaDeportes, objRespuestaFinanciera, objRespuestaLaboratorio)
                .map(results -> {
                    objRespuestaPazYSalvo.setObjDeportes(results.getT1());
                    objRespuestaPazYSalvo.setObjFinanciera(results.getT2());
                    objRespuestaPazYSalvo.setObjLaboratorio(results.getT3());
                    verificarPazYSalvoAsincrono(objPeticionConvertida, objRespuestaPazYSalvo);
                    return objRespuestaPazYSalvo;
                }).onErrorResume(error -> {
                    RespuestaPazYSalvoDTO respuesta = new RespuestaPazYSalvoDTO();
                    respuesta.setMensaje("Error al generar el paz y salvo");
                    return Mono.just(respuesta);
                });
    }

    private void verificarPazYSalvoAsincrono(Estudiante objPeticionConvertida, RespuestaPazYSalvoDTO objRespuestaPazYSalvo) {
        if (objRespuestaPazYSalvo.getObjDeportes().getDeudas().isEmpty()
                && objRespuestaPazYSalvo.getObjFinanciera().getDeudas().isEmpty()
                && objRespuestaPazYSalvo.getObjLaboratorio().getPrestamos().isEmpty()) {
            objRespuestaPazYSalvo
                    .setMensaje("El estudiante con código "
                            + objPeticionConvertida.getCodigoEstudiante() + " está a paz y salvo");
        } else {
            objRespuestaPazYSalvo.setMensaje("El estudiante no está a paz y salvo");
        }
    }
}
