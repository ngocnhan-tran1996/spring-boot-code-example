package io.ngocnhan_tran1996.code.example.database.support.oracle.in;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.OracleTypeUtils;
import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.Strings;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import oracle.jdbc.driver.OracleConnection;


@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ArrayTypeValue<T> extends AbstractOracleTypeValue<T> {

  protected static final String STRUCT_TYPE = "STRUCT";

  /** The type name of the ARRAY **/
  private final String arrayTypeName;
  private final List<T> values;

  @Override
  protected String getTypeName() {

    return STRUCT_TYPE.equals(this.arrayTypeName)
        ? null
        : Strings.toUpperCase(OracleTypeUtils.throwIfBlank(this.arrayTypeName));
  }

  @Override
  protected Object createTypeValue(Connection connection, String typeName) throws SQLException {

    return connection
        .unwrap(OracleConnection.class)
        .createOracleArray(typeName, this.getValues().toArray());
  }

  protected List<T> getValues() {

    return Optional.ofNullable(this.values)
        .orElse(new ArrayList<>());
  }

}