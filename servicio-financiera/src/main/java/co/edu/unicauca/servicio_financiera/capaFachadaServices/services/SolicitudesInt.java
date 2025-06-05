package co.edu.unicauca.servicio_financiera.capaFachadaServices.services;
import co.edu.unicauca.servicio_financiera.capaFachadaServices.DTO.Peticion.EstudianteDTO;
import co.edu.unicauca.servicio_financiera.capaFachadaServices.DTO.Respuesta.RespuestaFinancieraDTO;

public interface SolicitudesInt {
    public RespuestaFinancieraDTO solicitarDeudas(EstudianteDTO objPeticion);
    public String eliminarDeudas(int codigoEstudiante);
}
