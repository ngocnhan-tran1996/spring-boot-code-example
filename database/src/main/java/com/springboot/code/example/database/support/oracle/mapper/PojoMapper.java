package com.springboot.code.example.database.support.oracle.mapper;

import java.beans.PropertyDescriptor;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.LinkedCaseInsensitiveMap;
import com.springboot.code.example.database.support.oracle.utils.OracleTypeUtils;
import lombok.Getter;

public class PojoMapper<T> extends AbstractOracleMapper<T> {

  /** Map of the properties we provide mapping for. */
  private Map<String, PropertyDescriptor> mappedProperties;

  /** The class we are mapping to. */
  @Getter
  private Class<T> mappedClass;

  /**
   * Static factory method to create a new PojoMapper
   * (with the mapped class specified only once).
   * 
   * @param mappedClass
   *        the class that each row should be mapped to
   */
  public static <T> PojoMapper<T> newInstance(Class<T> mappedClass) {
    return new PojoMapper<>(mappedClass);
  }

  /**
   * Create a new PojoMapper.
   * 
   * @param mappedClass
   *        the class that each row should be mapped to.
   */
  protected PojoMapper(Class<T> mappedClass) {

    this.initialize(mappedClass);
  }

  private void initialize(Class<T> mappedClass) {

    this.mappedClass = OracleTypeUtils.throwIfNull(mappedClass);
    this.mappedProperties = new LinkedCaseInsensitiveMap<>();
    this.extractParameterNames();
  }

  protected void extractParameterNames() {

    for (PropertyDescriptor pd : BeanUtils.getPropertyDescriptors(this.mappedClass)) {

      String name = pd.getName();

      if (pd.getWriteMethod() == null) {

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
        .forEach((fieldName, propertyDescriptor) -> bw.setPropertyValue(
            propertyDescriptor.getName(),
            valueByName.get(fieldName)));

    return mappedObject;
  }

}