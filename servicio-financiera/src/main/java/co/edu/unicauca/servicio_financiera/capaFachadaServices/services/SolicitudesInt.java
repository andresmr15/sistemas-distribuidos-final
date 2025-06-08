package co.edu.unicauca.servicio_financiera.capaFachadaServices.services;
import co.edu.unicauca.servicio_financiera.capaFachadaServices.DTO.Peticion.PeticionEstudianteDTO;
import co.edu.unicauca.servicio_financiera.capaFachadaServices.DTO.Respuesta.RespuestaFinancieraDTO;

public interface SolicitudesInt {
    public RespuestaFinancieraDTO solicitarDeudas(PeticionEstudianteDTO objPeticion);
    public String eliminarDeudas(int codigoEstudiante);
}
