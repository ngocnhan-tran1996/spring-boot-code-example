package com.springboot.code.example.testcase;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.experimental.Delegate;

public class TestArguments implements List<TestArgument<Object, Object>> {

  @Getter
  @Delegate
  private List<TestArgument<Object, Object>> arguments = new ArrayList<>();

  public static <I, O> TestArguments params(
      I input,
      O output,
      Class<? extends Throwable> exceptionClass) {

    var testArguments = new TestArguments();
    testArguments.getArguments().add(TestArgument.of(input, output, exceptionClass));
    return testArguments;
  }

  public static <I, O> TestArguments params(I input, O output) {

    return params(input, output, null);
  }

  public static <I> TestArguments params(I input) {

    return params(input, input);
  }

  public <I, O> TestArguments nextParams(
      I input,
      O output,
      Class<? extends Throwable> exceptionClass) {

    this.getArguments().add(TestArgument.of(input, output, exceptionClass));
    return this;
  }

  public <I, O> TestArguments nextParams(I input, O output) {

    return this.nextParams(input, output, null);
  }

  public <I> TestArguments nextParams(I input) {

    return this.nextParams(input, null);
  }

}