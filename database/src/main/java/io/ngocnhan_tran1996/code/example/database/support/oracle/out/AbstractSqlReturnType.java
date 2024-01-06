package io.ngocnhan_tran1996.code.example.database.support.oracle.out;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Struct;
import java.sql.Types;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlReturnType;
import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.OracleTypeUtils;
import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.Strings;

abstract class AbstractSqlReturnType implements SqlReturnType {

  @Override
  public Object getTypeValue(CallableStatement cs, int paramIndex, int sqlType, String typeName)
      throws SQLException {

    if (sqlType() == Types.STRUCT) {

      var struct = (Struct) cs.getObject(paramIndex);
      return struct == null
          ? null
          : this.convertStruct(struct);
    }

    Array array = cs.getArray(paramIndex);
    return array == null
        ? null
        : this.convertArray(array);
  }

  protected abstract String getParameterName();

  protected abstract String getTypeName();

  protected abstract AbstractSqlReturnType getHandler();

  protected abstract Object convertStruct(Struct struct) throws SQLException;

  protected abstract Object convertArray(Array array) throws SQLException;

  protected abstract int sqlType();

  protected SqlOutParameter toSqlOutParameter() {

    String parameterName = OracleTypeUtils.throwIfBlank(this.getParameterName());
    String arrayTypeName = OracleTypeUtils.throwIfBlank(this.getTypeName());

    return new SqlOutParameter(
        parameterName,
        sqlType(),
        Strings.toUpperCase(arrayTypeName),
        this.getHandler());
  }

  /**
   * clone from {@link #toSqlOutParameter()} method
   */
  protected SqlInOutParameter toSqlInOutParameter() {

    String parameterName = OracleTypeUtils.throwIfBlank(this.getParameterName());
    String arrayTypeName = OracleTypeUtils.throwIfBlank(this.getTypeName());

    return new SqlInOutParameter(
        parameterName,
        sqlType(),
        Strings.toUpperCase(arrayTypeName),
        this.getHandler());
  }

}