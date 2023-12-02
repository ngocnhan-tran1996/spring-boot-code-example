package com.springboot.code.example.testcase;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.platform.commons.util.StringUtils;
import com.springboot.code.example.exception.ConditionViolationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class TestCaseUtils {

  public static final String DISPLAY_NAME = "%s. Got expected result=[%s] from input=[%s]";
  public static final String EXCEPTION_DISPLAY_NAME = "%s. Raise exception=[%s] from input=[%s]";
  public static final String ARGUMENTS = "Arguments";

  public static ConditionViolationException loadClass(String className) {

    return new ConditionViolationException("Could not load class [%s]".formatted(className));
  }

  public static ConditionViolationException loadVariableClass(
      String className,
      String variableName,
      String classTestName) {

    return new ConditionViolationException(
        "Could not find variable [%s] in class [%s] for test class [%s]"
            .formatted(variableName, className, classTestName));
  }

  public static ConditionViolationException loadArguments(Object item) {

    return new ConditionViolationException(
        "Variable [%s] must be a TestArguments impletementation"
            .formatted(item));
  }

  public static ConditionViolationException convertParameter(
      Exception cause,
      ParameterContext parameterContext) {

    String fullMessage = "Error converting parameter at index %s"
        .formatted(parameterContext.getIndex());
    if (StringUtils.isNotBlank(cause.getMessage())) {
      fullMessage += ": " + cause.getMessage();
    }

    return new ConditionViolationException(fullMessage);
  }

  public static ConditionViolationException configArguments() {

    return new ConditionViolationException(
        "Configuration error: You must configure at least one set of arguments for this @TestCase");
  }

  public static ConditionViolationException config(Exception cause) {

    return new ConditionViolationException(
        "Configuration @TestCase error: %s"
            .formatted(cause.getMessage()));
  }

}