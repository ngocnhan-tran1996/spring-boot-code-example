package com.springboot.code.example.testcase;

import static com.springboot.code.example.testcase.TestCaseUtils.convertParameter;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.junit.jupiter.params.converter.DefaultArgumentConverter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class TestCaseParameterResolver implements ParameterResolver {

  private static final ArgumentConverter CONVERTER = DefaultArgumentConverter.INSTANCE;

  private final Object[] arguments;

  @Override
  public boolean supportsParameter(
      ParameterContext parameterContext,
      ExtensionContext extensionContext) {

    Executable declaringExecutable = parameterContext.getDeclaringExecutable();
    Method testMethod = extensionContext.getTestMethod().orElse(null);
    int parameterIndex = parameterContext.getIndex();

    return declaringExecutable.equals(testMethod)
        && parameterIndex < this.arguments.length;
  }

  @Override
  public Object resolveParameter(
      ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {

    try {

      return CONVERTER.convert(this.arguments[parameterContext.getIndex()], parameterContext);
    } catch (ArgumentConversionException ex) {

      throw convertParameter(ex, parameterContext);
    }
  }

}
