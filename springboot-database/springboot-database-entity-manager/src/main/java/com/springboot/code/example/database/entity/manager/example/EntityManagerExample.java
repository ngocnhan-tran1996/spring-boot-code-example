package com.springboot.code.example.database.entity.manager.example;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory;
import org.hibernate.hql.spi.QueryTranslator;
import org.hibernate.hql.spi.QueryTranslatorFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.code.example.common.converter.PropertyConverter;
import com.springboot.code.example.common.helper.Strings;
import com.springboot.code.example.database.entity.manager.entity.CarEntity;
import com.springboot.code.example.database.entity.manager.response.CarResponse;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EntityManagerExample {

  @PersistenceContext
  private final EntityManager entityManager;

  @SuppressWarnings("unchecked")
  public List<CarResponse> convertTypedQueryToNativeQuery() {

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CarEntity> criteriaQuery = criteriaBuilder.createQuery(CarEntity.class);
    Root<CarEntity> root = criteriaQuery.from(CarEntity.class);
    Expression<Boolean> expression = criteriaBuilder.notEqual(root.get("id"), 0);
    criteriaQuery.select(
        criteriaBuilder.construct(
            CarEntity.class,
            criteriaBuilder.selectCase()
                .when(expression, root.get("id"))
                .otherwise(4),
            criteriaBuilder.selectCase()
                .when(expression, root.get("name"))
                .otherwise("6")));

    var query = entityManager.createQuery(criteriaQuery)
        .unwrap(Query.class);

    String hqlQueryString = query.getQueryString();
    QueryTranslatorFactory queryTranslatorFactory = new ASTQueryTranslatorFactory();
    QueryTranslator queryTranslator = queryTranslatorFactory.createQueryTranslator(
        Strings.EMPTY,
        hqlQueryString,
        Collections.emptyMap(),
        entityManager.unwrap(SessionImplementor.class).getFactory(),
        null);
    queryTranslator.compile(Collections.emptyMap(), false);

    String sqlQueryString = queryTranslator.getSQLString();

    var nativeQuery = entityManager.createNativeQuery(sqlQueryString);

    // set parameter
    query.getParameterMetadata()
        .getNamedParameters()
        .forEach(namedParameter -> Arrays.stream(namedParameter.getSourceLocations())
            .forEach(location -> nativeQuery.setParameter(
                location + 1,
                query.getParameterValue(namedParameter.getName()))));

    return ((List<Object[]>) nativeQuery.getResultList())
        .stream()
        .map(obj -> new CarResponse(((Number) obj[0]).intValue(), (String) obj[1]))
        .toList();
  }

  public List<CarEntity> saveAndFindAll() {

    entityManager.createNativeQuery(
        "INSERT INTO {h-schema}CAR(NAME) VALUES ('TEST')")
        .executeUpdate();

    String query = "SELECT id, name FROM {h-schema}CAR";

    @SuppressWarnings("unchecked")
    List<Tuple> tuples = entityManager.createNativeQuery(query, Tuple.class)
        .getResultList();
    return tuples.stream()
        .map(tuple -> {
          Map<String, Object> map = new HashMap<>();
          tuple.getElements().forEach(element -> map.put(
              element.getAlias(),
              tuple.get(element.getAlias())));
          return PropertyConverter.convert(map, CarEntity.class);
        })
        .toList();
  }

}