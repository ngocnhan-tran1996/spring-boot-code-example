package com.springboot.code.example.database.multiple.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.springboot.code.example.database.multiple.datasource.example.MultipleDatasourceExample;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootApplication
public class MultipleDatasourceApplication {

  public static void main(String[] args) {
    SpringApplication.run(MultipleDatasourceApplication.class, args);
  }

  @Autowired
  MultipleDatasourceExample multipleDatasourceExample;

  @Bean
  CommandLineRunner commandLineRunner() {
    return args -> {
      log.info("query {}", multipleDatasourceExample.getAllBaseEntity());
    };
  }

}