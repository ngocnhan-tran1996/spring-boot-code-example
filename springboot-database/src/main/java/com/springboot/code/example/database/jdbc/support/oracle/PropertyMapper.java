package com.springboot.code.example.database.jdbc.support.oracle;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import com.springboot.code.example.common.helper.Strings;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertyMapper {

  private static final ModelMapper modelMapper = new ModelMapper();

  public static <T> T map(Map<String, Object> source, Class<T> destinationType) {

    try {
      Map<String, Object> fieldSource = source.entrySet()
          .stream()
          .collect(Collectors.toMap(
              entry -> Strings.toLowerCase(entry.getKey()),
              Entry::getValue));
      Map<String, Object> result = new HashMap<>();

      for (Field field : destinationType.getDeclaredFields()) {
        String name = field.getName();

        var oracleColumnAnnotation = field.getAnnotation(OracleColumn.class);
        Optional.ofNullable(oracleColumnAnnotation)
            .filter(annotation -> Strings.isNotBlank(annotation.name()))
            .map(OracleColumn::name)
            .ifPresentOrElse(
                colName -> result.put(name, source.get(colName)),
                () -> {
                  var defaultValue = fieldSource.get(Strings.underscoreName(name));
                  var value = fieldSource.getOrDefault(name, defaultValue);
                  result.put(name, value);
                });
      }

      return modelMapper.map(result, destinationType);
    } catch (Exception e) {

      log.error("Can not map to {}", destinationType, e.getCause());
      return null;
    }
  }
}
