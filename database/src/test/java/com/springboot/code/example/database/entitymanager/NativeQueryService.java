package com.springboot.code.example.database.entitymanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.LinkedCaseInsensitiveMap;
import com.springboot.code.example.database.domain.name.NamePrefixResponse;
import com.springboot.code.example.utils.Strings;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class NativeQueryService {

  @PersistenceContext
  private final EntityManager entityManager;

  @SuppressWarnings("unchecked")
  List<NamePrefixResponse> nativeQueryNamePrefix() {

    var query = entityManager.createNativeQuery(
        "SELECT n.* FROM NAME_PREFIX n WHERE n.id < ?",
        Tuple.class);
    query.setParameter(1, "10");
    return ((List<Tuple>) query.getResultList())
        .stream()
        .map(tuple -> {

          Map<String, Object> map = new HashMap<>();
          tuple.getElements().forEach(element -> map.put(
              element.getAlias(),
              tuple.get(element.getAlias())));
          return convert(map, NamePrefixResponse.class);
        })
        .toList();
  }

  private static <T> T convert(Map<String, Object> source, Class<T> destinationType) {

    Assert.notNull(source, "source");
    Assert.notNull(destinationType, "destinationType");

    try {
      Map<String, Object> ignoreCaseMap = new LinkedCaseInsensitiveMap<>(source.size());
      ignoreCaseMap.putAll(source);

      // copy from DataClassRowMapper
      var typeConverter = new SimpleTypeConverter();
      var mappedConstructor = BeanUtils.getResolvableConstructor(destinationType);
      var constructorParameterNames = BeanUtils.getParameterNames(mappedConstructor);

      int paramCount = mappedConstructor.getParameterCount();
      var constructorParameterTypes = new TypeDescriptor[paramCount];
      for (int i = 0; i < paramCount; i++) {
        constructorParameterTypes[i] = new TypeDescriptor(
            new MethodParameter(mappedConstructor, i));
      }

      Object[] args = new Object[constructorParameterNames.length];
      for (int i = 0; i < args.length; i++) {

        String name = constructorParameterNames[i];
        var defaultValue = ignoreCaseMap.get(underscoreName(name));
        var value = ignoreCaseMap.getOrDefault(name, defaultValue);

        TypeDescriptor td = constructorParameterTypes[i];
        args[i] = typeConverter.convertIfNecessary(value, td.getType(), td);
      }

      return BeanUtils.instantiateClass(mappedConstructor, args);
    } catch (Exception e) {

      return null;
    }
  }

  private static String underscoreName(String name) {

    if (Strings.isBlank(name)) {

      return Strings.EMPTY;
    }

    var result = new StringBuilder();
    result.append(Character.toLowerCase(name.charAt(0)));

    for (int i = 1; i < name.length(); i++) {

      char c = name.charAt(i);

      if (Character.isUpperCase(c)) {

        result.append('_').append(Character.toLowerCase(c));
        continue;
      }

      result.append(c);
    }

    return result.toString();
  }

}