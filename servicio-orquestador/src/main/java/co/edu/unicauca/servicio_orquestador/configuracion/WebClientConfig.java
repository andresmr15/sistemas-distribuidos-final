package co.edu.unicauca.servicio_orquestador.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient metodoAjecutar(WebClient.Builder builder) {
        return builder.build();
    }
}