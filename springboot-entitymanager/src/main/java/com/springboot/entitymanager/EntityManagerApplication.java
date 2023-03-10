package com.springboot.entitymanager;

import javax.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.springboot.entitymanager.service.EntityManagerService;

@SpringBootApplication
public class EntityManagerApplication {

  public static void main(String[] args) {
    SpringApplication.run(EntityManagerApplication.class, args);
  }

  @Resource(name = "singleEntityManagerService")
  EntityManagerService entityManagerService;

  @Bean
  CommandLineRunner commandLineRunner() {
    return args -> entityManagerService.findAll();
  }

}
