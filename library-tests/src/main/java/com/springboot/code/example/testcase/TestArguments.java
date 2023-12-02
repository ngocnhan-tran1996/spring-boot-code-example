package com.springboot.code.example.testcase;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TestArguments<I, O> {

  private final I input;
  private final O expectedOutput;
  private final Class<? extends Throwable> exceptionClass;

  public static <I, O> TestArguments<I, O> of(
      I input,
      O expectedOutput,
      Class<? extends Throwable> exceptionClass) {

    return new TestArguments<>(input, expectedOutput, exceptionClass);
  }

  public static <I, O> TestArguments<I, O> of(I input, O expectedOutput) {

    return of(input, expectedOutput, null);
  }

  public static <I> TestArguments<I, I> ofSame(I input) {

    return of(input, input);
  }

}