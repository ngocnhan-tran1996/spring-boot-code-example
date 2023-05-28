package com.springboot.code.example.database.entity.manager.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import org.hibernate.query.sqm.internal.QuerySqmImpl;
import org.hibernate.query.sqm.sql.SqmTranslator;
import org.hibernate.query.sqm.sql.StandardSqmTranslatorFactory;
import org.hibernate.query.sqm.tree.expression.JpaCriteriaParameter;
import org.hibernate.query.sqm.tree.expression.SqmJpaCriteriaParameterWrapper;
import org.hibernate.query.sqm.tree.select.SqmSelectStatement;
import org.hibernate.sql.ast.SqlAstTranslator;
import org.hibernate.sql.ast.spi.StandardSqlAstTranslatorFactory;
import org.hibernate.sql.ast.tree.select.SelectStatement;
import org.hibernate.sql.exec.spi.JdbcSelect;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.code.example.common.converter.PropertyConverter;
import com.springboot.code.example.database.entity.manager.entity.CarEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EntityManagerExample {

  @PersistenceContext
  private final EntityManager entityManager;

  public List<CarEntity> toNativeQuery() {

    Specification<CarEntity> specification = (root, query, criteriaBuilder) -> {

      return criteriaBuilder.equal(root.get("id"), 2);
    };

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CarEntity> criteriaQuery = criteriaBuilder.createQuery(CarEntity.class);
    Root<CarEntity> root = criteriaQuery.from(CarEntity.class);
    criteriaQuery.where(specification.toPredicate(root, criteriaQuery, criteriaBuilder));
    var query = entityManager.createQuery(criteriaQuery);

    // get native query from CriteriaQuery
    var querySqmImpl = query.unwrap(QuerySqmImpl.class);

    SqmTranslator<SelectStatement> sqmTranslator = new StandardSqmTranslatorFactory()
        .createSelectTranslator(
            (SqmSelectStatement<?>) querySqmImpl.getSqmStatement(),
            querySqmImpl.getQueryOptions(),
            querySqmImpl.getDomainParameterXref(),
            querySqmImpl.getParameterBindings(),
            querySqmImpl.getLoadQueryInfluencers(),
            querySqmImpl.getSessionFactory(),
            true);

    SqlAstTranslator<JdbcSelect> sqlAstTranslator = new StandardSqlAstTranslatorFactory()
        .buildSelectTranslator(
            querySqmImpl.getSessionFactory(),
            sqmTranslator.translate().getSqlAst());

    String sql = sqlAstTranslator.translate(null, querySqmImpl.getQueryOptions())
        .getSql();
    var nativeQuery = entityManager.createNativeQuery(sql, Tuple.class);
    List<?> parameters = querySqmImpl.getDomainParameterXref()
        .getQueryParameters()
        .values()
        .stream()
        .flatMap(List::stream)
        .map(x -> (SqmJpaCriteriaParameterWrapper<?>) x)
        .map(SqmJpaCriteriaParameterWrapper::getJpaCriteriaParameter)
        .map(JpaCriteriaParameter::getValue)
        .toList();
    IntStream.range(0, parameters.size())
        .forEach(i -> nativeQuery.setParameter(i + 1, parameters.get(i)));

    @SuppressWarnings("unchecked")
    List<Tuple> tuples = nativeQuery.getResultList();
    return tuples.stream()
        .map(tuple -> {
          Map<String, Object> map = new HashMap<>();
          tuple.getElements().forEach(element -> map.put(element.getAlias(), tuple.get(element
              .getAlias())));
          return PropertyConverter.convert(map, CarEntity.class);
        })
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
          tuple.getElements().forEach(element -> map.put(element.getAlias(), tuple.get(element
              .getAlias())));
          return PropertyConverter.convert(map, CarEntity.class);
        })
        .toList();
  }

}