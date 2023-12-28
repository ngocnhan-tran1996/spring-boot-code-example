package com.springboot.code.example.database.support.oracle.mapper;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.stream.IntStream;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.TypeDescriptor;
import com.springboot.code.example.database.support.oracle.utils.OracleTypeUtils;

public class RecordMapper<T> extends PojoMapper<T> {

  private Constructor<T> mappedConstructor;

  private String[] constructorParameterNames;

  private TypeDescriptor[] constructorParameterTypes;

  /**
   * Static factory method to create a new {@code RecordMapper}.
   * 
   * @param mappedClass
   *        the class that each row should be mapped to
   */
  public static <T> RecordMapper<T> newInstance(Class<T> mappedClass) {

    return new RecordMapper<>(mappedClass);
  }

  /**
   * Create a new {@code RecordMapper}.
   * 
   * @param mappedClass
   *        the class that each row should be mapped to
   */
  private RecordMapper(Class<T> mappedClass) {

    super(mappedClass);
  }

  @Override
  protected void extractParameterNames() {

    this.mappedConstructor = OracleTypeUtils.throwIfNull(
        BeanUtils.getResolvableConstructor(this.getMappedClass()));

    int paramCount = this.mappedConstructor.getParameterCount();
    if (paramCount < 1) {
      return;
    }

    this.constructorParameterNames = BeanUtils.getParameterNames(this.mappedConstructor);
    this.constructorParameterTypes = new TypeDescriptor[paramCount];
    for (int i = 0; i < paramCount; i++) {
      this.constructorParameterTypes[i] = new TypeDescriptor(
          new MethodParameter(this.mappedConstructor, i));
    }
  }

  @Override
  protected T constructMappedInstance(Map<String, Object> valueByName, BeanWrapperImpl bw) {

    if (this.constructorParameterNames == null
        || this.constructorParameterTypes == null) {

      OracleTypeUtils.throwException("Mapped constructor was not initialized");
    }

    Object[] args = new Object[this.constructorParameterNames.length];
    IntStream.range(0, args.length)
        .forEach(i -> {

          String name = this.constructorParameterNames[i];

          var value = valueByName.get(name);
          if (value == null) {
            return;
          }

          TypeDescriptor td = this.constructorParameterTypes[i];
          args[i] = bw.convertIfNecessary(value, td.getType(), td);
        });

    return BeanUtils.instantiateClass(this.mappedConstructor, args);
  }

}