package io.ngocnhan_tran1996.code.example.database.support.oracle.in;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.OracleTypeUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import oracle.jdbc.driver.OracleConnection;


@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ArrayTypeValue<T> extends AbstractOracleTypeValue {

  protected static final String STRUCT_TYPE = "STRUCT";

  /** The type name of the ARRAY **/
  private final String arrayTypeName;

  @Getter
  private final List<T> values;

  @Override
  protected String getTypeName() {

    return STRUCT_TYPE.equals(this.arrayTypeName)
        ? null
        : OracleTypeUtils.throwIfBlank(this.arrayTypeName);
  }

  @Override
  protected Object createTypeValue(Connection connection, String typeName) throws SQLException {

    return connection
        .unwrap(OracleConnection.class)
        .createOracleArray(typeName, this.values.toArray());
  }

}