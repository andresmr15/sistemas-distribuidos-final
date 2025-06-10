package co.edu.unicauca.estudiante_clienterest.capaFachadaServices.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import co.edu.unicauca.estudiante_clienterest.capaFachadaServices.DTO.peticion.PeticionEstudianteDTO;
import co.edu.unicauca.estudiante_clienterest.capaFachadaServices.DTO.respuesta.RespuestaPazYSalvoDTO;
import feign.FeignException;

@Service
public class OperacionesClienteImpl {
    @Autowired
    private OperacionesClienteInt request;

    @Retryable(value = { FeignException.class }, maxAttempts = 3, // maximo de reintentos
            backoff = @Backoff(delay = 3000))
    public RespuestaPazYSalvoDTO generarPazYSalvoSincrono(@RequestBody PeticionEstudianteDTO objPeticion) {
        System.out.println("Intentando obtener paz y salvo de manera sincrona...");
        return request.generarPazYSalvoSincrono(objPeticion);
    }

    public RespuestaPazYSalvoDTO generarPazYSalvoAsincrono(@RequestBody PeticionEstudianteDTO objPeticion) {
        System.out.println("Intentando obtener paz y salvo de manera asincrona...");
        return request.generarPazYSalvoAsincrono(objPeticion);
    }

    @Recover
    public RespuestaPazYSalvoDTO recuperar(FeignException e, PeticionEstudianteDTO objPeticion) {
        System.out.println("Todos los reintentos fallaron. Volviendo al menú principal...");
        RespuestaPazYSalvoDTO respuesta = new RespuestaPazYSalvoDTO();
        respuesta.setMensaje("Error: No se pudo procesar la solicitud después de varios intentos. Por favor, intente más tarde.");
        return respuesta;
    }
}