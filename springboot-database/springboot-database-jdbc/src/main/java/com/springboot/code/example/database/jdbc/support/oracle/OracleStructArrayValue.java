package com.springboot.code.example.database.jdbc.support.oracle;

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
public class OracleStructArrayValue<T> extends OracleTypeValue {

  private final T[] values;

  /** The type name of the STRUCT **/
  private final String structTypeName;

  /** The type name of the ARRAY **/
  private final String arrayTypeName;

  @SuppressWarnings("unchecked")
  public static <T> OracleStructArrayValue<T> add(
      T[] values,
      String structTypeName,
      String arrayTypeName) {

    T[] newValues = Optional.ofNullable(values)
        .orElse((T[]) new Object[0]);

    return new OracleStructArrayValue<>(
        newValues,
        Strings.toUpperCase(structTypeName),
        Strings.toUpperCase(arrayTypeName));
  }

  @Override
  protected String getTypeName() {

    return this.arrayTypeName;
  }

  @Override
  protected Object createTypeValue(Connection connection, String typeName) throws SQLException {

    var oracleConnection = connection.unwrap(OracleConnection.class);

    var length = this.values.length;
    Struct[] structValues = new Struct[length];
    for (int i = 0; i < length; i++) {
      structValues[i] = this.getMapper().toStruct(values[i], connection, this.structTypeName);
    }

    return oracleConnection
        .createOracleArray(typeName, structValues);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected OracleMapper<T> getMapper() {

    return (OracleMapper<T>) Optional.ofNullable(this.values)
        .filter(v -> v.length > 0)
        .map(v -> OracleStructMapper.newInstance(v[0].getClass()))
        .orElse(null);
  }

}