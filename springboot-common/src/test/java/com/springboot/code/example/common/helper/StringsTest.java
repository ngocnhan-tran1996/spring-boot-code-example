package com.springboot.code.example.common.helper;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class StringsTest {

  private static final String NULL_VALUE = "NULL";
  private static final String TEXT_VALUE = "TEXT";

  @Test
  void testGetIfNull() {

    assertThat(Strings.getIfNull(null, NULL_VALUE))
        .isEqualTo(NULL_VALUE);
    assertThat(Strings.getIfNull("", NULL_VALUE))
        .isEqualTo(Strings.EMPTY);
    assertThat(Strings.getIfNull(TEXT_VALUE, NULL_VALUE))
        .isEqualTo(TEXT_VALUE);
  }

  @Test
  void testToRootLowerCase() {

    assertThat(Strings.toLowerCase(null)).isNull();
    assertThat(Strings.toLowerCase("A")).isEqualTo("a");
    assertThat(Strings.toLowerCase("a")).isEqualTo("a");
  }

  @Test
  void testIsNotBlank() {

    assertThat(Strings.isBlank("")).isTrue();
    assertThat(Strings.isBlank(" ")).isTrue();
    assertThat(Strings.isBlank(null)).isTrue();
    assertThat(Strings.isBlank(NULL_VALUE)).isFalse();
    assertThat(Strings.isBlank("   NULL_VALUE   ")).isFalse();
  }

  @ParameterizedTest
  @CsvSource({
      "age, age",
      "lastName, last_name",
      "Name, name",
      "FirstName, first_name",
      "EMail, e_mail",
      "URL, u_r_l", // likely undesirable, but that's the status quo
  })
  void testUnderscoreName(String input, String expectOutput) {

    assertThat(Strings.underscoreName(input)).isEqualTo(expectOutput);
  }

}
