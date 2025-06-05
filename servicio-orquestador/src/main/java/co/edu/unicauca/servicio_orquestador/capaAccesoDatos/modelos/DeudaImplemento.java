package co.edu.unicauca.servicio_orquestador.capaAccesoDatos.modelos;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeudaImplemento {
    private int codigoEstudiante;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionEstimada;
    private LocalDate fechaDevolucionReal;
    private String implementoDeportivo;
}
