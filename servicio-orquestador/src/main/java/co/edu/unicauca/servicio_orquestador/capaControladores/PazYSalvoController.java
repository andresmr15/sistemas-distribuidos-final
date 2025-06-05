package co.edu.unicauca.servicio_orquestador.capaControladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unicauca.servicio_orquestador.capaFachadaServices.DTO.Peticion.EstudianteDTO;
import co.edu.unicauca.servicio_orquestador.capaFachadaServices.DTO.Respuesta.RespuestaPazYSalvoDTO;
import co.edu.unicauca.servicio_orquestador.capaFachadaServices.services.GenerarPazYSalvoInt;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class PazYSalvoController {
    @Autowired
    private GenerarPazYSalvoInt objFachada;
    
    @PostMapping("/orquestadorSincrono")
    public RespuestaPazYSalvoDTO orquestarServiciosSincronamente(@RequestBody EstudianteDTO objPeticion){
        RespuestaPazYSalvoDTO objResultado = this.objFachada.generarPazYSalvoSincrono(objPeticion);
        return objResultado;
    }
    @PostMapping("/orquestadorAsincrono")
    public Mono<RespuestaPazYSalvoDTO> orquestadorServicionsAsincronamente(@RequestBody EstudianteDTO objPeticion){
        Mono<RespuestaPazYSalvoDTO> objResultado = this.objFachada.generarPazYSalvoAsincrono(objPeticion);
        return objResultado;
    }
}
