package com.springboot.datasource.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.springboot.datasource.common.repository.CardRepository;

@SpringBootApplication
public class DatasourceCommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatasourceCommonApplication.class, args);
    }

@Autowired
    CardRepository cardRepository;

  @Bean
  CommandLineRunner commandLineRunner() {
    return args -> {

        var newCar = new CarEntity();

        cardRepository.save(newCar)

    };
  }

}
