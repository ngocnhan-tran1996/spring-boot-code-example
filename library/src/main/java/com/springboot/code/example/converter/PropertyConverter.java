package com.springboot.code.example.converter;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.util.Assert;
import org.springframework.util.LinkedCaseInsensitiveMap;
import com.springboot.code.example.support.oracle.OracleColumn;
import com.springboot.code.example.utils.Strings;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertyConverter {

  public static <T> T convert(Map<String, Object> source, Class<T> destinationType) {

    Assert.notNull(source, "source");
    Assert.notNull(destinationType, "destinationType");

    try {
      Map<String, Object> ignoreCaseMap = new LinkedCaseInsensitiveMap<>(source.size());
      ignoreCaseMap.putAll(source);

      T mappedObject = BeanUtils.instantiateClass(destinationType);
      BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);

      for (Field field : destinationType.getDeclaredFields()) {

        String name = field.getName();

        if (!bw.isWritableProperty(name)) {
          continue;
        }

        var oracleColumnAnnotation = field.getAnnotation(OracleColumn.class);
        Optional.ofNullable(oracleColumnAnnotation)
            .filter(annotation -> Strings.isNotBlank(annotation.name()))
            .map(OracleColumn::name)
            .ifPresentOrElse(
                colName -> bw.setPropertyValue(name, ignoreCaseMap.get(colName)),
                () -> {
                  var defaultValue = ignoreCaseMap.get(Strings.underscoreName(name));
                  var value = ignoreCaseMap.getOrDefault(name, defaultValue);
                  bw.setPropertyValue(name, value);
                });
      }

      return mappedObject;
    } catch (Exception e) {

      log.error("Can not map to {}", destinationType, e.getCause());
      return null;
    }
  }

}
