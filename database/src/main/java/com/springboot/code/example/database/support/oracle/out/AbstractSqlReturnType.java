package com.springboot.code.example.database.support.oracle.out;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlReturnType;
import com.springboot.code.example.database.support.oracle.utils.OracleTypeUtils;
import com.springboot.code.example.database.support.oracle.utils.Strings;

abstract class AbstractSqlReturnType implements SqlReturnType {

  @Override
  public Object getTypeValue(CallableStatement cs, int paramIndex, int sqlType, String typeName)
      throws SQLException {

    Array array = cs.getArray(paramIndex);
    return array == null
        ? null
        : this.convertArray(array);
  }

  protected abstract String getParameterName();

  protected abstract String getTypeName();

  protected abstract AbstractSqlReturnType getHandler();

  protected abstract Object convertArray(Array array) throws SQLException;

  protected SqlOutParameter toSqlOutParameter() {

    String parameterName = OracleTypeUtils.throwIfBlank(this.getParameterName());
    String arrayTypeName = OracleTypeUtils.throwIfBlank(this.getTypeName());

    return new SqlOutParameter(
        parameterName,
        Types.ARRAY,
        Strings.toUpperCase(arrayTypeName),
        this.getHandler());
  }

}