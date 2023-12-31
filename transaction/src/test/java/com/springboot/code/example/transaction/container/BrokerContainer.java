package com.springboot.code.example.transaction.container;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.RabbitMQContainer;

@TestConfiguration(proxyBeanMethods = false)
public class BrokerContainer {

  private static final String RABBIT = "rabbitmq";

  @Bean
  @ServiceConnection
  public RabbitMQContainer rabbit() {

    try (var rabbitMQContainer = new RabbitMQContainer(RABBIT)) {

      return rabbitMQContainer
          .withCreateContainerCmdModifier(cmd -> cmd.withName(RABBIT));
    }
  }

}