package co.edu.unicauca.servicio_orquestador.capaFachadaServices.services;

import co.edu.unicauca.servicio_orquestador.capaFachadaServices.DTO.Peticion.PeticionEstudianteDTO;
import co.edu.unicauca.servicio_orquestador.capaFachadaServices.DTO.Respuesta.RespuestaPazYSalvoDTO;
import reactor.core.publisher.Mono;

public interface GenerarPazYSalvoInt {
    public RespuestaPazYSalvoDTO generarPazYSalvoSincrono(PeticionEstudianteDTO objPeticion);
    public Mono<RespuestaPazYSalvoDTO> generarPazYSalvoAsincrono(PeticionEstudianteDTO objPeticion);
}
