package com.springboot.code.example.testcase;

import static com.springboot.code.example.testcase.TestCaseUtils.config;
import static com.springboot.code.example.testcase.TestCaseUtils.configArguments;
import static org.junit.platform.commons.util.AnnotationUtils.isAnnotated;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

class TestCaseExtension implements TestTemplateInvocationContextProvider {

  private static final ArgumentsProvider ARGUMENTS_PROVIDER = new TestCaseProvider();

  @Override
  public boolean supportsTestTemplate(ExtensionContext context) {

    var testMethod = context.getTestMethod();
    return testMethod.isPresent()
        && isAnnotated(testMethod.get(), TestCase.class);
  }

  @Override
  public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(
      ExtensionContext context) {

    var invocationCount = new AtomicLong(0);

    try {

      return ARGUMENTS_PROVIDER.provideArguments(context)
          .map(Arguments::get)
          .filter(arguments -> arguments.length == 3)
          .map(arguments -> {

            invocationCount.incrementAndGet();
            return createInvocationContext(arguments);
          })
          .onClose(() -> {

            if (invocationCount.get() > 0) {

              return;
            }

            throw configArguments();
          });
    } catch (Exception ex) {

      throw config(ex);
    }

  }

  private static TestTemplateInvocationContext createInvocationContext(Object[] arguments) {

    return new TestCaseInvocationContext(arguments);
  }

}