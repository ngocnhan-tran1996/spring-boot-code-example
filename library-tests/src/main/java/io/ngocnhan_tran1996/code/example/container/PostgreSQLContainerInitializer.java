package io.ngocnhan_tran1996.code.example.container;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import io.ngocnhan_tran1996.code.example.utils.Strings;

public class PostgreSQLContainerInitializer implements
    ApplicationContextInitializer<ConfigurableApplicationContext> {

  static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
      .withCreateContainerCmdModifier(cmd -> cmd.withName("postgres"));

  static {
    postgres.start();
  }

  @Override
  public void initialize(ConfigurableApplicationContext ctx) {
    TestPropertyValues.of(
        Strings.join("spring.datasource.url=", postgres.getJdbcUrl()),
        Strings.join("spring.datasource.username=", postgres.getUsername()),
        Strings.join("spring.datasource.password=", postgres.getPassword()),
        Strings.join("spring.datasource.driver-class-name=", postgres.getDriverClassName()))
        .applyTo(ctx.getEnvironment());
  }

}