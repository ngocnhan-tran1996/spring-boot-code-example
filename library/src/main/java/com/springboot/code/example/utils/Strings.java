package com.springboot.code.example.utils;

import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Strings {

  public static boolean isBlank(final String input) {

    return input == null
        || input.isBlank();
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
