package com.springboot.code.example.database.jdbc.support.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import com.springboot.code.example.common.helper.Strings;
import com.springboot.code.example.database.jdbc.support.oracle.mapper.OracleMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import oracle.jdbc.driver.OracleConnection;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OracleArrayValue<T> extends OracleTypeValue {

  private final T[] values;

  /** The type name of the ARRAY **/
  private final String arrayTypeName;

  public static <T> OracleArrayValue<T> add(T[] values, String arrayTypeName) {

    return new OracleArrayValue<>(values, Strings.toUpperCase(arrayTypeName));
  }

  @Override
  protected String getTypeName() {

    return this.arrayTypeName;
  }

  @Override
  protected Object createTypeValue(Connection connection, String typeName) throws SQLException {

    return connection
        .unwrap(OracleConnection.class)
        .createOracleArray(typeName, this.values);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected OracleMapper<T> getMapper() {

    return null;
  }

}