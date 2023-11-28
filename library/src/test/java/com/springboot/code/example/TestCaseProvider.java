package com.springboot.code.example;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.util.CollectionUtils;
import org.junit.platform.commons.util.ReflectionUtils;
import com.springboot.code.example.utils.Patterns;
import com.springboot.code.example.utils.Strings;

class TestCaseProvider implements ArgumentsProvider, AnnotationConsumer<TestCase> {

  private static final String TEST = "Test";
  private static final String ARGUMENTS = "Arguments";

  private String variableName;

  @Override
  public void accept(TestCase annotation) {
    this.variableName = annotation.value();
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
    if (Strings.isBlank(this.variableName)) {

      String packageName = Strings.join(testClass.getPackageName(), ".testcase.");
      String testSimpleClassName = testClass.getSimpleName();
      String testCaseClassName = testSimpleClassName.endsWith(TEST)
          ? Strings.join(testSimpleClassName, ARGUMENTS)
          : Strings.join(testSimpleClassName, TEST, ARGUMENTS);
      String fullyVariableName = testMethodName + "Arguments";

      return tryToReadFieldValue(packageName + testCaseClassName, fullyVariableName, testClassName);
    }

    String[] parts = Patterns.getClassNameAndVariableName(this.variableName);

    if (com.springboot.code.example.utils.CollectionUtils.isEmpty(parts)) {

      return tryToReadFieldValue(testClassName, this.variableName, testClassName);
    }

    return tryToReadFieldValue(parts[0], parts[1], testClassName);
  }

  private static Object tryToReadFieldValue(
      String className,
      String variableName,
      String testClassName) {

    Class<?> clazz = ReflectionUtils.tryToLoadClass(className)
        .getOrThrow(cause -> TestCaseProviderException.notLoad(className));

    return ReflectionUtils.tryToReadFieldValue(clazz, variableName, null)
        .getOrThrow(e -> TestCaseProviderException.notFound(
            className,
            variableName,
            testClassName));
  }

  private static Arguments toArguments(Object item) {

    if (item instanceof TestArguments args) {
      return arguments(args.getInput(), args.getExpectedOutput());
    }

    // Nothing to do except cast.
    if (item instanceof Arguments args) {
      return args;
    }

    // Pass all multidimensional arrays "as is", in contrast to Object[].
    // See https://github.com/junit-team/junit5/issues/1665
    if (ReflectionUtils.isMultidimensionalArray(item)) {
      return arguments(item);
    }

    // Special treatment for one-dimensional reference arrays.
    // See https://github.com/junit-team/junit5/issues/1665
    if (item instanceof Object[] args) {
      return arguments(args);
    }

    // Pass everything else "as is".
    return arguments(item);
  }

}