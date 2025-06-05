package co.edu.unicauca.servicio_deportes.capaFachadaServices.services;
import co.edu.unicauca.servicio_deportes.capaFachadaServices.DTO.Peticion.EstudianteDTO;
import co.edu.unicauca.servicio_deportes.capaFachadaServices.DTO.Respuesta.RespuestaDeportesDTO;

public interface SolicitudesInt {
    public RespuestaDeportesDTO solicitarDeudas(EstudianteDTO objPeticion);
    public String eliminarDeudas(int codigoEstudiante);
}
