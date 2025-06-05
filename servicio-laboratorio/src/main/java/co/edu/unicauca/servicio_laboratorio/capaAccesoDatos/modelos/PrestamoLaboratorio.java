package co.edu.unicauca.servicio_laboratorio.capaAccesoDatos.modelos;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PrestamoLaboratorio {
    private int codigoEstudiante;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionEstimada;
    private LocalDate fechaDevolucionReal;
    private EstadoPrestamo estadoPrestamo;
    private String equipoPrestado;

    public PrestamoLaboratorio(int codigoEstudiante, LocalDate fechaPrestamo,
            LocalDate fechaDevolucionEstimada, LocalDate fechaDevolucionReal,
            EstadoPrestamo estadoPrestamo, String equipoPrestado) {
        this.codigoEstudiante = codigoEstudiante;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionEstimada = fechaDevolucionEstimada;
        this.fechaDevolucionReal = fechaDevolucionReal;
        this.estadoPrestamo = estadoPrestamo;
        this.equipoPrestado = equipoPrestado;
    }
}
