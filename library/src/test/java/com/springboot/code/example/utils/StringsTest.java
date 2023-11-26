package com.springboot.code.example.utils;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.params.ParameterizedTest;
import com.springboot.code.example.TestCase;

class StringsTest {

  @ParameterizedTest
  @TestCase
  void testIsBlank(String input, boolean expectedOutput) {

    assertThat(Strings.isBlank(input)).isEqualTo(expectedOutput);
  }

  @ParameterizedTest
  @TestCase
  void testJoin(String[] input, String expectedOutput) {

    assertThat(Strings.join(input)).isEqualTo(expectedOutput);
  }

}
