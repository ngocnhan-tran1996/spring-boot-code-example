package io.ngocnhan_tran1996.code.example.database.support.oracle.in;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import oracle.jdbc.driver.OracleConnection;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ArrayTypeValue<T> extends AbstractOracleTypeValue {

  /** The type name of the ARRAY **/
  private final String arrayTypeName;

  @Getter
  private final List<T> values;

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