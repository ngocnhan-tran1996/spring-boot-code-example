package com.springboot.code.example.database.jdbc.support.oracle.value;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.Optional;
import com.springboot.code.example.common.helper.Strings;
import com.springboot.code.example.database.jdbc.support.oracle.mapper.OracleMapper;
import com.springboot.code.example.database.jdbc.support.oracle.mapper.OracleStructMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import oracle.jdbc.driver.OracleConnection;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OracleArrayValue<T> extends OracleTypeValue {

  private final T[] values;

  /** The type name of the STRUCT **/
  private final String structTypeName;

  /** The type name of the ARRAY **/
  private final String arrayTypeName;

  public static <T> OracleArrayValue<T> add(T[] values, String arrayTypeName) {

    return add(values, null, arrayTypeName);
  }

  @SuppressWarnings("unchecked")
  public static <T> OracleArrayValue<T> add(
      T[] values,
      String structTypeName,
      String arrayTypeName) {

    T[] newValues = Optional.ofNullable(values)
        .orElse((T[]) new Object[0]);

    return new OracleArrayValue<>(newValues, structTypeName, arrayTypeName);
  }

  @Override
  protected String getTypeName() {

    return this.arrayTypeName;
  }

  @Override
  protected Object createTypeValue(Connection connection, String typeName) throws SQLException {

    var oracleConnection = connection.unwrap(OracleConnection.class);
    if (Strings.isNotBlank(this.structTypeName)) {

      var length = this.values.length;
      Struct[] structValues = new Struct[length];
      for (int i = 0; i < length; i++) {
        structValues[i] = this.getMapper().toStruct(values[i], connection, this.structTypeName);
      }

      return oracleConnection
          .createOracleArray(typeName, structValues);
    }

    return oracleConnection
        .createOracleArray(typeName, this.values);
  }

  @Override
  protected OracleMapper getMapper() {

    return Optional.ofNullable(this.values)
        .filter(v -> v.length > 0)
        .map(v -> OracleStructMapper.newInstance(v[0].getClass()))
        .orElse(null);
  }

}