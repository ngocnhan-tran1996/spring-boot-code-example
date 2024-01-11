package io.ngocnhan_tran1996.code.example.database.support.oracle.mapper;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.TypeDescriptor;
import io.ngocnhan_tran1996.code.example.database.support.oracle.annotation.OracleColumn;
import io.ngocnhan_tran1996.code.example.database.support.oracle.exception.OracleTypeException;
import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.OracleTypeUtils;
import io.ngocnhan_tran1996.code.example.database.support.oracle.utils.Strings;

class RecordPropertyMapper<T> extends BeanPropertyMapper<T> {

  private Constructor<T> mappedConstructor;

  private String[] constructorParameterNames;

  private TypeDescriptor[] constructorParameterTypes;

  /**
   * Static factory method to create a new {@code RecordPropertyMapper}.
   * 
   * @param mappedClass
   *        the class that each row should be mapped to
   */
  public static <T> RecordPropertyMapper<T> newInstance(Class<T> mappedClass) {

    return new RecordPropertyMapper<>(mappedClass);
  }

  /**
   * Create a new {@code RecordPropertyMapper}.
   * 
   * @param mappedClass
   *        the class that each row should be mapped to
   */
  private RecordPropertyMapper(Class<T> mappedClass) {

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
    this.constructorParameterTypes = IntStream.range(0, paramCount)
        .boxed()
        .map(i -> new TypeDescriptor(new MethodParameter(this.mappedConstructor, i)))
        .toArray(TypeDescriptor[]::new);
  }

  @Override
  protected T constructMappedInstance(Map<String, Object> valueByName, BeanWrapperImpl bw) {

    if (this.constructorParameterNames == null
        || this.constructorParameterTypes == null) {

      throw new OracleTypeException("Mapped constructor was not initialized");
    }

    Object[] args = new Object[this.constructorParameterNames.length];
    IntStream.range(0, args.length)
        .forEach(i -> {

          try {

            var name = this.constructorParameterNames[i];
            var oracleColumn = this.getMappedClass().getDeclaredField(name)
                .getDeclaredAnnotation(OracleColumn.class);
            var fieldName = Optional.ofNullable(oracleColumn)
                .map(OracleColumn::value)
                .filter(Predicate.not(Strings::isBlank))
                .orElse(name);

            var value = valueByName.get(fieldName);
            if (value == null) {
              return;
            }

            TypeDescriptor td = this.constructorParameterTypes[i];
            args[i] = bw.convertIfNecessary(value, td.getType(), td);
          } catch (Exception e) {

            // do nothing
          }
        });

    return BeanUtils.instantiateClass(this.mappedConstructor, args);
  }

  @Override
  protected Object[] createStruct(int columns, Map<String, Integer> columnNameByIndex,
      BeanWrapperImpl bw) {

    super.extractParameterNames();
    return super.createStruct(columns, columnNameByIndex, bw);
  }

}