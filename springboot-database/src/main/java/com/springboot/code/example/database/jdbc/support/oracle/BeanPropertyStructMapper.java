package com.springboot.code.example.database.jdbc.support.oracle;

import java.beans.PropertyDescriptor;
import java.sql.Connection;
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
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import com.springboot.code.example.common.helper.Strings;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;
import oracle.jdbc.OracleDatabaseMetaData;
import oracle.jdbc.OracleTypeMetaData;
import oracle.jdbc.driver.OracleConnection;

@Log4j2
public class BeanPropertyStructMapper implements OracleStructMapper {

  /** Map of the properties we provide mapping for. */
  @Nullable
  private Map<String, PropertyDescriptor> mappedProperties;

  /**
   * Static factory method to create a new BeanPropertyStructMapper
   * (with the mapped class specified only once).
   * 
   * @param mappedClass
   *        the class that each row should be mapped to
   */
  public static BeanPropertyStructMapper newInstance(Class<?> mappedClass) {
    return new BeanPropertyStructMapper(mappedClass);
  }

  /**
   * Create a new BeanPropertyRowMapper.
   * 
   * @param mappedClass
   *        the class that each row should be mapped to.
   */
  private BeanPropertyStructMapper(Class<?> mappedClass) {
    initialize(mappedClass);
  }

  protected void initialize(Class<?> mappedClass) {
    this.mappedProperties = new HashMap<>();

    for (PropertyDescriptor pd : BeanUtils.getPropertyDescriptors(mappedClass)) {

      if (pd.getWriteMethod() == null) {
        continue;
      }

      String name = pd.getName();
      String lowerCaseName = name.toLowerCase();
      this.mappedProperties.put(lowerCaseName, pd);

      String underscoreName = Strings.underscoreName(name);
      if (!lowerCaseName.equals(underscoreName)) {
        this.mappedProperties.put(underscoreName, pd);
      }

      Try.run(() -> {
        var oracleColumnAnnotation = mappedClass.getDeclaredField(name)
            .getAnnotation(OracleColumn.class);

        Optional.ofNullable(oracleColumnAnnotation)
            .filter(annotation -> Strings.isNotBlank(annotation.name()))
            .map(OracleColumn::name)
            .ifPresent(colName -> this.mappedProperties.put(colName, pd));
        this.mappedProperties.put(name, pd);
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
  public <T> Struct toStruct(T source, Connection connection, String typeName) throws SQLException {

    OracleTypeMetaData oracleTypeMetaData = connection
        .getMetaData()
        .unwrap(OracleDatabaseMetaData.class)
        .getOracleTypeMetaData(typeName);

    if (oracleTypeMetaData.getKind() != OracleTypeMetaData.Kind.STRUCT) {
      throw new SQLDataException(typeName + " is not struct");
    }

    var rsmd = ((OracleTypeMetaData.Struct) oracleTypeMetaData).getMetaData();

    int columns = rsmd.getColumnCount();
    Object[] values = new Object[columns];

    for (int index = 1; index <= columns; index++) {
      BeanWrapper bw = new BeanWrapperImpl(source);

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
          log.debug("Mapping column '" + column + "' to property '" + name +
              "' of type '" + ClassUtils.getQualifiedName(pd.getPropertyType()) + "'");
        }

        values[index - 1] = bw.getPropertyValue(name);
      } catch (NotReadablePropertyException ex) {
        throw new DataRetrievalFailureException("Unable to map column '" + column
            + "' to property '" + name + "'", ex);
      }
    }

    return connection.unwrap(OracleConnection.class)
        .createStruct(typeName, values);
  }

}