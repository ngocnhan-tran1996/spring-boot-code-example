package com.springboot.code.example.utils;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.params.ParameterizedTest;
import com.springboot.code.example.TestCase;

class CollectionUtilsTest {

  @ParameterizedTest
  @TestCase
  void testIsEmpty(String[] input, boolean expectedOutput) {

    assertThat(CollectionUtils.isEmpty(input)).isEqualTo(expectedOutput);
  }

}
