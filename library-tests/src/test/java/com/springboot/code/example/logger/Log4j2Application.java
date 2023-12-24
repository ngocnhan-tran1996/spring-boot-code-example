package com.springboot.code.example.logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.springboot.code.example.logger")
public class Log4j2Application {

  public static void main(String[] args) {
    SpringApplication.run(Log4j2Application.class, args);
  }

}