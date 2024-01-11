package io.ngocnhan_tran1996.code.example.database.support.oracle.utils;

import io.ngocnhan_tran1996.code.example.database.support.oracle.exception.OracleTypeException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OracleTypeUtils {

  public static <T> T throwIfNull(final T value) {

    if (value == null) {

      throw new OracleTypeException("value must not be null!");
    }

    return value;
  }

  public static String throwIfBlank(final String value) {

    if (Strings.isBlank(value)) {

      throw new OracleTypeException("value must not be blank!");
    }

    return value;
  }

}