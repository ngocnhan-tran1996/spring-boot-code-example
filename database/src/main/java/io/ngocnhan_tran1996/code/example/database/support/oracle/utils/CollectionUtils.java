package io.ngocnhan_tran1996.code.example.database.support.oracle.utils;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CollectionUtils {

    public static <T> T getByIndex(final List<T> list, final int index) {

        return list == null
            || index > list.size()
            ? null
            : list.get(index);
    }

}