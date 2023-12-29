package com.springboot.code.example.database.support.oracle.in;

import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.jdbc.core.support.AbstractSqlTypeValue;
import com.springboot.code.example.database.support.oracle.exception.OracleTypeException;
import com.springboot.code.example.database.support.oracle.utils.Strings;

abstract class AbstractOracleTypeValue extends AbstractSqlTypeValue {

  /**
   * The implementation for this specific type. This method is called internally by the
   * Spring Framework during the out parameter processing and it's not accessed by application
   * code directly.
   * 
   * @see org.springframework.jdbc.core.support.AbstractSqlTypeValue
   */
  @Override
  protected Object createTypeValue(Connection connection, int sqlType, String typeName)
      throws SQLException {

    if (Strings.isBlank(this.getTypeName(), typeName)) {

      throw new OracleTypeException("The typeName is null in this context");
    }

    var defaultTypeName = Strings.getIfNotBlank(this.getTypeName(), typeName);
    var upperCaseTypeName = Strings.toUpperCase(defaultTypeName);
    return this.createTypeValue(connection, upperCaseTypeName);
  }

  protected abstract String getTypeName();

  protected abstract Object createTypeValue(Connection connection, String typeName)
      throws SQLException;

}