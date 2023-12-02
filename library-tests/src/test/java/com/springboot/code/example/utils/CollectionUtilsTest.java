package com.springboot.code.example.utils;

import static org.assertj.core.api.Assertions.assertThat;
import com.springboot.code.example.testcase.TestCase;

class CollectionUtilsTest {

  @TestCase
  void testIsEmpty(String[] input, boolean expectedOutput) {

    assertThat(CollectionUtils.isEmpty(input)).isEqualTo(expectedOutput);
  }

}
