package com.springboot.code.example.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import com.springboot.code.example.database.jdbc.JdbcTemplateExecutor;
import com.springboot.code.example.database.profiles.JdbcProfile;

@JdbcProfile(
    scanBasePackages = "com.springboot.code.example.database.jdbc",
    basePackages = "com.springboot.code.example.database.jdbc",
    packages = "com.springboot.code.example.database.jdbc")
public class DatabaseApplication {

  public static void main(String[] args) {
    SpringApplication.run(DatabaseApplication.class, args);
  }

  @Autowired
  JdbcTemplateExecutor executor;

  @Bean
  CommandLineRunner commandLineRunner() {
    return args -> executor.executeProcedure();
  }
}
