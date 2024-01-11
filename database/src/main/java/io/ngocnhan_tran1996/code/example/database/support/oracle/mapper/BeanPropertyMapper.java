package io.ngocnhan_tran1996.code.example.database.support.oracle.mapper;

import java.beans.PropertyDescriptor;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.LinkedCaseInsensitiveMap;
import io.ngocnhan_tran1996.code.example.database.support.oracle.annotation.OracleColumn;
import io.ngocnhan_tran1996.code.example.database.support.oracle.exception.OracleTypeException;
import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.OracleTypeUtils;
import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.Strings;
import lombok.Getter;

class BeanPropertyMapper<T> extends AbstractOracleMapper<T> {

  /** The class we are mapping to. */
  @Getter
  private Class<T> mappedClass;

  /** Map of the properties we provide mapping for. */
  private Map<String, PropertyDescriptor> mappedProperties;

  /**
   * Static factory method to create a new BeanPropertyMapper
   * (with the mapped class specified only once).
   * 
   * @param mappedClass
   *        the class that each row should be mapped to
   */
  public static <T> BeanPropertyMapper<T> newInstance(Class<T> mappedClass) {
    return new BeanPropertyMapper<>(mappedClass);
  }

  /**
   * Create a new BeanPropertyMapper.
   * 
   * @param mappedClass
   *        the class that each row should be mapped to.
   */
  protected BeanPropertyMapper(Class<T> mappedClass) {

    this.initialize(mappedClass);
  }

  private void initialize(Class<T> mappedClass) {

    this.mappedClass = OracleTypeUtils.throwIfNull(mappedClass);
    this.extractParameterNames();
  }

  protected void extractParameterNames() {

    this.mappedProperties = new LinkedCaseInsensitiveMap<>();

    for (PropertyDescriptor pd : BeanUtils.getPropertyDescriptors(this.mappedClass)) {

      String name = pd.getName();

      if (pd.getWriteMethod() == null
          && pd.getReadMethod() == null) {

        continue;
      }

      if (this.mappedProperties.containsKey(name)) {

        throw new OracleTypeException(String.format("Field %s already exists", name));
      }

      try {

        var oracleColumn = this.mappedClass.getDeclaredField(name)
            .getDeclaredAnnotation(OracleColumn.class);
        Optional.ofNullable(oracleColumn)
            .map(OracleColumn::value)
            .filter(Predicate.not(Strings::isBlank))
            .filter(Predicate.not(name::equals))
            .ifPresentOrElse(
                fieldName -> this.mappedProperties.put(fieldName, pd),
                () -> this.mappedProperties.put(name, pd));
      } catch (Exception e) {

        // do nothing
      }
    }

  }

  @Override
  protected T constructMappedInstance(Map<String, Object> valueByName, BeanWrapperImpl bw) {

    T mappedObject = BeanUtils.instantiateClass(this.mappedClass);
    bw.setBeanInstance(mappedObject);

    this.mappedProperties
        .forEach((fieldName, propertyDescriptor) -> {

          if (!bw.isWritableProperty(propertyDescriptor.getName())) {

            return;
          }

          bw.setPropertyValue(
              propertyDescriptor.getName(),
              valueByName.get(fieldName));
        });

    return mappedObject;
  }

  @Override
  protected Object[] createStruct(
      int columns,
      Map<String, Integer> columnNameByIndex,
      BeanWrapperImpl bw) {

    Object[] values = new Object[columns];

    this.mappedProperties
        .forEach((fieldName, propertyDescriptor) -> {

          String name = propertyDescriptor.getName();
          if (!bw.isReadableProperty(name)
              || columnNameByIndex.get(fieldName) == null) {

            return;
          }

          values[columnNameByIndex.get(fieldName)] = bw.getPropertyValue(name);
        });

    return values;
  }

}