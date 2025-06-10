package co.edu.unicauca.servicio_laboratorio.capaControladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class controladorLaboratorio {
    @Autowired
    private SolicitudesImpl objSolicitudDeudas;
    @PostMapping("/prestamosLaboratorio")
    public RespuestaLaboratorioDTO solicitarPrestamos(@RequestBody PeticionEstudianteDTO objPeticion){
        System.out.println("[SERVICIO-LABORATORIO] Servicio consumido: Consulta de préstamos de laboratorio para estudiante " + objPeticion.getCodigoEstudiante());
        return this.objSolicitudDeudas.solicitarPrestamos(objPeticion);
    }
    @DeleteMapping("/prestamosLaboratorio/{codigoEstudiante}")
    public String eliminarDeudas(@PathVariable int codigoEstudiante){
        System.out.println("[SERVICIO-LABORATORIO] Servicio consumido: Eliminación de préstamos de laboratorio para estudiante " + codigoEstudiante);
        return this.objSolicitudDeudas.eliminarPrestamos(codigoEstudiante);
    }
}
