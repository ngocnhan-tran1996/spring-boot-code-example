package com.springboot.code.example.database.support.oracle.mapper;

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.Map;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;
import com.springboot.code.example.database.support.oracle.utils.OracleTypeUtils;
import com.springboot.code.example.database.support.oracle.utils.Strings;
import oracle.jdbc.OracleDatabaseMetaData;
import oracle.jdbc.OracleStruct;
import oracle.jdbc.OracleTypeMetaData;
import oracle.jdbc.driver.OracleConnection;

abstract class AbstractOracleMapper<T> implements OracleMapper<T> {

  /**
   * Extract the values for all columns in the current row.
   * <p>
   * Utilizes public setters and result set meta-data.
   * 
   * @see java.sql.ResultSetMetaData
   */
  @Override
  public Struct toStruct(T source, Connection connection, String typeName) throws SQLException {

    var oracleTypeMetaData = connection.getMetaData()
        .unwrap(OracleDatabaseMetaData.class)
        .getOracleTypeMetaData(typeName);

    if (oracleTypeMetaData.getKind() != OracleTypeMetaData.Kind.STRUCT) {

      OracleTypeUtils.throwMessage(String.format("%s is not struct", typeName));
    }

    ResultSetMetaData rsmd = this.getResultSetMetaData(oracleTypeMetaData);
    Object[] objects = this.createStruct(
        rsmd.getColumnCount(),
        this.extractColumnNameByIndex(rsmd),
        new BeanWrapperImpl(source));
    return connection.unwrap(OracleConnection.class)
        .createStruct(typeName, objects);
  }

  /**
   * Extract the values for all attributes in the struct.
   * <p>
   * Utilizes public setters and result set metadata.
   * 
   * @see java.sql.ResultSetMetaData
   */
  @Override
  public T fromStruct(OracleStruct struct) {

    try {

      ResultSetMetaData rsmd = this.getResultSetMetaData(struct.getOracleMetaData());
      Object[] values = struct.getAttributes();
      var valueByName = new LinkedCaseInsensitiveMap<Object>();
      this.extractColumnNameByIndex(rsmd)
          .forEach((columnName, index) -> valueByName.put(columnName, values[index]));

      return this.constructMappedInstance(valueByName, new BeanWrapperImpl());
    } catch (SQLException e) {

      return null;
    }
  }

  protected abstract Object[] createStruct(
      int columns,
      Map<String, Integer> columnNameByIndex,
      BeanWrapperImpl bw);

  protected abstract T constructMappedInstance(Map<String, Object> valueByName, BeanWrapperImpl bw);

  private ResultSetMetaData getResultSetMetaData(OracleTypeMetaData struct)
      throws SQLException {

    return ((OracleTypeMetaData.Struct) struct).getMetaData();
  }

  private Map<String, Integer> extractColumnNameByIndex(ResultSetMetaData rsmd)
      throws SQLException {

    Map<String, Integer> columnNameByIndex = new LinkedCaseInsensitiveMap<>();
    var columns = rsmd.getColumnCount();
    for (int i = 0; i < columns; i++) {

      String columnName = JdbcUtils.lookupColumnName(rsmd, i + 1);
      columnNameByIndex.put(columnName, i);

      String propertyName = JdbcUtils.convertUnderscoreNameToPropertyName(columnName);
      if (Strings.notEquals(columnName, propertyName)) {

        columnNameByIndex.put(propertyName, i);
      }
    }

    return columnNameByIndex;
  }

}