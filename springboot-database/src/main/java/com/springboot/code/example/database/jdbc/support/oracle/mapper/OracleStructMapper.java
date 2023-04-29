package com.springboot.code.example.database.jdbc.support.oracle.mapper;

import java.beans.PropertyDescriptor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;
import com.springboot.code.example.common.helper.Strings;
import com.springboot.code.example.database.jdbc.support.oracle.OracleColumn;
import com.springboot.code.example.database.jdbc.support.oracle.value.OracleTypeValueException;
import lombok.extern.log4j.Log4j2;
import oracle.jdbc.OracleDatabaseMetaData;
import oracle.jdbc.OracleTypeMetaData;
import oracle.jdbc.driver.OracleConnection;

@Log4j2
public class OracleStructMapper<T> implements OracleMapper<T> {

  /** Map of the properties we provide mapping for. */
  private Map<String, PropertyDescriptor> mappedProperties;

  /** The class we are mapping to. */
  private Class<T> mappedClass;

  /**
   * Static factory method to create a new BeanPropertyStructMapper
   * (with the mapped class specified only once).
   * 
   * @param mappedClass
   *        the class that each row should be mapped to
   */
  public static <T> OracleStructMapper<T> newInstance(Class<T> mappedClass) {
    return new OracleStructMapper<>(mappedClass);
  }

  /**
   * Create a new BeanPropertyRowMapper.
   * 
   * @param mappedClass
   *        the class that each row should be mapped to.
   */
  private OracleStructMapper(Class<T> mappedClass) {
    Assert.notNull(mappedClass, "mappedClass");
    initialize(mappedClass);
  }

  protected void initialize(Class<T> mappedClass) {
    this.mappedProperties = new HashMap<>();
    this.mappedClass = mappedClass;

    for (PropertyDescriptor pd : BeanUtils.getPropertyDescriptors(mappedClass)) {

      String name = pd.getName();

      if (this.mappedProperties.containsKey(name)) {
        throw new OracleTypeValueException(String.format("Field %s already exists", name));
      }

      Optional.ofNullable(pd.getWriteMethod())
          .ifPresent(method -> {
            try {
              var oracleColumnAnnotation = mappedClass.getDeclaredField(name)
                  .getAnnotation(OracleColumn.class);

              Optional.ofNullable(oracleColumnAnnotation)
                  .filter(annotation -> Strings.isNotBlank(annotation.name()))
                  .map(OracleColumn::name)
                  .ifPresent(colName -> this.mappedProperties.put(colName, pd));
            } catch (Exception e) {
              log.warn("Can not find annotation field '{}'", name);
            }

            String lowerCaseName = name.toLowerCase();
            this.mappedProperties.put(lowerCaseName, pd);

            String underscoreName = Strings.underscoreName(name);
            if (!lowerCaseName.equals(underscoreName)) {
              this.mappedProperties.put(underscoreName, pd);
            }
          });
    }
  }

  /**
   * Extract the values for all columns in the current row.
   * <p>
   * Utilizes public setters and result set meta-data.
   * 
   * @see java.sql.ResultSetMetaData
   */
  @Override
  public Struct toStruct(T source, Connection connection, String typeName) throws SQLException {

    OracleTypeMetaData oracleTypeMetaData = connection
        .getMetaData()
        .unwrap(OracleDatabaseMetaData.class)
        .getOracleTypeMetaData(typeName);

    if (oracleTypeMetaData.getKind() != OracleTypeMetaData.Kind.STRUCT) {
      throw new SQLDataException(typeName + " is not struct");
    }

    var rsmd = ((OracleTypeMetaData.Struct) oracleTypeMetaData).getMetaData();

    BeanWrapper bw = new BeanWrapperImpl(source);
    int columns = rsmd.getColumnCount();
    Object[] values = new Object[columns];

    for (int index = 1; index <= columns; index++) {

      String column = JdbcUtils.lookupColumnName(rsmd, index)
          .toLowerCase();
      PropertyDescriptor pd = this.mappedProperties.get(column);

      if (pd == null
          || !bw.isReadableProperty(pd.getName())) {

        log.warn("Unable to access the getter/setter method");
        continue;
      }

      var name = pd.getName();
      try {

        if (log.isDebugEnabled()) {
          log.debug("Mapping column '{}' to property '{}'", column, name);
        }

        values[index - 1] = bw.getPropertyValue(name);
      } catch (NotReadablePropertyException ex) {
        throw new OracleTypeValueException(
            String.format("Unable to map column '%s' to property '%s'", column, name));
      }
    }

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

    T mappedObject = BeanUtils.instantiateClass(this.mappedClass);

    BeanWrapperImpl bw = new BeanWrapperImpl();
    bw.setBeanInstance(mappedObject);

    ResultSetMetaData rsmd = rs.getMetaData();
    int columnCount = rsmd.getColumnCount();

    for (int index = 1; index <= columnCount; index++) {

      String column = JdbcUtils.lookupColumnName(rsmd, index)
          .toLowerCase();
      PropertyDescriptor pd = this.mappedProperties.get(column);

      if (pd == null
          || !bw.isWritableProperty(pd.getName())) {

        log.warn("Unable to access the getter/setter method");
        continue;
      }

      var name = pd.getName();
      try {

        if (rowNumber == 0 && log.isDebugEnabled()) {
          log.debug("Mapping column '{}' to property '{}'", column, name);
        }

        Object value = JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
        bw.setPropertyValue(pd.getName(), value);

      } catch (NotReadablePropertyException ex) {
        throw new OracleTypeValueException(
            String.format("Unable to map column '%s' to property '%s'", column, name));
      }
    }

    return mappedObject;
  }

}