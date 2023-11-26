package com.springboot.code.example.utils;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Strings {

  public static final String EMPTY = "";
  public static final String SPACE = " ";
  public static final String COMMA = ",";
  private static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder()
      .findAndAddModules()
      .build();

  public static String getIfNull(final String input, final String defaultInput) {

    return input == null
        ? defaultInput
        : input;
  }

  public static String toLowerCase(final String input) {

    return input == null
        ? null
        : input.toLowerCase();
  }

  public static boolean isNotBlank(final String input) {

    return !isBlank(input);
  }

  public static boolean isBlank(final String input) {

    return input == null
        || input.isBlank();
  }

  public static String getIfNotBlank(final String... values) {

    return stream(values)
        .filter(Strings::isNotBlank)
        .findFirst()
        .orElse(null);
  }

  private static Stream<String> stream(final String... values) {

    return Optional.ofNullable(values)
        .map(Arrays::stream)
        .orElse(Stream.empty());
  }


  /**
   * Convert a name in camelCase to an underscored name in lower case.
   * Any upper case letters are converted to lower case with a preceding underscore.
   *
   * @param name
   *        the original name
   * @return the converted name
   */
  public static String underscoreName(final String name) {

    if (isBlank(name)) {
      return EMPTY;
    }

    var result = new StringBuilder();
    result.append(Character.toLowerCase(name.charAt(0)));
    for (int i = 1; i < name.length(); i++) {

      char c = name.charAt(i);
      if (Character.isUpperCase(c)) {
        result.append('_');
      }

      result.append(Character.toLowerCase(c));
    }

    return result.toString();
  }

  public static String toString(final Object object) {

    if (object == null) {

      return null;
    }

    try {

      return OBJECT_MAPPER.writeValueAsString(object);
    } catch (JsonProcessingException e) {

      return null;
    }
  }

  public static String toUpperCase(final String input) {

    return input == null
        ? null
        : input.toUpperCase();
  }

  public static boolean notEquals(final String input, final String compareInput) {

    return !Objects.equals(input, compareInput);
  }

  public static String join(final String... strings) {

    if (strings == null) {

      return null;
    }

    var text = new StringBuilder();
    Stream.of(strings)
        .forEach(text::append);

    return text.toString();
  }

}
