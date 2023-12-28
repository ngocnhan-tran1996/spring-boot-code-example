package com.springboot.code.example.database.support.oracle.in;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.springboot.code.example.database.support.oracle.utils.OracleTypeUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import oracle.jdbc.driver.OracleConnection;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OracleArrayValue<T> extends AbstractOracleTypeValue {

  private List<T> values = new ArrayList<>();

  /** The type name of the ARRAY **/
  private final String arrayTypeName;

  public static <T> OracleArrayValue<T> withTypeName(String arrayTypeName) {

    return new OracleArrayValue<>(arrayTypeName);
  }

  public OracleArrayValue<T> value(T value) {

    this.values.add(OracleTypeUtils.throwIfNull(value));
    return this;
  }

  @Override
  protected String getTypeName() {

    return this.arrayTypeName;
  }

  @Override
  protected Object createTypeValue(Connection connection, String typeName) throws SQLException {

    return connection
        .unwrap(OracleConnection.class)
        .createOracleArray(typeName, this.values.toArray());
  }

}