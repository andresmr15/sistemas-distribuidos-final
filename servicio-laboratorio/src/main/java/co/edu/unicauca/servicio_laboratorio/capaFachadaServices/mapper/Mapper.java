package co.edu.unicauca.servicio_laboratorio.capaFachadaServices.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Mapper {
    @Bean
    public ModelMapper generarMapper(){
        ModelMapper objMapper = new ModelMapper();
        return objMapper;
    }
}
