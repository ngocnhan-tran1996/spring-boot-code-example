package com.springboot.code.example.database.entity.manager.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.query.spi.MutableQueryOptions;
import org.hibernate.query.sqm.internal.QuerySqmImpl;
import org.hibernate.query.sqm.sql.internal.StandardSqmTranslator;
import org.hibernate.query.sqm.tree.expression.SqmJpaCriteriaParameterWrapper;
import org.hibernate.sql.ast.SqlAstTranslator;
import org.hibernate.sql.ast.spi.StandardSqlAstTranslator;
import org.hibernate.sql.ast.tree.select.SelectStatement;
import org.hibernate.sql.exec.spi.JdbcParameterBinder;
import org.hibernate.sql.exec.spi.JdbcSelect;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.code.example.common.converter.PropertyConverter;
import com.springboot.code.example.database.entity.manager.entity.CarEntity;
import com.springboot.code.example.database.entity.manager.response.CarResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
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

    QuerySqmImpl<?> querySqmImpl = entityManager.createQuery(criteriaQuery)
        .unwrap(QuerySqmImpl.class);
    final SessionFactoryImplementor sessionFactory = querySqmImpl.getSessionFactory();
    final MutableQueryOptions queryOptions = querySqmImpl.getQueryOptions();
    final StandardSqmTranslator<SelectStatement> sqmTranslator = new StandardSqmTranslator<>(
        querySqmImpl.getSqmStatement(),
        queryOptions,
        querySqmImpl.getDomainParameterXref(),
        querySqmImpl.getQueryParameterBindings(),
        querySqmImpl.getSession().getLoadQueryInfluencers(),
        sessionFactory,
        true);
    final SqlAstTranslator<JdbcSelect> sqlAstTranslator = new StandardSqlAstTranslator<>(
        sessionFactory,
        sqmTranslator.translate().getSqlAst());
    final JdbcSelect jdbcSelect = sqlAstTranslator.translate(null, queryOptions);

    Query nativeQuery = entityManager.createNativeQuery(jdbcSelect.getSql());

    Map<JdbcParameterBinder, Object> valuesByJdbcParam = new HashMap<>();
    sqmTranslator.getJdbcParamsBySqmParam()
        .entrySet()
        .forEach(entry -> entry.getValue()
            .forEach(values -> values.forEach(value -> {

              var key = value.getParameterBinder();
              var object = (SqmJpaCriteriaParameterWrapper<?>) entry.getKey();
              var objectValue = object.getJpaCriteriaParameter()
                  .getValue();
              valuesByJdbcParam.put(key, objectValue);
            })));

    // set parameter
    final List<JdbcParameterBinder> parameterBinders = jdbcSelect.getParameterBinders();
    IntStream.range(0, parameterBinders.size())
        .forEach(index -> {

          Optional.ofNullable(valuesByJdbcParam.get(parameterBinders.get(index)))
              .ifPresent(value -> nativeQuery.setParameter(index + 1, value));
        });

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