package io.ngocnhan_tran1996.code.example.database.support.oracle.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DelegateOracleMapper<T> {

  private static final String RECORD_CLASS_NAME = "java.lang.Record";

  public static <T> OracleMapper<T> get(Class<T> mappedClass) {

    boolean isRecord = mappedClass != null
        && mappedClass.getSuperclass() != null
        && RECORD_CLASS_NAME.equals(mappedClass.getSuperclass().getName());

    return isRecord
        ? RecordPropertyMapper.newInstance(mappedClass)
        : BeanPropertyMapper.newInstance(mappedClass);
  }

}
