package com.springboot.code.example.aspect.testcase;

import java.util.List;
import com.springboot.code.example.testcase.TestArguments;

class AspectTestArguments {

  static List<TestArguments<Throwable, Class<? extends Throwable>>> testAspectArguments =
      List.of(
          TestArguments.of(new RuntimeException("Test"), RuntimeException.class),
          TestArguments.of(new NullPointerException(), NullPointerException.class),
          TestArguments.of(new MyException(), MyException.class));

  static class MyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

  }
}