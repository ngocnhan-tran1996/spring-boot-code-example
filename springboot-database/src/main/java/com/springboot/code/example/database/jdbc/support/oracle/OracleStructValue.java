package com.springboot.code.example.database.jdbc.support.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OracleStructValue<T> extends OracleTypeValue {

  private final T source;

  /** The type name of the STRUCT **/
  private final String typeName;

  @Override
  protected String getTypeName() {

    return this.typeName;
  }

  @Override
  protected Object createTypeValue(Connection connection, String typeName)
      throws SQLException {

    return OracleStructMapper.newInstance(this.source.getClass())
        .toStruct(source, connection, typeName);
  }

}