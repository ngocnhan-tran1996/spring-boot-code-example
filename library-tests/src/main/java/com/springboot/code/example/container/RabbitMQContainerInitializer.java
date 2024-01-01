package com.springboot.code.example.container;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.RabbitMQContainer;
import com.springboot.code.example.utils.Strings;

public class RabbitMQContainerInitializer implements
    ApplicationContextInitializer<ConfigurableApplicationContext> {

  private static final String RABBIT = "rabbitmq";

  static final RabbitMQContainer rabbitmq = new RabbitMQContainer(RABBIT)
      .withCreateContainerCmdModifier(cmd -> cmd.withName(RABBIT));

  static {
    rabbitmq.start();
  }

  @Override
  public void initialize(ConfigurableApplicationContext ctx) {
    TestPropertyValues.of(
        Strings.join("spring.rabbitmq.host=", rabbitmq.getHost()),
        Strings.join("spring.rabbitmq.port=", rabbitmq.getAmqpPort()),
        Strings.join("spring.rabbitmq.username=", rabbitmq.getAdminUsername()),
        Strings.join("spring.rabbitmq.password=", rabbitmq.getAdminPassword()))
        .applyTo(ctx.getEnvironment());
  }

}
