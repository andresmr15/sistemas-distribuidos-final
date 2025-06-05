package co.edu.unicauca.servicio_deportes.capaAccesoDatos.modelos;

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

    public DeudaImplemento(int codigoEstudiante, LocalDate fechaPrestamo, LocalDate fechaDevolucionEstimada,
            LocalDate fechaDevolucionReal, String implementoDeportivo) {
        this.codigoEstudiante = codigoEstudiante;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionEstimada = fechaDevolucionEstimada;
        this.fechaDevolucionReal = fechaDevolucionReal;
        this.implementoDeportivo = implementoDeportivo;
    }
}
