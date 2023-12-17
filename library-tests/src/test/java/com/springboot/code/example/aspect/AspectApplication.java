package com.springboot.code.example.aspect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.springboot.code.example.aspect")
public class AspectApplication {

  public static void main(String[] args) {
    SpringApplication.run(AspectApplication.class, args);
  }

}