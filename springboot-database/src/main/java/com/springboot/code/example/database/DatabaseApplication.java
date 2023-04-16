package com.springboot.code.example.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.springboot.code.example.database.jdbc.JdbcTemplateExecutor;
import com.springboot.code.example.database.profiles.ProfilePackages;

@SpringBootApplication(scanBasePackages = ProfilePackages.JDBC)
public class DatabaseApplication {

  public static void main(String[] args) {
    SpringApplication.run(DatabaseApplication.class, args);
  }

  @Autowired
  JdbcTemplateExecutor jdbcTemplateExecutor;

  @Bean
  CommandLineRunner c() {
    return args -> jdbcTemplateExecutor.executeProcedureWith();
  }
}