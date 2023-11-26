package com.springboot.code.example.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CollectionUtils {

  public static boolean isEmpty(final String[] objects) {

    return objects == null
        || objects.length == 0;
  }

}
