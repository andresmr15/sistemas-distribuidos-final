package co.edu.unicauca.servicio_laboratorio.capaFachadaServices.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unicauca.servicio_laboratorio.capaAccesoDatos.modelos.Estudiante;
import co.edu.unicauca.servicio_laboratorio.capaAccesoDatos.repositorios.RepositorioLaboratorio;
import co.edu.unicauca.servicio_laboratorio.capaFachadaServices.DTO.Peticion.PeticionEstudianteDTO;
import co.edu.unicauca.servicio_laboratorio.capaFachadaServices.DTO.Respuesta.RespuestaLaboratorioDTO;

@Service
public class SolicitudesImpl implements SolicitudesInt {
    @Autowired
    private RepositorioLaboratorio objRepositorioLaboratorio;
    @Autowired
    private ModelMapper modelMapper;

    public RespuestaLaboratorioDTO solicitarPrestamos(PeticionEstudianteDTO objPeticion) {
        RespuestaLaboratorioDTO objRespuesta = new RespuestaLaboratorioDTO();
        Estudiante objUsuarioConvertido = this.modelMapper.map(objPeticion, Estudiante.class);
        objRespuesta.setPrestamos(objRepositorioLaboratorio.consultarPrestamos(objUsuarioConvertido.getCodigoEstudiante()));
        return objRespuesta;
    }

    public String eliminarPrestamos(int codigoEstudiante) {
        return objRepositorioLaboratorio.eliminarPrestamos(codigoEstudiante)
                ? "Los prestamos del área de laboratorios del estudiante con código " + codigoEstudiante
                        + " fueron eliminadas correctamente"
                : "No se encontraron prestamos para este estudiante";
    }
}
