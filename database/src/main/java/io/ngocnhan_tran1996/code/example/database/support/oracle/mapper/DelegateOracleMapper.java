package io.ngocnhan_tran1996.code.example.database.support.oracle.mapper;

import java.util.function.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DelegateOracleMapper<T> {

  private static final String RECORD_CLASS_NAME = "java.lang.Record";
  private static final Predicate<Class<?>> recordClassPredicate = mappedClass -> mappedClass != null
      && mappedClass.getSuperclass() != null
      && RECORD_CLASS_NAME.equals(mappedClass.getSuperclass().getName());

  public static <T> OracleMapper<T> get(Class<T> mappedClass) {

    return recordClassPredicate.test(mappedClass)
        ? RecordPropertyMapper.newInstance(mappedClass)
        : BeanPropertyMapper.newInstance(mappedClass);
  }

}
