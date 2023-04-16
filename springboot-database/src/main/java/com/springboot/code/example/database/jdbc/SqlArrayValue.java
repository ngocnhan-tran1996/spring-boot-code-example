package com.springboot.code.example.database.jdbc;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.jdbc.core.support.AbstractSqlTypeValue;
import oracle.jdbc.driver.OracleConnection;

public class SqlArrayValue<T> extends AbstractSqlTypeValue {

  private final Connection oracleCon;
  private final Object[] values;

  public SqlArrayValue(final Connection oracleCon, final Object[] values) {
    this.oracleCon = oracleCon;
    this.values = values;
  }


  /**
   * The implementation for this specific type. This method is called internally by the
   * Spring Framework during the out parameter processing and it's not accessed by application
   * code directly.
   * 
   * @see org.springframework.jdbc.core.support.AbstractSqlTypeValue
   */
  protected Object createTypeValue(Connection connection, int sqlType, String typeName)
      throws SQLException {
    Array array = connection.unwrap(OracleConnection.class).createOracleArray(
        "PACK_EXAMPLE.IN_PARAMS", values);
    return array;
  }
}