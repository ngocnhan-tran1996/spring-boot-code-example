package com.springboot.code.example.database.support.oracle.mapper;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;
import com.springboot.code.example.database.support.oracle.utils.Strings;
import oracle.jdbc.OracleStruct;
import oracle.jdbc.OracleTypeMetaData;

public abstract class AbstractOracleMapper<T> implements OracleMapper<T> {

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

      ResultSetMetaData rsmd = ((OracleTypeMetaData.Struct) struct.getOracleMetaData())
          .getMetaData();
      Object[] values = struct.getAttributes();
      int columns = values.length;
      var valueByName = new LinkedCaseInsensitiveMap<Object>();

      for (int i = 1; i <= columns; i++) {

        Object value = values[i - 1];
        String columnName = JdbcUtils.lookupColumnName(rsmd, i);
        valueByName.put(columnName, value);

        String propertyName = JdbcUtils.convertUnderscoreNameToPropertyName(columnName);
        if (Strings.notEquals(columnName, propertyName)) {

          valueByName.put(propertyName, value);
        }
      }

      return this.constructMappedInstance(valueByName, new BeanWrapperImpl());
    } catch (SQLException e) {

      return null;
    }
  }

  protected abstract T constructMappedInstance(Map<String, Object> valueByName, BeanWrapperImpl bw);

}