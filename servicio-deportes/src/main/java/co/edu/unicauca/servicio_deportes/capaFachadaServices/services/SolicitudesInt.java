package co.edu.unicauca.servicio_deportes.capaFachadaServices.services;
import co.edu.unicauca.servicio_deportes.capaFachadaServices.DTO.Peticion.PeticionEstudianteDTO;
import co.edu.unicauca.servicio_deportes.capaFachadaServices.DTO.Respuesta.RespuestaDeportesDTO;

public interface SolicitudesInt {
    public RespuestaDeportesDTO solicitarDeudas(PeticionEstudianteDTO objPeticion);
    public String eliminarDeudas(int codigoEstudiante);
}
