package co.edu.unicauca.servicio_orquestador.capaAccesoDatos.modelos;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeudaFinanciera {
    private int codigoEstudiante;
    private double montoAdeudado;
    private String motivoDeuda;
    private LocalDate fechaGeneracionDeuda;
    private LocalDate fechaLimitePago;
    private EstadoDeuda estadoDeuda;
}
