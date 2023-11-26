package com.springboot.code.example.utils;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.params.ParameterizedTest;
import com.springboot.code.example.TestCase;

class PatternsTest {

  @ParameterizedTest
  @TestCase
  public void testGetClassNameAndVariableName(String input, String[] expectedOutput) {

    assertThat(Patterns.getClassNameAndVariableName(input)).isEqualTo(expectedOutput);
  }

}
