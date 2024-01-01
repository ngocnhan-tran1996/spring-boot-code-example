package com.springboot.code.example.container;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import com.springboot.code.example.utils.Strings;

public class PolyPostgreSQLContainerInitializer extends PostgreSQLContainerInitializer {

  @Override
  public void initialize(ConfigurableApplicationContext ctx) {
    TestPropertyValues.of(
        Strings.join("app.datasource.postgres.url=", postgres.getJdbcUrl()),
        Strings.join("app.datasource.postgres.username=", postgres.getUsername()),
        Strings.join("app.datasource.postgres.password=", postgres.getPassword()),
        Strings.join("app.datasource.postgres.driver-class-name=", postgres.getDriverClassName()))
        .applyTo(ctx.getEnvironment());
  }

}