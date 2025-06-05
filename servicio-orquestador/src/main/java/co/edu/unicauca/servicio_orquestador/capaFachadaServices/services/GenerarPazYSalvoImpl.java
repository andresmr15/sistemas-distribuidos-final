package co.edu.unicauca.servicio_orquestador.capaFachadaServices.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import co.edu.unicauca.servicio_orquestador.capaAccesoDatos.modelos.Estudiante;
import co.edu.unicauca.servicio_orquestador.capaFachadaServices.DTO.Peticion.EstudianteDTO;
import co.edu.unicauca.servicio_orquestador.capaFachadaServices.DTO.Respuesta.RespuestaPazYSalvoDTO;
import reactor.core.publisher.Mono;
import co.edu.unicauca.servicio_orquestador.capaFachadaServices.DTO.Respuesta.RespuestaDeportesDTO;
import co.edu.unicauca.servicio_orquestador.capaFachadaServices.DTO.Respuesta.RespuestaFinancieraDTO;
import co.edu.unicauca.servicio_orquestador.capaFachadaServices.DTO.Respuesta.RespuestaLaboratorioDTO;

@Service
public class GenerarPazYSalvoImpl implements GenerarPazYSalvoInt {
    @Autowired
    private WebClient webClient;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public RespuestaPazYSalvoDTO generarPazYSalvoSincrono(EstudianteDTO objPeticion) {

        System.out.println("Iniciando el proceso de generación de paz y salvo... de manera sincrona");
        RespuestaPazYSalvoDTO objRespuestaPazYSalvo = new RespuestaPazYSalvoDTO();
        Estudiante objPeticionConvertida = this.modelMapper.map(objPeticion, Estudiante.class);

        try {
            String urlServicioDeportes = "http://localhost:5001/api/deudasDeportivo";
            RespuestaDeportesDTO objRespuestaDeportes = webClient.post()
                    .uri(urlServicioDeportes)
                    .bodyValue(objPeticionConvertida)
                    .retrieve()
                    .bodyToMono(RespuestaDeportesDTO.class)
                    .block();
            objRespuestaPazYSalvo.setObjDeportes(objRespuestaDeportes);

            String urlServicioFinanciera = "http://localhost:5002/api/deudasFinanciera";
            RespuestaFinancieraDTO objRespuestaFinanciera = webClient.post()
                    .uri(urlServicioFinanciera)
                    .bodyValue(objPeticionConvertida)
                    .retrieve()
                    .bodyToMono(RespuestaFinancieraDTO.class)
                    .block();
            objRespuestaPazYSalvo.setObjFinanciera(objRespuestaFinanciera);

            String urlServicioLaboratorio = "http://localhost:5003/api/prestamosLaboratorio";
            RespuestaLaboratorioDTO objRespuestaLaboratorio = webClient.post()
                    .uri(urlServicioLaboratorio)
                    .bodyValue(objPeticionConvertida)
                    .retrieve()
                    .bodyToMono(RespuestaLaboratorioDTO.class)
                    .block();
            objRespuestaPazYSalvo.setObjLaboratorio(objRespuestaLaboratorio);

            if (objRespuestaDeportes.getDeudas().isEmpty() && objRespuestaFinanciera.getDeudas().isEmpty()
                    && objRespuestaLaboratorio.getPrestamos().isEmpty()) {
                objRespuestaPazYSalvo
                        .setMensaje("El estudiante " + objPeticionConvertida.getNombreEstudiante() + " con código "
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
    public Mono<RespuestaPazYSalvoDTO> generarPazYSalvoAsincrono(EstudianteDTO objPeticion) {
        System.out.println("Iniciando el proceso de generación de paz y salvo... de manera asincrona");
        RespuestaPazYSalvoDTO objRespuestaPazYSalvo = new RespuestaPazYSalvoDTO();
        Estudiante objPeticionConvertida = this.modelMapper.map(objPeticion, Estudiante.class);

        String urlServicioDeportes = "http://localhost:5001/api/deudasDeportivo";
        Mono<RespuestaDeportesDTO> objRespuestaDeportes = webClient.post()
                .uri(urlServicioDeportes)
                .bodyValue(objPeticionConvertida)
                .retrieve()
                .bodyToMono(RespuestaDeportesDTO.class)
                .doOnError(e -> System.err
                        .println("Error generando el paz y salvo del área de deportes: " + e.getMessage())); // Síncrono

        String urlServicioFinanciera = "http://localhost:5002/api/deudasFinanciera";
        Mono<RespuestaFinancieraDTO> objRespuestaFinanciera = webClient.post()
                .uri(urlServicioFinanciera)
                .bodyValue(objPeticionConvertida)
                .retrieve()
                .bodyToMono(RespuestaFinancieraDTO.class)
                .doOnError(e -> System.err
                        .println("Error generando el paz y salvo del área financiera" + e.getMessage()));

        String urlServicioLaboratorio = "http://localhost:5003/api/prestamosLaboratorio";
        Mono<RespuestaLaboratorioDTO> objRespuestaLaboratorio = webClient.post()
                .uri(urlServicioLaboratorio)
                .bodyValue(objPeticionConvertida)
                .retrieve()
                .bodyToMono(RespuestaLaboratorioDTO.class)
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
                    .setMensaje("El estudiante " + objPeticionConvertida.getNombreEstudiante() + " con código "
                            + objPeticionConvertida.getCodigoEstudiante() + " está a paz y salvo");
        } else {
            objRespuestaPazYSalvo.setMensaje("El estudiante no está a paz y salvo");
        }
    }
}
