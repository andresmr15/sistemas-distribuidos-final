package co.edu.unicauca.estudiante_clienterest.capaFachadaServices.DTO.respuesta;

import lombok.Data;

@Data
public class RespuestaPazYSalvoDTO {
    private RespuestaDeportesDTO objDeportes;
    private RespuestaFinancieraDTO objFinanciera;
    private RespuestaLaboratorioDTO objLaboratorio;
    private String mensaje;
}
