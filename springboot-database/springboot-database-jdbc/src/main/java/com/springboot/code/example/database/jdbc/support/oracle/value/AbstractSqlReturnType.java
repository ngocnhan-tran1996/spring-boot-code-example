package com.springboot.code.example.database.jdbc.support.oracle.value;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlReturnType;

public abstract class AbstractSqlReturnType implements SqlReturnType {

  @Override
  public Object getTypeValue(CallableStatement cs, int paramIndex, int sqlType, String typeName)
      throws SQLException {

    Array array = cs.getArray(paramIndex);
    return array == null
        ? null
        : this.convertArray(array);
  }

  protected abstract Object convertArray(Array array) throws SQLException;

  protected static <T extends AbstractSqlReturnType> SqlOutParameter convertSqlOutParameter(
      String outParameterName,
      String arrayTypeName,
      T sqlReturnType) {

    return new SqlOutParameter(outParameterName, Types.ARRAY, arrayTypeName, sqlReturnType);
  }

}