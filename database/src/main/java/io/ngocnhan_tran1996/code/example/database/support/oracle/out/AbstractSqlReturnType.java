package io.ngocnhan_tran1996.code.example.database.support.oracle.out;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Struct;
import java.sql.Types;
import org.springframework.jdbc.core.SqlReturnType;
import io.ngocnhan_tran1996.code.example.database.support.oracle.OracleMapperAccessor;

abstract class AbstractSqlReturnType<T> implements SqlReturnType,
    OracleMapperAccessor<AbstractSqlReturnType<T>> {

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

  protected abstract T convertStruct(Struct struct) throws SQLException;

  protected abstract Object convertArray(Array array) throws SQLException;

  protected abstract int sqlType();

}