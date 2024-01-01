package com.springboot.code.example.container;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import com.springboot.code.example.utils.Strings;

public class PolyOracleContainerInitializer extends OracleContainerInitializer {

  @Override
  public void initialize(ConfigurableApplicationContext ctx) {
    TestPropertyValues.of(
        Strings.join("app.datasource.oracle.url=", oracle.getJdbcUrl()),
        Strings.join("app.datasource.oracle.username=", oracle.getUsername()),
        Strings.join("app.datasource.oracle.password=", oracle.getPassword()),
        Strings.join("app.datasource.oracle.driver-class-name=", oracle.getDriverClassName()))
        .applyTo(ctx.getEnvironment());
  }

}