package co.edu.unicauca.estudiante_clienterest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.retry.annotation.EnableRetry;
import co.edu.unicauca.estudiante_clienterest.vista.MenuCliente;

@SpringBootApplication
@EnableFeignClients
@EnableRetry
public class EstudianteClienterestApplication implements CommandLineRunner {

	@Autowired
	private MenuCliente menu;

	public static void main(String[] args) {
		SpringApplication.run(EstudianteClienterestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		menu.mostrarMenu();
	}

}
