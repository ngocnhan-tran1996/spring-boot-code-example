package com.springboot.code.example;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TestArguments<I, O> {

  private I input;
  private O expectedOutput;

  public static <I, O> TestArguments<I, O> of(I input, O expectedOutput) {

    return new TestArguments<I, O>(input, expectedOutput);
  }

  public static <I> TestArguments<I, I> ofSame(I input) {

    return new TestArguments<>(input, input);
  }

}