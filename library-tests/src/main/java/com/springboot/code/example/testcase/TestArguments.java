package com.springboot.code.example.testcase;

public record TestArguments<I, O>(I input, O output, Class<? extends Throwable> exceptionClass) {

  public static <I, O> TestArguments<I, O> of(
      I input,
      O output,
      Class<? extends Throwable> exceptionClass) {

    return new TestArguments<>(input, output, exceptionClass);
  }

  public static <I, O> TestArguments<I, O> of(I input, O output) {

    return of(input, output, null);
  }

  public static <I> TestArguments<I, I> ofSame(I input) {

    return of(input, input);
  }

}