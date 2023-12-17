package com.springboot.code.example.aspect;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.springboot.code.example.trace.ThrowableTrace;

@Configuration
public class AppAspectConfig {

  @Bean
  ThrowableTrace throwableTrace() {

    return new ThrowableTrace();
  }

}