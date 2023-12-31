package com.springboot.code.example.container;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import com.springboot.code.example.utils.Strings;

public class PostgreSQLContainerInitializer implements
    ApplicationContextInitializer<ConfigurableApplicationContext> {

  static final PostgreSQLContainer<?> POSTGRE_CONTAINER = new PostgreSQLContainer<>(
      "postgres:15-alpine")
          .withCreateContainerCmdModifier(cmd -> cmd.withName("postgres"));

  static {
    POSTGRE_CONTAINER.start();
  }

  @Override
  public void initialize(ConfigurableApplicationContext ctx) {
    TestPropertyValues.of(
        Strings.join("spring.datasource.url=", POSTGRE_CONTAINER.getJdbcUrl()),
        Strings.join("spring.datasource.username=", POSTGRE_CONTAINER.getUsername()),
        Strings.join("spring.datasource.password=", POSTGRE_CONTAINER.getPassword()),
        Strings.join("spring.datasource.driver-class-name=", POSTGRE_CONTAINER
            .getDriverClassName()))
        .applyTo(ctx.getEnvironment());
  }

}
