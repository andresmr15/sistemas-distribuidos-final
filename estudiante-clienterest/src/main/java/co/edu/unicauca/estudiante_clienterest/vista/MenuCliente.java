package co.edu.unicauca.estudiante_clienterest.vista;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.edu.unicauca.estudiante_clienterest.capaFachadaServices.servicios.OperacionesClienteImpl;
import co.edu.unicauca.estudiante_clienterest.capaFachadaServices.DTO.peticion.EstudianteDTO;
import co.edu.unicauca.estudiante_clienterest.capaFachadaServices.DTO.respuesta.DeudaFinancieraDTO;
import co.edu.unicauca.estudiante_clienterest.capaFachadaServices.DTO.respuesta.DeudaImplementoDTO;
import co.edu.unicauca.estudiante_clienterest.capaFachadaServices.DTO.respuesta.PrestamoLaboratorioDTO;
import co.edu.unicauca.estudiante_clienterest.capaFachadaServices.DTO.respuesta.RespuestaPazYSalvoDTO;
import co.edu.unicauca.estudiante_clienterest.utilidades.UtilidadesConsola;

@Component

public class MenuCliente {
    @Autowired
    private OperacionesClienteImpl objOperacionCliente;

    public void mostrarMenu() {
        int opcion;

        do {
            System.out.println("\n======= Menu Cliente =======");
            System.out.println("1. Solicitar paz y salvo");
            System.out.println("2. Salir");
            System.out.print("Ingrese una opcion: ");

            opcion = UtilidadesConsola.leerEntero();

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese su código de estudiante: ");
                    int codigoEstudiante = UtilidadesConsola.leerEntero();
                    EstudianteDTO objPeticion = new EstudianteDTO(codigoEstudiante);
                    RespuestaPazYSalvoDTO objRespuesta = this.objOperacionCliente.generarPazYSalvoSincrono(objPeticion);
                    imprimirDeudasDeportes(objRespuesta.getObjDeportes().getDeudas());
                    imprimirDeudasFinanciera(objRespuesta.getObjFinanciera().getDeudas());
                    imprimirDeudasLaboratorio(objRespuesta.getObjLaboratorio().getPrestamos());
                    System.out.println("\nMensaje: " + objRespuesta.getMensaje());
                    break;
                case 3:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opcion invalida");

            }

        } while (opcion != 3);
    }

    private void imprimirDeudasDeportes(List<DeudaImplementoDTO> listaDeudasAreaDeportes) {
        if (!listaDeudasAreaDeportes.isEmpty()) {
            System.out.println("\n---------------------------------------------------");
            System.out.println("Deudas área de deportes:");
            System.out.println("---------------------------------------------------");
            for (DeudaImplementoDTO deuda : listaDeudasAreaDeportes) {
                System.out.println("Código del estudiante: " + deuda.getCodigoEstudiante());
                System.out.println("Fecha del prestamo: " + deuda.getFechaPrestamo());
                System.out.println("Fecha estimada de la devolución:" + deuda.getFechaDevolucionEstimada());
                System.out.println("Fecha real de la devolución: " + deuda.getFechaDevolucionReal());
                System.out.println("Implemento deportivo: " + deuda.getImplementoDeportivo());
            }
        }
    }

    private void imprimirDeudasFinanciera(List<DeudaFinancieraDTO> listaDeudasAreaFinanciera) {
        if (!listaDeudasAreaFinanciera.isEmpty()) {
            System.out.println("\n---------------------------------------------------");
            System.out.println("Deudas área financiera");
            System.out.println("---------------------------------------------------");
            for (DeudaFinancieraDTO deuda : listaDeudasAreaFinanciera) {
                System.out.println("Código del estudiante: " + deuda.getCodigoEstudiante());
                System.out.println("Monto adeudado: " + deuda.getMontoAdeudado());
                System.out.println("Motivo de la deuda: " + deuda.getMotivoDeuda());
                System.out.println("Fecha de generación de la deuda: " + deuda.getFechaGeneracionDeuda());
                System.out.println("Fehca limite de pago: " + deuda.getFechaLimitePago());
                System.out.println("Estado de la dueda: " + deuda.getEstadoDeuda());
                System.out.println("\n");
            }
        }
    }

    private void imprimirDeudasLaboratorio(List<PrestamoLaboratorioDTO> listaPrestamosAreaLaboratorios) {
        if (!listaPrestamosAreaLaboratorios.isEmpty()) {
            System.out.println("\n---------------------------------------------------");
            System.out.println("Prestamos área laboratorios:");
            System.out.println("---------------------------------------------------");
            for (PrestamoLaboratorioDTO prestamo : listaPrestamosAreaLaboratorios) {
                System.out.println("Código del estudiante: " + prestamo.getCodigoEstudiante());
                System.out.println("Fecha del prestamo: " + prestamo.getFechaPrestamo());
                System.out.println("Fecha estimada de la devolución: " + prestamo.getFechaDevolucionEstimada());
                System.out.println("Fecha real de la devolución" + prestamo.getEquipoPrestado());
            }
        }
    }
}
