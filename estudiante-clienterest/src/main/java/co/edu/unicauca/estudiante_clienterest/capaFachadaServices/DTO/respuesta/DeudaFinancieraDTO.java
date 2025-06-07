package co.edu.unicauca.estudiante_clienterest.capaFachadaServices.DTO.respuesta;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeudaFinancieraDTO {
    private int codigoEstudiante;
    private double montoAdeudado;
    private String motivoDeuda;
    private LocalDate fechaGeneracionDeuda;
    private LocalDate fechaLimitePago;
    private EstadoDeudaDTO estadoDeuda;
}
