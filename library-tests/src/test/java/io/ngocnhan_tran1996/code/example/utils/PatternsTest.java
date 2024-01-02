package io.ngocnhan_tran1996.code.example.utils;

import static org.assertj.core.api.Assertions.assertThat;
import io.ngocnhan_tran1996.code.example.testcase.TestCase;
import io.ngocnhan_tran1996.code.example.utils.Patterns;

class PatternsTest {

  @TestCase
  public void testGetClassNameAndVariableName(String input, String[] expectedOutput) {

    assertThat(Patterns.getClassNameAndVariableName(input)).isEqualTo(expectedOutput);
  }

}
