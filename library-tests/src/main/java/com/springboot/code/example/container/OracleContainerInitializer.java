package com.springboot.code.example.container;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.oracle.OracleContainer;

abstract class OracleContainerInitializer implements
    ApplicationContextInitializer<ConfigurableApplicationContext> {

  static final OracleContainer oracle = new OracleContainer("gvenzl/oracle-free:slim-faststart")
      .withCreateContainerCmdModifier(cmd -> cmd.withName("oracle"));

  static {
    oracle.start();
  }

}