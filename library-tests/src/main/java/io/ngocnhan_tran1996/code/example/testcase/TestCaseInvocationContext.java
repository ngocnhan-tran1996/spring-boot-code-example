package io.ngocnhan_tran1996.code.example.testcase;

import static io.ngocnhan_tran1996.code.example.testcase.TestCaseUtils.DISPLAY_NAME;
import static io.ngocnhan_tran1996.code.example.testcase.TestCaseUtils.EXCEPTION_DISPLAY_NAME;
import static java.util.Collections.singletonList;

import io.ngocnhan_tran1996.code.example.utils.Strings;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;

@RequiredArgsConstructor
class TestCaseInvocationContext implements TestTemplateInvocationContext {

    private final Object[] arguments;
    private final String templateMethodName;

    @Override
    public String getDisplayName(int invocationIndex) {

        var input = Strings.toSafeString(this.arguments[0]);
        var expectedOutput = Strings.toSafeString(this.arguments[1]);
        if (templateMethodName.contains("_ByNotOutput")
            && this.arguments[1] instanceof Boolean value) {

            expectedOutput = Strings.toSafeString(!value);
        }

        if (Objects.isNull(this.arguments[2])) {

            return DISPLAY_NAME
                .formatted(invocationIndex, expectedOutput, input);
        }

        return EXCEPTION_DISPLAY_NAME
            .formatted(invocationIndex, Strings.toSafeString(this.arguments[2]), input);
    }

    @Override
    public List<Extension> getAdditionalExtensions() {

        return singletonList(new TestCaseParameterResolver(this.arguments));
    }

}