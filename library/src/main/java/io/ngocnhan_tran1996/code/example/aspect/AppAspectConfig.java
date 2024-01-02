package io.ngocnhan_tran1996.code.example.aspect;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.ngocnhan_tran1996.code.example.trace.ThrowableTrace;

@Configuration
public class AppAspectConfig {

  @Bean
  ThrowableTrace throwableTrace() {

    return new ThrowableTrace();
  }

}