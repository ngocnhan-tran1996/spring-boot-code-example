package io.ngocnhan_tran1996.code.example.utils;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Map;
import io.ngocnhan_tran1996.code.example.testcase.TestCase;
import io.ngocnhan_tran1996.code.example.utils.CollectionUtils;

class CollectionUtilsTest {

  @TestCase
  void testIsEmptyObjectArray(Object[] input, boolean expectedOutput) {

    assertThat(CollectionUtils.isEmpty(input)).isEqualTo(expectedOutput);
  }

  @TestCase
  void testIsEmptyMap(Map<?, ?> input, boolean expectedOutput) {

    assertThat(CollectionUtils.isEmpty(input)).isEqualTo(expectedOutput);
  }

  @TestCase("io.ngocnhan_tran1996.code.example.utils.testcase.CollectionUtilsTestArguments#testIsEmptyMapArguments")
  void testIsNotEmptyMap_ByNotOutputTestIsEmptyMapArguments(
      Map<?, ?> input,
      boolean expectedOutput) {

    assertThat(CollectionUtils.isNotEmpty(input)).isNotEqualTo(expectedOutput);
  }

}
