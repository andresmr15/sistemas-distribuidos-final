package co.edu.unicauca.estudiante_clienterest.vista;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.edu.unicauca.estudiante_clienterest.capaFachadaServices.servicios.OperacionesClienteImpl;
import co.edu.unicauca.estudiante_clienterest.capaFachadaServices.DTO.peticion.PeticionEstudianteDTO;
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
        int codigoEstudiante;
        PeticionEstudianteDTO objPeticion;
        RespuestaPazYSalvoDTO objRespuesta;

        do {
            try {
                System.out.println("\n======= Menu Cliente =======");
                System.out.println("1. Solicitar paz y salvo de manera sincrona");
                System.out.println("2. Solicitar paz y salvo de manera asíncrona");
                System.out.println("3. Salir");
                System.out.print("Ingrese una opcion: ");

                opcion = UtilidadesConsola.leerEntero();

                switch (opcion) {
                    case 1:
                        try {
                            System.out.print("Ingrese su código de estudiante: ");
                            codigoEstudiante = UtilidadesConsola.leerEntero();
                            objPeticion = new PeticionEstudianteDTO(codigoEstudiante);
                            objRespuesta = this.objOperacionCliente.generarPazYSalvoSincrono(objPeticion);
                            imprimirResultado(objRespuesta);
                        } catch (Exception e) {
                            System.out.println("\nError al procesar la solicitud: " + e.getMessage());
                            System.out.println("Volviendo al menú principal...");
                        }
                        break;
                    case 2:
                        try {
                            System.out.print("Ingrese su código de estudiante: ");
                            codigoEstudiante = UtilidadesConsola.leerEntero();
                            objPeticion = new PeticionEstudianteDTO(codigoEstudiante);
                            objRespuesta = this.objOperacionCliente.generarPazYSalvoAsincrono(objPeticion);
                            imprimirResultado(objRespuesta);
                        } catch (Exception e) {
                            System.out.println("\nError al procesar la solicitud: " + e.getMessage());
                            System.out.println("Volviendo al menú principal...");
                        }
                        break;
                    case 3:
                        System.out.println("Saliendo...");
                        break;
                    default:
                        System.out.println("Opcion invalida");
                }
            } catch (Exception e) {
                System.out.println("\nError inesperado: " + e.getMessage());
                System.out.println("Volviendo al menú principal...");
                opcion = 0; // Forzar continuar en el menú
            }
        } while (opcion != 3);
    }

    private void imprimirResultado(RespuestaPazYSalvoDTO objRespuesta) {
        if (objRespuesta.getMensaje() != null && objRespuesta.getMensaje().contains("Error")) {
            System.out.println("\n" + objRespuesta.getMensaje());
            return;
        }

        imprimirDeudasDeportes(objRespuesta.getObjDeportes().getDeudas());
        imprimirDeudasFinanciera(objRespuesta.getObjFinanciera().getDeudas());
        imprimirDeudasLaboratorio(objRespuesta.getObjLaboratorio().getPrestamos());
        System.out.println("\nMensaje: " + objRespuesta.getMensaje());
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
