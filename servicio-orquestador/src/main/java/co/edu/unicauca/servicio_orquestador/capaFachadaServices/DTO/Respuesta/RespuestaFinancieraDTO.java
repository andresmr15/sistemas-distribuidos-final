package co.edu.unicauca.servicio_orquestador.capaFachadaServices.DTO.Respuesta;
import java.util.List;

import co.edu.unicauca.servicio_orquestador.capaAccesoDatos.modelos.DeudaFinanciera;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RespuestaFinancieraDTO {
    private List<DeudaFinanciera> deudas;
}
