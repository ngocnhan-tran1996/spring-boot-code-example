package com.springboot.datasource.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.springboot.datasource.common.repository.CardEntityManagerRepository;

@SpringBootApplication
public class DatasourceCommonApplication {

  public static void main(String[] args) {
    SpringApplication.run(DatasourceCommonApplication.class, args);
  }

  @Autowired
  CardEntityManagerRepository cardEntityManagerRepository;

  @Bean
  CommandLineRunner commandLineRunner() {
    return args -> {

      cardEntityManagerRepository.execute();
    };
  }

}
