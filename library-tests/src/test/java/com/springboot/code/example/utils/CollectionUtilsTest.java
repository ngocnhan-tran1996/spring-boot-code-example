package com.springboot.code.example.utils;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Map;
import com.springboot.code.example.testcase.TestCase;

class CollectionUtilsTest {

  @TestCase
  void testIsEmptyObjectArray(Object[] input, boolean expectedOutput) {

    assertThat(CollectionUtils.isEmpty(input)).isEqualTo(expectedOutput);
  }

  @TestCase
  void testIsEmptyMap(Map<?, ?> input, boolean expectedOutput) {

    assertThat(CollectionUtils.isEmpty(input)).isEqualTo(expectedOutput);
  }

  @TestCase("com.springboot.code.example.utils.testcase.CollectionUtilsTestArguments#testIsEmptyMapArguments")
  void testIsNotEmptyMap_ByNotOutputTestIsEmptyMapArguments(
      Map<?, ?> input,
      boolean expectedOutput) {

    assertThat(CollectionUtils.isNotEmpty(input)).isNotEqualTo(expectedOutput);
  }

}
