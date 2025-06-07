package co.edu.unicauca.estudiante_clienterest;
import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;
@Configuration
public class FeignConfig {
    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(
        Duration.ofSeconds (4), // connectTimeout 
        Duration.ofSeconds (4) // readTimeout 
        ,false
        );
    }
}