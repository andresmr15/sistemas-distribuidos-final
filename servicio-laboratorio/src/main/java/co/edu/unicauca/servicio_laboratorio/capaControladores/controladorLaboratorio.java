package co.edu.unicauca.servicio_laboratorio.capaControladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unicauca.servicio_laboratorio.capaFachadaServices.DTO.Peticion.PeticionEstudianteDTO;
import co.edu.unicauca.servicio_laboratorio.capaFachadaServices.DTO.Respuesta.RespuestaLaboratorioDTO;
import co.edu.unicauca.servicio_laboratorio.capaFachadaServices.services.SolicitudesImpl;

@RestController
@RequestMapping("/api")
public class controladorLaboratorio {
    @Autowired
    private SolicitudesImpl objSolicitudDeudas;
    @PostMapping("/prestamosLaboratorio")
    public RespuestaLaboratorioDTO solicitarPrestamos(@RequestBody PeticionEstudianteDTO objPeticion){
        return this.objSolicitudDeudas.solicitarPrestamos(objPeticion);
    }
    @DeleteMapping("/prestamosLaboratorio/{codigoEstudiante}")
    public String eliminarDeudas(@PathVariable int codigoEstudiante){
        return this.objSolicitudDeudas.eliminarPrestamos(codigoEstudiante);
    }
}
