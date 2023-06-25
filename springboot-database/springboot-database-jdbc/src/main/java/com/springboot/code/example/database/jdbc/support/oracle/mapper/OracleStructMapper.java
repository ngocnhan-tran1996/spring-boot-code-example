package com.springboot.code.example.database.jdbc.support.oracle.mapper;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import com.springboot.code.example.common.helper.Strings;
import com.springboot.code.example.common.support.oracle.OracleColumn;
import com.springboot.code.example.database.jdbc.support.oracle.value.OracleTypeValueException;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class OracleStructMapper<T> extends AbstractOracleStructMapper<T> {

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

  @Override
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

            String lowerCaseName = Strings.toLowerCase(name);
            this.mappedProperties.put(lowerCaseName, pd);

            String underscoreName = Strings.underscoreName(name);
            if (Strings.notEquals(underscoreName, lowerCaseName)) {

              this.mappedProperties.put(underscoreName, pd);
            }
          });
    }
  }

  @Override
  protected PropertyDescriptor getPropertyDescriptor(String column) {

    return this.mappedProperties.get(column);
  }

  @Override
  protected Class<T> getMappedClass() {

    return this.mappedClass;
  }

}