package co.edu.unicauca.servicio_laboratorio.capaFachadaServices.services;
import co.edu.unicauca.servicio_laboratorio.capaFachadaServices.DTO.Peticion.PeticionEstudianteDTO;
import co.edu.unicauca.servicio_laboratorio.capaFachadaServices.DTO.Respuesta.RespuestaLaboratorioDTO;

public interface SolicitudesInt {
    public RespuestaLaboratorioDTO solicitarPrestamos(PeticionEstudianteDTO objPeticion);
    public String eliminarPrestamos(int codigoEstudiante);
}
