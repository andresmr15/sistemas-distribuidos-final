package co.edu.unicauca.servicio_deportes.capaAccesoDatos.repositorios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

import co.edu.unicauca.servicio_deportes.capaAccesoDatos.modelos.DeudaImplemento;

@Repository
public class RepositorioImplementos {
    private final Map<Integer, List<DeudaImplemento>> deudas = new HashMap<>();

    public RepositorioImplementos() {
        System.out.println("Configurando servicio del área de deportes...");
        insertarImplementosPrueba();
    }

    private void insertarImplementosPrueba() {
        agregarDeuda(1, new DeudaImplemento(
                1,
                LocalDate.parse("2025-01-26"),
                LocalDate.parse("2025-02-01"),
                null,
                "conos"));
        agregarDeuda(1, new DeudaImplemento(
                1,
                LocalDate.parse("2025-05-21"),
                LocalDate.parse("2025-06-01"),
                LocalDate.parse("2025-05-20"),
                "conos"));
        agregarDeuda(2, new DeudaImplemento(
                2,
                LocalDate.parse("2025-01-10"),
                LocalDate.parse("2025-01-25"),
                null,
                "conos"));
    }

    private void agregarDeuda(int codigoEstudiante, DeudaImplemento deuda) {
        if (!deudas.containsKey(codigoEstudiante)) {
            deudas.put(codigoEstudiante, new ArrayList<>());
        }
        deudas.get(codigoEstudiante).add(deuda);

    }

    public List<DeudaImplemento> consultarDeudas(int codigoEstudiante) {
        return deudas.getOrDefault(codigoEstudiante, Collections.emptyList());
    }

    public boolean eliminarDeudas(int codigoEstudiante) {
        return deudas.remove(codigoEstudiante) != null ? true : false;
    }
}