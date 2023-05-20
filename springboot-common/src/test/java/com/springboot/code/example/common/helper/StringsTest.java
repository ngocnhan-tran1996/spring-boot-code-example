package com.springboot.code.example.common.helper;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.stream.Stream;
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

    assertThat(Strings.isNotBlank("")).isFalse();
    assertThat(Strings.isNotBlank(" ")).isFalse();
    assertThat(Strings.isNotBlank(null)).isFalse();
    assertThat(Strings.isNotBlank(NULL_VALUE)).isTrue();
    assertThat(Strings.isNotBlank("   NULL_VALUE   ")).isTrue();
  }

  @ParameterizedTest
  @CsvSource({
      "age, age",
      "lastName, last_name",
      "Name, name",
      "FirstName, first_name",
      "EMail, e_mail",
      "URL, u_r_l", // likely undesirable, but that's the status quo
      " , ''"
  })
  void testUnderscoreName(String input, String expectOutput) {

    assertThat(Strings.underscoreName(input)).isEqualTo(expectOutput);
  }

  @Test
  void testGetIfNotBlank() {

    assertThat(Strings.getIfNotBlank()).isNull();
    assertThat(Strings.getIfNotBlank((String[]) null)).isNull();
    assertThat(Strings.getIfNotBlank(null, null, null)).isNull();
    assertThat(Strings.getIfNotBlank(null, "", " ")).isNull();
    assertThat(Strings.getIfNotBlank(null, null, " ")).isNull();
    assertThat(Strings.getIfNotBlank(null, "zz")).isEqualTo("zz");
    assertThat(Strings.getIfNotBlank("abc")).isEqualTo("abc");
    assertThat(Strings.getIfNotBlank(null, "xyz")).isEqualTo("xyz");
    assertThat(Strings.getIfNotBlank(null, "xyz", "abc")).isEqualTo("xyz");
  }

  @Test
  void testToString() {

    assertThat(Strings.toString(null)).isNull();
    assertThat(Strings.toString(List.of(1, 2, 3))).isEqualTo("[1,2,3]");
    assertThat(Strings.toString(Stream.empty())).isEqualTo(null);
  }

}
