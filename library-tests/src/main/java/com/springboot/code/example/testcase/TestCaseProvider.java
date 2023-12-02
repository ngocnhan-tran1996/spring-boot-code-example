package com.springboot.code.example.testcase;

import static com.springboot.code.example.testcase.TestCaseUtils.ARGUMENTS;
import static com.springboot.code.example.testcase.TestCaseUtils.loadArguments;
import static com.springboot.code.example.testcase.TestCaseUtils.loadClass;
import static com.springboot.code.example.testcase.TestCaseUtils.loadVariableClass;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.util.CollectionUtils;
import org.junit.platform.commons.util.ReflectionUtils;
import com.springboot.code.example.utils.Strings;

class TestCaseProvider implements ArgumentsProvider, AnnotationConsumer<TestCase> {

  @Override
  public void accept(TestCase annotation) {

    // do nothing
  }

  @Override
  public Stream<Arguments> provideArguments(ExtensionContext context) {

    Class<?> testClass = context.getRequiredTestClass();
    String testMethodName = context.getRequiredTestMethod()
        .getName();
    return Stream.ofNullable(findValue(testClass, testMethodName))
        .flatMap(CollectionUtils::toStream)
        .map(TestCaseProvider::toArguments);
  }

  private Object findValue(Class<?> testClass, String testMethodName) {

    String testClassName = testClass.getName();
    String testCaseClassName = Strings.join(testClass.getSimpleName(), ARGUMENTS);
    String packageName = Strings.join(testClass.getPackageName(), ".testcase.");
    String className = Strings.join(packageName, testCaseClassName);
    String variableName = Strings.join(testMethodName, ARGUMENTS);

    Class<?> clazz = ReflectionUtils.tryToLoadClass(className)
        .getOrThrow(cause -> loadClass(className));

    return ReflectionUtils.tryToReadFieldValue(clazz, variableName, null)
        .getOrThrow(e -> loadVariableClass(className, variableName, testClassName));
  }

  private static Arguments toArguments(Object item) {

    if (item instanceof TestArguments<?, ?> args) {

      return arguments(args.getInput(), args.getExpectedOutput(), args.getExceptionClass());
    }

    throw loadArguments(item);
  }

}