package co.edu.unicauca.estudiante_clienterest.capaFachadaServices.DTO.respuesta;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeudaImplementoDTO {
    private int codigoEstudiante;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionEstimada;
    private LocalDate fechaDevolucionReal;
    private String implementoDeportivo;
}
