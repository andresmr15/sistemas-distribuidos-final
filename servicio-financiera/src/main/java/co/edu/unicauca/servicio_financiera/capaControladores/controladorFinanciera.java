package co.edu.unicauca.servicio_financiera.capaControladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unicauca.servicio_financiera.capaFachadaServices.DTO.Peticion.PeticionEstudianteDTO;
import co.edu.unicauca.servicio_financiera.capaFachadaServices.DTO.Respuesta.RespuestaFinancieraDTO;
import co.edu.unicauca.servicio_financiera.capaFachadaServices.services.SolicitudesImpl;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class controladorFinanciera {
    @Autowired
    private SolicitudesImpl objSolicitudDeudas;
    
    @PostMapping("/deudasFinanciera")
    public RespuestaFinancieraDTO solicitarDeudas(@RequestBody PeticionEstudianteDTO objPeticion){
        System.out.println("[SERVICIO-FINANCIERA] Servicio consumido: Consulta de deudas financieras para estudiante " + objPeticion.getCodigoEstudiante());
        return this.objSolicitudDeudas.solicitarDeudas(objPeticion);
    }
    
    @DeleteMapping("/deudasFinanciera/{codigoEstudiante}")
    public String eliminarDeudas(@PathVariable int codigoEstudiante){
        System.out.println("[SERVICIO-FINANCIERA] Servicio consumido: Eliminación de deudas financieras para estudiante " + codigoEstudiante);
        return this.objSolicitudDeudas.eliminarDeudas(codigoEstudiante);
    }
}
