package com.springboot.code.example.database.problem;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class SpringContext {

  private static volatile ConfigurableApplicationContext context;

  public static void autowireBean(Object bean) {

    context = SpringApplication.run(DatabaseProblemApplication.class);
    var factory = context.getAutowireCapableBeanFactory();
    factory.autowireBean(bean);
  }

  public static void close() {

    context.close();
  }

}
