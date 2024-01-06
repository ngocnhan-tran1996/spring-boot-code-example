package io.ngocnhan_tran1996.code.example.database.support.oracle.mapper;

import java.beans.PropertyDescriptor;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.LinkedCaseInsensitiveMap;
import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.OracleTypeUtils;
import lombok.Getter;

class BeanPropertyMapper<T> extends AbstractOracleMapper<T> {

  /** The class we are mapping to. */
  @Getter
  private Class<T> mappedClass;

  /** Map of the properties we provide mapping for. */
  private Map<String, PropertyDescriptor> mappedProperties;

  /**
   * Static factory method to create a new PojoMapper
   * (with the mapped class specified only once).
   * 
   * @param mappedClass
   *        the class that each row should be mapped to
   */
  public static <T> BeanPropertyMapper<T> newInstance(Class<T> mappedClass) {
    return new BeanPropertyMapper<>(mappedClass);
  }

  /**
   * Create a new PojoMapper.
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

        OracleTypeUtils.throwMessage(String.format("Field %s already exists", name));
      }

      this.mappedProperties.put(name, pd);
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