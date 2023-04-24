package com.springboot.code.example.common.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Strings {

  public static final String EMPTY = "";

  public static String getIfNull(String a, String b) {

    return a == null
        ? b
        : a;
  }

  public static String toLowerCase(String input) {

    return input == null
        ? null
        : input.toLowerCase();
  }

  public static boolean isNotBlank(String input) {

    return !isBlank(input);
  }

  public static boolean isBlank(String input) {

    return input == null
        || input.isBlank();
  }

  /**
   * Convert a name in camelCase to an underscored name in lower case.
   * Any upper case letters are converted to lower case with a preceding underscore.
   *
   * @param name
   *        the original name
   * @return the converted name
   */
  public static String underscoreName(String name) {

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
}
