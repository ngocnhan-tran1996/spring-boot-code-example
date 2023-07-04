package com.springboot.code.example.database.jdbc.support.oracle;

import java.sql.Array;
import java.sql.SQLException;
import org.springframework.jdbc.core.SqlOutParameter;
import com.springboot.code.example.common.helper.Strings;

public class OracleArrayReturnType extends AbstractSqlReturnType {

  @Override
  protected Object convertArray(Array array) throws SQLException {

    return array.getArray();
  }

  public static SqlOutParameter toSqlOutParameter(
      String outParameterName,
      String arrayTypeName) {

    return convertSqlOutParameter(
        outParameterName,
        Strings.toUpperCase(arrayTypeName),
        new OracleArrayReturnType());
  }

}