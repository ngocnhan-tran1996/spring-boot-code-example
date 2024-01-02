package io.ngocnhan_tran1996.code.example.database.support.oracle.utils;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Strings {

  public static String toUpperCase(final String input) {

    return input == null
        ? null
        : input.toUpperCase();
  }

  public static String getIfNotBlank(final String... values) {

    return values == null
        ? null
        : Arrays.stream(values)
            .filter(Objects::nonNull)
            .filter(Predicate.not(String::isBlank))
            .findFirst()
            .orElse(null);
  }

  public static boolean isBlank(final String... values) {

    return values == null
        || Arrays.stream(values)
            .allMatch(Strings::isBlank);
  }

  private static boolean isBlank(final String value) {

    return value == null
        || value.isBlank();
  }

  public static boolean notEquals(final String input, final String compareInput) {

    return !Objects.equals(input, compareInput);
  }

}