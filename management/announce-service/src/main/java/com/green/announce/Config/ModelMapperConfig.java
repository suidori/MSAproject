package com.green.announce.Config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
 @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        
        modelMapper.createTypeMap(LocalDateTime.class, String.class)
            .setConverter(context -> {
                LocalDateTime source = context.getSource();
                if (source == null) {
                    return null;
                }
                return source.format(DateTimeFormatter.ofPattern("yy/MM/dd HH:mm"));
            });
            
        return modelMapper;
    }
}
