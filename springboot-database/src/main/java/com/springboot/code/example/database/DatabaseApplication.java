package com.springboot.code.example.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.springboot.code.example.database.multiple.datasource.DatasourceExecutor;
import com.springboot.code.example.database.profiles.ProfilePackages;

@SpringBootApplication(scanBasePackages = ProfilePackages.MULTIPLE_DATASOURCE)
public class DatabaseApplication {

  public static void main(String[] args) {
    SpringApplication.run(DatabaseApplication.class, args);
  }

  @Autowired
  DatasourceExecutor datasourceExecutor;

  @Bean
  CommandLineRunner commandLineRunner() {

    return args -> datasourceExecutor.logSQL();
  }

}