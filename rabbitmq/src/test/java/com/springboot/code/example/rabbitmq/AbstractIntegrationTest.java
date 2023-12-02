package com.springboot.code.example.rabbitmq;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class AbstractIntegrationTest {

  private static final String RABBIT = "rabbitmq";

  @Container
  static final RabbitMQContainer RABBIT_MQ_CONTAINER = new RabbitMQContainer(RABBIT)
      .withCreateContainerCmdModifier(cmd -> cmd.withName(RABBIT))
      .withReuse(true);

  @DynamicPropertySource
  static void overrideProperties(DynamicPropertyRegistry registry) {

    registry.add("spring.rabbitmq.host", RABBIT_MQ_CONTAINER::getHost);
    registry.add("spring.rabbitmq.port", RABBIT_MQ_CONTAINER::getAmqpPort);
    registry.add("spring.rabbitmq.username", RABBIT_MQ_CONTAINER::getAdminUsername);
    registry.add("spring.rabbitmq.password", RABBIT_MQ_CONTAINER::getAdminPassword);
  }

}
