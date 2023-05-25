package com.springboot.code.example.common.helper;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class CollectionUtilsTest {


  @Test
  void testAsList() {

    assertThat(CollectionUtils.asList(null)).isEqualTo(new ArrayList<>());
    assertThat(CollectionUtils.asList(new String[0])).isEqualTo(new ArrayList<>());
    assertThat(CollectionUtils.asList(new Object[] {1})).isEqualTo(Arrays.asList(1));
  }

}
