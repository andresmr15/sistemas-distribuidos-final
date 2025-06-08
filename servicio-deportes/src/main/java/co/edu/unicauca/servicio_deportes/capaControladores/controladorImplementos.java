package co.edu.unicauca.servicio_deportes.capaControladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unicauca.servicio_deportes.capaFachadaServices.DTO.Peticion.PeticionEstudianteDTO;
import co.edu.unicauca.servicio_deportes.capaFachadaServices.DTO.Respuesta.RespuestaDeportesDTO;
import co.edu.unicauca.servicio_deportes.capaFachadaServices.services.SolicitudesImpl;

@RestController
@RequestMapping("/api")
public class controladorImplementos {
    @Autowired
    private SolicitudesImpl objSolicitudDeudas;
    @PostMapping("/deudasDeportivo")
    public RespuestaDeportesDTO solicitarDeudas(@RequestBody PeticionEstudianteDTO objPeticion){
        return this.objSolicitudDeudas.solicitarDeudas(objPeticion);
    }
    @DeleteMapping("/deudasDeportivo/{codigoEstudiante}")
    public String eliminarDeudas(@PathVariable int codigoEstudiante){
        return this.objSolicitudDeudas.eliminarDeudas(codigoEstudiante);
    }
}
