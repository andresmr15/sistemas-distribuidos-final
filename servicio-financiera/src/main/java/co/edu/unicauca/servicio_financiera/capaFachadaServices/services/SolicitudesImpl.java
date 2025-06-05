package co.edu.unicauca.servicio_financiera.capaFachadaServices.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.unicauca.servicio_financiera.capaAccesoDatos.modelos.Estudiante;
import co.edu.unicauca.servicio_financiera.capaAccesoDatos.repositorios.RepositorioFinanciera;
import co.edu.unicauca.servicio_financiera.capaFachadaServices.DTO.Peticion.EstudianteDTO;
import co.edu.unicauca.servicio_financiera.capaFachadaServices.DTO.Respuesta.RespuestaFinancieraDTO;

@Service
public class SolicitudesImpl implements SolicitudesInt {
    @Autowired
    private RepositorioFinanciera objRepositorioFinanciera;
    @Autowired
    private ModelMapper modelMapper;

    public RespuestaFinancieraDTO solicitarDeudas(EstudianteDTO objPeticion) {
        RespuestaFinancieraDTO objRespuesta = new RespuestaFinancieraDTO();
        Estudiante objUsuarioConvertido = this.modelMapper.map(objPeticion, Estudiante.class);
        objRespuesta.setDeudas(objRepositorioFinanciera.consultarDeudas(objUsuarioConvertido.getCodigoEstudiante()));
        return objRespuesta;
    }

    public String eliminarDeudas(int codigoEstudiante) {
        return objRepositorioFinanciera.eliminarDeudas(codigoEstudiante)
                ? "Las deudas del área de laboratorios del estudiante con código " + codigoEstudiante
                        + " fueron eliminadas correctamente"
                : "No se encontraron deudas para este estudiante";
    }
}
