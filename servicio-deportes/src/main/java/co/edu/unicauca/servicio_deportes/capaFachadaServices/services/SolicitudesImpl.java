package co.edu.unicauca.servicio_deportes.capaFachadaServices.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unicauca.servicio_deportes.capaAccesoDatos.modelos.Estudiante;
import co.edu.unicauca.servicio_deportes.capaAccesoDatos.repositorios.RepositorioImplementos;
import co.edu.unicauca.servicio_deportes.capaFachadaServices.DTO.Peticion.PeticionEstudianteDTO;
import co.edu.unicauca.servicio_deportes.capaFachadaServices.DTO.Respuesta.RespuestaDeportesDTO;

@Service
public class SolicitudesImpl implements SolicitudesInt {
    @Autowired
    private RepositorioImplementos objRepositorioImplementos;
    @Autowired
    private ModelMapper modelMapper;

    public RespuestaDeportesDTO solicitarDeudas(PeticionEstudianteDTO objPeticion) {
        RespuestaDeportesDTO objRespuesta = new RespuestaDeportesDTO();
        Estudiante objUsuarioConvertido = this.modelMapper.map(objPeticion, Estudiante.class);
        objRespuesta.setDeudas(objRepositorioImplementos.consultarDeudas(objUsuarioConvertido.getCodigoEstudiante()));
        return objRespuesta;
    }

    public String eliminarDeudas(int codigoEstudiante) {
        return objRepositorioImplementos.eliminarDeudas(codigoEstudiante)
                ? "Las deudas del área de laboratorios del estudiante con código " + codigoEstudiante
                        + " fueron eliminadas correctamente"
                : "No se encontraron deudas para este estudiante";
    }
}
