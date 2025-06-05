package co.edu.unicauca.servicio_laboratorio.capaFachadaServices.services;
import co.edu.unicauca.servicio_laboratorio.capaFachadaServices.DTO.Peticion.EstudianteDTO;
import co.edu.unicauca.servicio_laboratorio.capaFachadaServices.DTO.Respuesta.RespuestaLaboratorioDTO;

public interface SolicitudesInt {
    public RespuestaLaboratorioDTO solicitarPrestamos(EstudianteDTO objPeticion);
    public String eliminarPrestamos(int codigoEstudiante);
}
