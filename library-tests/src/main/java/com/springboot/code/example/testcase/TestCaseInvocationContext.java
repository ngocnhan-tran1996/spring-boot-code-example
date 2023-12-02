package com.springboot.code.example.testcase;

import static com.springboot.code.example.testcase.TestCaseUtils.DISPLAY_NAME;
import static com.springboot.code.example.testcase.TestCaseUtils.EXCEPTION_DISPLAY_NAME;
import static java.util.Collections.singletonList;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class TestCaseInvocationContext implements TestTemplateInvocationContext {

  private final Object[] arguments;

  @Override
  public String getDisplayName(int invocationIndex) {

    var input = Objects.toString(this.arguments[0]);
    var expectedOutput = Objects.toString(this.arguments[1]);

    if (Objects.isNull(this.arguments[2])) {

      return DISPLAY_NAME
          .formatted(invocationIndex, expectedOutput, input);
    }

    return EXCEPTION_DISPLAY_NAME
        .formatted(invocationIndex, Objects.toString(this.arguments[2]), input);
  }

  @Override
  public List<Extension> getAdditionalExtensions() {

    return singletonList(new TestCaseParameterResolver(this.arguments));
  }

}
