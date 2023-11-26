package com.springboot.code.example.utils;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.params.ParameterizedTest;
import com.springboot.code.example.TestCase;

class StringsTest {

  @ParameterizedTest
  @TestCase
  void testIsNotBlank(String input, boolean expected) {

    assertThat(Strings.isBlank(input)).isEqualTo(expected);
  }
}
