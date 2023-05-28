package com.springboot.code.example.database.jdbc.example.support.oracle.mapper;

import java.beans.PropertyDescriptor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.function.Predicate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.jdbc.support.JdbcUtils;
import com.springboot.code.example.common.helper.Strings;
import com.springboot.code.example.database.jdbc.example.support.oracle.value.OracleTypeValueException;
import com.springboot.code.example.database.jdbc.function.PropertyHandlerFunction;
import lombok.extern.log4j.Log4j2;
import oracle.jdbc.OracleDatabaseMetaData;
import oracle.jdbc.OracleTypeMetaData;
import oracle.jdbc.driver.OracleConnection;

@Log4j2
public abstract class AbstractOracleStructMapper<T> implements OracleMapper<T> {

  /**
   * Extract the values for all columns in the current row.
   * <p>
   * Utilizes public setters and result set meta-data.
   * 
   * @see java.sql.ResultSetMetaData
   */
  @Override
  public Struct toStruct(T source, Connection connection, String typeName) throws SQLException {

    var oracleTypeMetaData = connection
        .getMetaData()
        .unwrap(OracleDatabaseMetaData.class)
        .getOracleTypeMetaData(typeName);

    if (oracleTypeMetaData.getKind() != OracleTypeMetaData.Kind.STRUCT) {
      throw new SQLDataException(typeName + " is not struct");
    }

    ResultSetMetaData rsmd = ((OracleTypeMetaData.Struct) oracleTypeMetaData).getMetaData();
    Object[] values = new Object[rsmd.getColumnCount()];

    BeanWrapper bw = new BeanWrapperImpl(source);

    this.handlePropertyDescriptor(
        rsmd,
        pd -> bw.isReadableProperty(pd.getName()),
        (index, pd) -> values[index - 1] = bw.getPropertyValue(pd.getName()));

    return connection.unwrap(OracleConnection.class)
        .createStruct(typeName, values);
  }

  /**
   * Extract the values for all columns in the current row.
   * <p>
   * Utilizes public setters and result set meta-data.
   * 
   * @see java.sql.ResultSetMetaData
   */
  @Override
  public T mapRow(ResultSet rs, int rowNumber) throws SQLException {

    T mappedObject = BeanUtils.instantiateClass(this.getMappedClass());

    BeanWrapperImpl bw = new BeanWrapperImpl();
    bw.setBeanInstance(mappedObject);

    ResultSetMetaData rsmd = rs.getMetaData();

    this.handlePropertyDescriptor(
        rsmd,
        pd -> bw.isWritableProperty(pd.getName()),
        (index, pd) -> {

          Object value = JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
          bw.setPropertyValue(pd.getName(), value);
        });

    return mappedObject;
  }


  protected void handlePropertyDescriptor(
      ResultSetMetaData rsmd,
      Predicate<PropertyDescriptor> predicate,
      PropertyHandlerFunction<Integer, PropertyDescriptor> propertyHandler)
      throws SQLException {

    int columns = rsmd.getColumnCount();
    for (int index = 1; index <= columns; index++) {

      String column = Strings.toLowerCase(JdbcUtils.lookupColumnName(rsmd, index));
      PropertyDescriptor pd = this.getPropertyDescriptor(column);

      if (pd == null
          || Predicate.not(predicate).test(pd)) {

        log.warn("Unable to access the getter/setter method");
        continue;
      }

      var name = pd.getName();
      try {

        if (log.isDebugEnabled()) {
          log.debug("Mapping column '{}' to property '{}'", column, name);
        }

        propertyHandler.accept(index, pd);
      } catch (NotReadablePropertyException ex) {

        throw new OracleTypeValueException(
            String.format("Unable to map column '%s' to property '%s'", column, name));
      }
    }
  }

  protected abstract void initialize(Class<T> mappedClass);

  protected abstract PropertyDescriptor getPropertyDescriptor(String column);

  protected abstract Class<T> getMappedClass();


}