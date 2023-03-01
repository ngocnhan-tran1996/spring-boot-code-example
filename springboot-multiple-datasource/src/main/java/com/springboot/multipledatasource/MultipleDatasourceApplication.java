package com.springboot.multipledatasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.springboot.multipledatasource.service.DataService;

@SpringBootApplication
public class MultipleDatasourceApplication {

  @Autowired
  DataService dataService;

  public static void main(String[] args) {
    SpringApplication.run(MultipleDatasourceApplication.class, args);
  }

  @Bean
  CommandLineRunner commandLineRunner() {
    return args -> dataService.manipulate();
  }

}