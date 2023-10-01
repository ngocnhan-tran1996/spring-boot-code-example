package com.springboot.code.example.database.problem;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

@State(Scope.Thread)
public abstract class DatabaseIntegrationTests {

  @Setup
  public void init() {

    SpringContext.autowireBean(this);
  }

  @TearDown
  public void close() {

    SpringContext.close();;
  }

}
