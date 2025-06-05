package co.edu.unicauca.servicio_laboratorio.capaAccesoDatos.repositorios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

import co.edu.unicauca.servicio_laboratorio.capaAccesoDatos.modelos.EstadoPrestamo;
import co.edu.unicauca.servicio_laboratorio.capaAccesoDatos.modelos.PrestamoLaboratorio;

@Repository
public class RepositorioLaboratorio {
    private final Map<Integer, List<PrestamoLaboratorio>> prestamos = new HashMap<>();

    public RepositorioLaboratorio() {
        System.out.println("Configurando servicio del área laboratorios...");
        insertarPrestamosPrueba();
    }

    private void insertarPrestamosPrueba() {
        agregarPrestamo(1, new PrestamoLaboratorio(
                1,
                LocalDate.parse("2025-01-01"),
                LocalDate.parse("2025-01-06"),
                null,
                EstadoPrestamo.ACTIVO,
                "Microscopio"));
        agregarPrestamo(1, new PrestamoLaboratorio(
                1,
                LocalDate.parse("2025-02-01"),
                LocalDate.parse("2025-02-04"),
                LocalDate.parse("2025-01-03"),
                EstadoPrestamo.DEVUELTO,
                "Osciloscopio"));
        agregarPrestamo(2, new PrestamoLaboratorio(
                  1,
                LocalDate.parse("2025-03-01"),
                LocalDate.parse("2025-03-06"),
                null,
                EstadoPrestamo.ACTIVO,
                "Computador de laboratorio"));
        agregarPrestamo(3, new PrestamoLaboratorio(
                1,
                LocalDate.parse("2025-04-02"),
                LocalDate.parse("2025-04-12"),
                null,
                EstadoPrestamo.ACTIVO,
                "Tester"));
    }

    private void agregarPrestamo(int codigoEstudiante, PrestamoLaboratorio deuda) {
        if (!prestamos.containsKey(codigoEstudiante)) {
            prestamos.put(codigoEstudiante, new ArrayList<>());
        }
        prestamos.get(codigoEstudiante).add(deuda);
    }

    public List<PrestamoLaboratorio> consultarPrestamos(int codigoEstudiante) {
        return prestamos.getOrDefault(codigoEstudiante, Collections.emptyList());
    }

    public boolean eliminarPrestamos(int codigoEstudiante) {
        return prestamos.remove(codigoEstudiante) != null ? true : false;
    }
}