package co.edu.unicauca.servicio_deportes.capaFachadaServices.DTO.Respuesta;
import java.util.List;

import co.edu.unicauca.servicio_deportes.capaAccesoDatos.modelos.DeudaImplemento;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RespuestaDeportesDTO {
    private List<DeudaImplemento> deudas;
}
