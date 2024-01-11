package io.ngocnhan_tran1996.code.example.database.support.oracle.in;

import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.jdbc.core.support.AbstractSqlTypeValue;
import io.ngocnhan_tran1996.code.example.database.support.oracle.OracleMapperAccessor;
import io.ngocnhan_tran1996.code.example.database.support.oracle.exception.OracleTypeException;
import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.Strings;

abstract class AbstractOracleTypeValue<T> extends AbstractSqlTypeValue implements
    OracleMapperAccessor<AbstractOracleTypeValue<T>> {

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
    return this.createTypeValue(connection, defaultTypeName);
  }

  protected abstract String getTypeName();

  protected abstract Object createTypeValue(Connection connection, String typeName)
      throws SQLException;

}