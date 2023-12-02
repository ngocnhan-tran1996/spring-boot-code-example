package com.springboot.code.example.utils;

import static org.assertj.core.api.Assertions.assertThat;
import com.springboot.code.example.testcase.TestCase;

class StringsTest {

  @TestCase
  void testIsBlank(String input, boolean expectedOutput) {

    assertThat(Strings.isBlank(input)).isEqualTo(expectedOutput);
  }

  @TestCase
  void testJoin(String[] input, String expectedOutput) {

    assertThat(Strings.join(input)).isEqualTo(expectedOutput);
  }

}