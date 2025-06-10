package co.edu.unicauca.servicio_deportes.capaControladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class controladorImplementos {
    @Autowired
    private SolicitudesImpl objSolicitudDeudas;
    
    @PostMapping("/deudasDeportivo")
    public RespuestaDeportesDTO solicitarDeudas(@RequestBody PeticionEstudianteDTO objPeticion){
        System.out.println("[SERVICIO-DEPORTES] Servicio consumido: Consulta de deudas deportivas para estudiante " + objPeticion.getCodigoEstudiante());
        return this.objSolicitudDeudas.solicitarDeudas(objPeticion);
    }
    
    @DeleteMapping("/deudasDeportivo/{codigoEstudiante}")
    public String eliminarDeudas(@PathVariable int codigoEstudiante){
        System.out.println("[SERVICIO-DEPORTES] Servicio consumido: Eliminación de deudas deportivas para estudiante " + codigoEstudiante);
        return this.objSolicitudDeudas.eliminarDeudas(codigoEstudiante);
    }
}
