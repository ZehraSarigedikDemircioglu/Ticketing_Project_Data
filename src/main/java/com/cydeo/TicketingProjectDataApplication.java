package com.cydeo;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication // this includes @Configuration
public class TicketingProjectDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketingProjectDataApplication.class, args);

    }

    // Add bean in the container through @Bean annotation since mapper dependency from library.
    // external => @Bean

    // ModelMapper modelMapper = new ModelMapper(); => tightly-coupled
    // to be loosely-coupled, we use as below
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
