package co.edu.unicauca.servicio_orquestador.capaFachadaServices.DTO.Respuesta;
import java.util.List;

import co.edu.unicauca.servicio_orquestador.capaAccesoDatos.modelos.PrestamoLaboratorio;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RespuestaLaboratorioDTO {
    private List<PrestamoLaboratorio> prestamos;
}
