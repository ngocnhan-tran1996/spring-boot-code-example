package com.springboot.code.example.database.benchmark;

import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.AbstractEnvironment;
import com.springboot.code.example.database.DatabaseApplication;

abstract class BenchmarkConfig {

  volatile ConfigurableApplicationContext applicationContext;

  @Setup
  public void init() {

    System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, setProfile());
    this.applicationContext = SpringApplication.run(DatabaseApplication.class);
    var factory = this.applicationContext.getAutowireCapableBeanFactory();
    factory.autowireBean(this);
  }

  @TearDown
  public void clean() {

    this.applicationContext.close();
  }

  abstract String setProfile();

}