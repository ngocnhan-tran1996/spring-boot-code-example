package io.ngocnhan_tran1996.code.example.database.support.oracle.in;

import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.SqlTypeValue;
import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.Strings;
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

  public static OracleTypeValue<Object> withTypeName(String typeName) {

    return withTypeName(null, typeName);
  }

  public static <T> OracleTypeValue<T> withTypeName(Class<T> clazz, String typeName) {

    return new OracleTypeValue<>(clazz, typeName);
  }

  public static <T> OracleTypeValue<T> withStructTypeName(Class<T> clazz, String typeName) {

    var oracleTypeValue = withTypeName(clazz, null);
    oracleTypeValue.withStructType(typeName);
    return oracleTypeValue;
  }

  public OracleTypeValue<T> withStructType(String structTypeName) {

    this.structTypeName = structTypeName;
    return this;
  }


  public OracleTypeValue<T> value(T value) {

    this.values.add(value);
    return this;
  }

  public SqlTypeValue toTypeValue() {

    if (Strings.isBlank(this.arrayTypeName)) {

      return new StructTypeValue<>(this.values, this.structTypeName)
          .setOracleMapper(this.clazz);
    }

    return clazz == null
        ? new ArrayTypeValue<>(this.arrayTypeName, this.values)
        : new StructArrayTypeValue<>(this.arrayTypeName, this.values, this.structTypeName)
            .setOracleMapper(this.clazz);
  }

}