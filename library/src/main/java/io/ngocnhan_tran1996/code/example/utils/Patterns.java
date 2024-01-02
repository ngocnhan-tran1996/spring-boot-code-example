package io.ngocnhan_tran1996.code.example.utils;

import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Patterns {

  private static final String PACKAGE_PATTERN = "([\\S._]+)#([A-z]+)$";

  public static String[] getClassNameAndVariableName(final String input) {

    if (Strings.isBlank(input)) {

      return new String[0];
    }

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
