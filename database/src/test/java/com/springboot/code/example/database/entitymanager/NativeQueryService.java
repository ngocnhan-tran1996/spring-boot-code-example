package com.springboot.code.example.database.entitymanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.springboot.code.example.database.domain.NamePrefixResponse;
import com.springboot.code.example.database.support.oracle.mapper.DelegateOracleMapper;
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
          return DelegateOracleMapper.get(NamePrefixResponse.class)
              .convert(map);
        })
        .toList();
  }

}