package com.springboot.code.example.database;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.springboot.code.example.database.profiles.ProfilePackages;

@SpringBootApplication(scanBasePackages = ProfilePackages.JDBC)
public class DatabaseApplication {

  public static void main(String[] args) {
    SpringApplication.run(DatabaseApplication.class, args);
  }

}