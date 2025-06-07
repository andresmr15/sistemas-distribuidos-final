package co.edu.unicauca.estudiante_clienterest.capaFachadaServices.servicios;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation. PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import co.edu.unicauca.estudiante_clienterest.FeignConfig;
import co.edu.unicauca.estudiante_clienterest.capaFachadaServices.DTO.peticion.EstudianteDTO;
import co.edu.unicauca.estudiante_clienterest.capaFachadaServices.DTO.respuesta.RespuestaPazYSalvoDTO;

@FeignClient(name="orquestador", url = "http://localhost:5004/api", configuration=FeignConfig.class)
public interface OperacionesClienteInt {
    @PostMapping("/orquestadorSincrono")
    public RespuestaPazYSalvoDTO generarPazYSalvoSincrono (@RequestBody EstudianteDTO objPeticion);
}