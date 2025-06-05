package co.edu.unicauca.servicio_financiera.capaAccesoDatos.modelos;

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

    public DeudaFinanciera(int codigoEstudiante,double montoAdeudado,
                            String motivoDeuda, LocalDate fechaGeneracionDeuda, 
                            LocalDate fechaLimitePago,EstadoDeuda estadoDeuda) {
        this.codigoEstudiante = codigoEstudiante;
        this.montoAdeudado = montoAdeudado;
        this.motivoDeuda = motivoDeuda;
        this.fechaGeneracionDeuda = fechaGeneracionDeuda;
        this.fechaLimitePago = fechaLimitePago;
        this.estadoDeuda = estadoDeuda;
    }
}
