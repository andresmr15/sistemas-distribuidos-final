package co.edu.unicauca.servicio_financiera.capaAccesoDatos.repositorios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

import co.edu.unicauca.servicio_financiera.capaAccesoDatos.modelos.DeudaFinanciera;
import co.edu.unicauca.servicio_financiera.capaAccesoDatos.modelos.EstadoDeuda;

@Repository
public class RepositorioFinanciera {
    private final Map<Integer, List<DeudaFinanciera>> deudas = new HashMap<>();

    public RepositorioFinanciera() {
        System.out.println("Configurando servicio del área financiera...");
        insertarDeudasPrueba();
    }

    private void insertarDeudasPrueba() {
        agregarDeuda(1, new DeudaFinanciera(
                1,
                100000,
                "Perdida de materia",
                LocalDate.parse("2025-01-01"),
                LocalDate.parse("2025-01-10"),
                EstadoDeuda.EN_MORA));
        agregarDeuda(1, new DeudaFinanciera(
                1,
                200000,
                "Mora en pago de matrícula",
                LocalDate.parse("2025-02-01"),
                LocalDate.parse("2025-02-10"),
                EstadoDeuda.PAGADA));
        agregarDeuda(2, new DeudaFinanciera(
                2,
                60000,
                "Adción de laboratorio",
                LocalDate.parse("2025-03-01"),
                LocalDate.parse("2025-03-10"),
                EstadoDeuda.PENDIENTE));
        agregarDeuda(3, new DeudaFinanciera(
                3,
                75000,
                "Mora en pago de habilitación",
                LocalDate.parse("2025-04-01"),
                LocalDate.parse("2025-04-10"),
                EstadoDeuda.PENDIENTE));
    }

    private void agregarDeuda(int codigoEstudiante, DeudaFinanciera deuda) {
        if (!deudas.containsKey(codigoEstudiante)) {
            deudas.put(codigoEstudiante, new ArrayList<>());
        }
        deudas.get(codigoEstudiante).add(deuda);

    }

    public List<DeudaFinanciera> consultarDeudas(int codigoEstudiante) {
        return deudas.getOrDefault(codigoEstudiante, Collections.emptyList());
    }

    public boolean eliminarDeudas(int codigoEstudiante) {
        return deudas.remove(codigoEstudiante) != null ? true : false;
    }
}