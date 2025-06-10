package co.edu.unicauca.servicio_orquestador.capaControladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unicauca.servicio_orquestador.capaFachadaServices.DTO.Peticion.PeticionEstudianteDTO;
import co.edu.unicauca.servicio_orquestador.capaFachadaServices.DTO.Respuesta.RespuestaPazYSalvoDTO;
import co.edu.unicauca.servicio_orquestador.capaFachadaServices.services.GenerarPazYSalvoInt;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class PazYSalvoController {
    @Autowired
    private GenerarPazYSalvoInt objFachada;
    
    @Autowired
    private ContadorFallos contadorFallos;
    
    @PostMapping("/orquestadorSincrono")
    public RespuestaPazYSalvoDTO orquestarServiciosSincronamente(@RequestBody PeticionEstudianteDTO objPeticion){
       // simularFallo();
        RespuestaPazYSalvoDTO objResultado = this.objFachada.generarPazYSalvoSincrono(objPeticion);
        return objResultado;
    }
    @PostMapping("/orquestadorAsincrono")
    public Mono<RespuestaPazYSalvoDTO> orquestadorServicionsAsincronamente(@RequestBody PeticionEstudianteDTO objPeticion){
        Mono<RespuestaPazYSalvoDTO> objResultado = this.objFachada.generarPazYSalvoAsincrono(objPeticion);
        return objResultado;
    }

    private void simularFallo(){
        int intento = contadorFallos.siguienteIntento();
        if (intento < 3) {
            try {
                System.out.println("Simulando fallo...");
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } 
        
        throw new RuntimeException("Error simulado");
    }
}
