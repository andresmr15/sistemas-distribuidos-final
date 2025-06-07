package co.edu.unicauca.estudiante_clienterest.capaFachadaServices.DTO.respuesta;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RespuestaDeportesDTO {
    private List<DeudaImplementoDTO> deudas;
}
