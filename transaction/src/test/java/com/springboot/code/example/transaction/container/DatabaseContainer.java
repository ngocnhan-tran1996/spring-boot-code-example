package com.springboot.code.example.transaction.container;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class DatabaseContainer {

  @Bean
  @ServiceConnection
  public PostgreSQLContainer<?> postgres() {

    try (var postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine")) {

      return postgreSQLContainer
          .withReuse(true);
    }
  }

}
