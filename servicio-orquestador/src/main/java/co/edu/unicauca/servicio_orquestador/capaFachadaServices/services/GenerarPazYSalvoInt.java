package co.edu.unicauca.servicio_orquestador.capaFachadaServices.services;

import co.edu.unicauca.servicio_orquestador.capaFachadaServices.DTO.Peticion.EstudianteDTO;
import co.edu.unicauca.servicio_orquestador.capaFachadaServices.DTO.Respuesta.RespuestaPazYSalvoDTO;
import reactor.core.publisher.Mono;

public interface GenerarPazYSalvoInt {
    public RespuestaPazYSalvoDTO generarPazYSalvoSincrono(EstudianteDTO objPeticion);
    public Mono<RespuestaPazYSalvoDTO> generarPazYSalvoAsincrono(EstudianteDTO objPeticion);
}
