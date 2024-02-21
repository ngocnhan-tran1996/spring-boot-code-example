package io.ngocnhan_tran1996.code.example.testcase;

import static io.ngocnhan_tran1996.code.example.testcase.TestCaseUtils.config;
import static io.ngocnhan_tran1996.code.example.testcase.TestCaseUtils.configArguments;
import static org.junit.platform.commons.util.AnnotationUtils.findRepeatableAnnotations;
import static org.junit.platform.commons.util.AnnotationUtils.isAnnotated;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.support.AnnotationConsumerInitializer;
import org.junit.platform.commons.util.ReflectionUtils;

class TestCaseExtension implements TestTemplateInvocationContextProvider {

    private static TestTemplateInvocationContext createInvocationContext(
        Object[] arguments,
        String templateMethodName) {

        return new TestCaseInvocationContext(arguments, templateMethodName);
    }

    private static Stream<? extends Arguments> arguments(
        ArgumentsProvider provider,
        ExtensionContext context) {
        try {

            return provider.provideArguments(context);
        } catch (Exception ex) {

            throw config(ex);
        }
    }

    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {

        var testMethod = context.getTestMethod();
        return testMethod.isPresent()
            && isAnnotated(testMethod.get(), TestCase.class);
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(
        ExtensionContext context) {

        Method templateMethod = context.getRequiredTestMethod();
        String templateMethodName = templateMethod.getName();
        var invocationCount = new AtomicLong(0);

        return findRepeatableAnnotations(templateMethod, ArgumentsSource.class)
            .stream()
            .map(ArgumentsSource::value)
            .map(this::instantiateArgumentsProvider)
            .map(provider -> AnnotationConsumerInitializer.initialize(templateMethod, provider))
            .flatMap(provider -> arguments(provider, context))
            .map(Arguments::get)
            // length = 3 means input, output, exClass
            .filter(arguments -> arguments.length == 3)
            .map(arguments -> {

                invocationCount.incrementAndGet();
                return createInvocationContext(arguments, templateMethodName);
            })
            .onClose(() -> {

                if (invocationCount.get() > 0) {

                    return;
                }

                throw configArguments();
            });
    }

    private ArgumentsProvider instantiateArgumentsProvider(
        Class<? extends ArgumentsProvider> clazz) {
        try {

            return ReflectionUtils.newInstance(clazz);
        } catch (Exception ex) {

            throw config(ex);
        }
    }
}