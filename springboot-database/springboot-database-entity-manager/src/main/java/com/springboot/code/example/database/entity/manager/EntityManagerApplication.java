package com.springboot.code.example.database.entity.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.springboot.code.example.database.entity.manager.example.EntityManagerExample;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootApplication
public class EntityManagerApplication {

  public static void main(String[] args) {
    SpringApplication.run(EntityManagerApplication.class, args);
  }

  @Autowired
  EntityManagerExample entityManagerExample;

  @Bean
  CommandLineRunner commandLineRunner() {
    return args -> {
      log.info("query {}", entityManagerExample.toNativeQuery());
      log.info("query {}", entityManagerExample.saveAndFindAll());
    };
  }

}