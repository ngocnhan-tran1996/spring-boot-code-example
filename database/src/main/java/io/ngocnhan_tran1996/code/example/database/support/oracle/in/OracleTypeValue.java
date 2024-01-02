package io.ngocnhan_tran1996.code.example.database.support.oracle.in;

import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.support.AbstractSqlTypeValue;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class OracleTypeValue<T> {

  private List<T> values = new ArrayList<>();

  private final Class<T> clazz;

  /** The type name of the ARRAY **/
  private final String arrayTypeName;

  /** The type name of the STRUCT **/
  private String structTypeName;

  public static OracleTypeValue<Object> withTypeName(String arrayTypeName) {

    return new OracleTypeValue<>(null, arrayTypeName);
  }

  public static <T> OracleTypeValue<T> withTypeName(Class<T> clazz, String arrayTypeName) {

    return new OracleTypeValue<>(clazz, arrayTypeName);
  }

  public OracleTypeValue<T> withStructType(String structTypeName) {

    this.structTypeName = structTypeName;
    return this;
  }


  public OracleTypeValue<T> value(T value) {

    this.values.add(value);
    return this;
  }

  public AbstractSqlTypeValue toTypeValue() {

    return clazz == null
        ? new ArrayTypeValue<>(this.arrayTypeName, this.values)
        : new StructArrayTypeValue<>(this.arrayTypeName, this.values, this.structTypeName)
            .setOracleMapper(this.clazz);
  }

}