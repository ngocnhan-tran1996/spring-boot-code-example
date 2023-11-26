package com.springboot.code.example.utils;

import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Patterns {

  private static final String PACKAGE_PATTERN = "([\\w.]+)#(\\w+)";

  public static String[] getClassNameAndVariableName(String input) {

    var matcher = Pattern.compile(PACKAGE_PATTERN)
        .matcher(input);

    if (!matcher.find()) {

      return new String[0];
    }

    String className = matcher.group(1);
    String variableName = matcher.group(2);

    return new String[] {className, variableName};
  }

}
