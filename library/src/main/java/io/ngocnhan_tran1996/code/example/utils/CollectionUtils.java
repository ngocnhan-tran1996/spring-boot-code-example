package io.ngocnhan_tran1996.code.example.utils;

import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CollectionUtils {

  public static boolean isEmpty(final Object[] objects) {

    return objects == null
        || objects.length == 0;
  }

  public static boolean isEmpty(final Map<?, ?> map) {

    return map == null
        || map.isEmpty();
  }

  public static boolean isNotEmpty(final Map<?, ?> map) {

    return !isEmpty(map);
  }

}
