package com.springboot.code.example;

import static java.lang.String.format;
import com.springboot.code.example.exception.ConditionViolationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestCaseProviderException {

  public static ConditionViolationException notLoad(String className) {

    return new ConditionViolationException(format(
        "Could not load class [%s]",
        className));
  }

  public static ConditionViolationException notFound(String className, String variableName,
      String classTestName) {

    return new ConditionViolationException(format(
        "Could not find variable [%s] in class [%s] for test class [%s]",
        variableName, className, classTestName));
  }

}