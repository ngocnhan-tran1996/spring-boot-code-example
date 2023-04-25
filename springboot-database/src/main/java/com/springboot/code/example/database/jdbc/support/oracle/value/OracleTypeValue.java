package com.springboot.code.example.database.jdbc.support.oracle.value;

import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.jdbc.core.support.AbstractSqlTypeValue;
import com.springboot.code.example.common.helper.Strings;
import com.springboot.code.example.database.jdbc.support.oracle.mapper.OracleMapper;

public abstract class OracleTypeValue extends AbstractSqlTypeValue {

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

    if (typeName == null && this.getTypeName() == null) {
      throw new OracleTypeValueException("The typeName is null in this context");
    }

    return this.createTypeValue(connection, Strings.getIfNull(this.getTypeName(), typeName));
  }

  protected abstract String getTypeName();

  protected abstract OracleMapper getMapper();

  protected abstract Object createTypeValue(Connection connection, String typeName)
      throws SQLException;

}