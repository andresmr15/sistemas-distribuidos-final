package co.edu.unicauca.servicio_orquestador.capaFachadaServices.DTO.Respuesta;

import lombok.Data;

@Data
public class RespuestaPazYSalvoDTO {
    private RespuestaDeportesDTO objDeportes;
    private RespuestaFinancieraDTO objFinanciera;
    private RespuestaLaboratorioDTO objLaboratorio;
    private String mensaje;
}
