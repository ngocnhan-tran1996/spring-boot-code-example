package io.ngocnhan_tran1996.code.example.testcase;

import static io.ngocnhan_tran1996.code.example.testcase.TestCaseUtils.ARGUMENTS;
import static io.ngocnhan_tran1996.code.example.testcase.TestCaseUtils.loadArguments;
import static io.ngocnhan_tran1996.code.example.testcase.TestCaseUtils.loadClass;
import static io.ngocnhan_tran1996.code.example.testcase.TestCaseUtils.loadVariableClass;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import io.ngocnhan_tran1996.code.example.utils.CollectionUtils;
import io.ngocnhan_tran1996.code.example.utils.Patterns;
import io.ngocnhan_tran1996.code.example.utils.Strings;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.util.ReflectionUtils;

class TestCaseProvider implements ArgumentsProvider, AnnotationConsumer<TestCase> {

    private String variableName;

    private static Object tryToReadFieldValue(
        String className,
        String variableName,
        String testClassName) {

        Class<?> clazz = ReflectionUtils.tryToLoadClass(className)
            .getOrThrow(cause -> loadClass(className));

        return ReflectionUtils.tryToReadFieldValue(clazz, variableName, null)
            .getOrThrow(e -> loadVariableClass(className, variableName, testClassName));
    }

    private static Arguments toArguments(Object item) {

        if (item instanceof TestArgument<?, ?> args) {

            return arguments(args.input(), args.output(), args.exceptionClass());
        }

        throw loadArguments(item);
    }

    @Override
    public void accept(TestCase annotation) {

        this.variableName = annotation.value();
    }

    @Override
    public Stream<Arguments> provideArguments(ExtensionContext context) {

        Class<?> testClass = context.getRequiredTestClass();
        String testMethodName = context.getRequiredTestMethod()
            .getName();
        return Stream.ofNullable(this.findValue(testClass, testMethodName))
            .filter(TestArguments.class::isInstance)
            .map(obj -> ((TestArguments) obj).getArguments())
            .flatMap(org.junit.platform.commons.util.CollectionUtils::toStream)
            .map(TestCaseProvider::toArguments);
    }

    private Object findValue(Class<?> testClass, String testMethodName) {

        String testClassName = testClass.getName();
        if (Strings.isBlank(this.variableName)) {

            String packageName = Strings.join(testClass.getPackageName(), ".testcase.");
            String testCaseClassName = Strings.join(testClass.getSimpleName(), ARGUMENTS);
            String fullyClassName = Strings.join(packageName, testCaseClassName);

            return tryToReadFieldValue(
                fullyClassName,
                Strings.join(testMethodName, ARGUMENTS),
                testClassName);
        }

        String[] parts = Patterns.getClassNameAndVariableName(this.variableName);

        if (CollectionUtils.isEmpty(parts)) {

            return tryToReadFieldValue(testClassName, this.variableName, testClassName);
        }

        return tryToReadFieldValue(parts[0], parts[1], testClassName);
    }

}