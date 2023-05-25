package com.springboot.code.example.common.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CollectionUtils {

  public static boolean isEmpty(final Object[] objects) {

    return objects == null
        || objects.length == 0;
  }

  public static <T> List<T> asList(final T[] objects) {

    return isEmpty(objects)
        ? new ArrayList<>(0)
        : Arrays.asList(objects);
  }

}
