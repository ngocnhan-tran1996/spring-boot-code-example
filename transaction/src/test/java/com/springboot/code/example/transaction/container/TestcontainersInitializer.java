package com.springboot.code.example.transaction.container;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.RabbitMQContainer;
import com.springboot.code.example.utils.Strings;

class TestcontainersInitializer implements
    ApplicationContextInitializer<ConfigurableApplicationContext> {

  private static final String RABBIT = "rabbitmq";

  static final RabbitMQContainer RABBIT_MQ_CONTAINER = new RabbitMQContainer(RABBIT)
      .withCreateContainerCmdModifier(cmd -> cmd.withName(RABBIT));

  static {
    RABBIT_MQ_CONTAINER.start();
  }

  @Override
  public void initialize(ConfigurableApplicationContext ctx) {
    TestPropertyValues.of(
        Strings.join("spring.rabbitmq.host=", RABBIT_MQ_CONTAINER.getHost()),
        Strings.join("spring.rabbitmq.port=", RABBIT_MQ_CONTAINER.getAmqpPort()),
        Strings.join("spring.rabbitmq.username=", RABBIT_MQ_CONTAINER.getAdminUsername()),
        Strings.join("spring.rabbitmq.password=", RABBIT_MQ_CONTAINER.getAdminPassword()))
        .applyTo(ctx.getEnvironment());
  }

}
