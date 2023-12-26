package com.springboot.code.example.database.entitymanager;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.hibernate.query.sqm.internal.QuerySqmImpl;
import org.hibernate.query.sqm.sql.SqmTranslator;
import org.hibernate.query.sqm.sql.StandardSqmTranslatorFactory;
import org.hibernate.query.sqm.tree.expression.SqmJpaCriteriaParameterWrapper;
import org.hibernate.query.sqm.tree.select.SqmSelectStatement;
import org.hibernate.sql.ast.SqlAstTranslator;
import org.hibernate.sql.ast.spi.StandardSqlAstTranslatorFactory;
import org.hibernate.sql.ast.tree.select.SelectStatement;
import org.hibernate.sql.exec.spi.JdbcOperationQuerySelect;
import org.hibernate.sql.exec.spi.JdbcParameterBinder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.code.example.database.domain.name.NamePrefixEntity;
import com.springboot.code.example.database.domain.name.NamePrefixEntity_;
import com.springboot.code.example.database.domain.name.NamePrefixResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TypedQueryPaginationService {

  @PersistenceContext
  private final EntityManager entityManager;

  List<NamePrefixResponse> queryNamePrefix() {

    var query = entityManager.createQuery(criteriaQuery());
    query.setFirstResult(0);
    query.setMaxResults(10);
    return query.getResultList();
  }

  /**
   * count typed query, but it is not like pagination query which we want
   * Like {@code SELECT COUNT(*) FROM ( MY_QUERY )}
   * 
   * @return {@code long} total records
   */
  long countQueryNamePrefix() {

    var criteriaBuilder = this.entityManager.getCriteriaBuilder();
    var countQuery = criteriaBuilder.createQuery(Long.class);
    var countRoot = countQuery.from(NamePrefixEntity.class);

    var subQuery = countQuery.subquery(Long.class);
    var subRoot = subQuery.from(NamePrefixEntity.class);
    subQuery
        .select(criteriaBuilder.min(subRoot.get(NamePrefixEntity_.ID)))
        .where(criteriaBuilder.lessThan(subRoot.get(NamePrefixEntity_.ID), 10))
        .groupBy(
            subRoot.get(NamePrefixEntity_.ID),
            subRoot.get(NamePrefixEntity_.FIRST_NAME),
            subRoot.get(NamePrefixEntity_.LAST_NAME),
            subRoot.get(NamePrefixEntity_.AGE));

    countQuery.where(criteriaBuilder.in(countRoot.get(NamePrefixEntity_.ID)).value(subQuery));
    countQuery.select(criteriaBuilder.count(countRoot.get(NamePrefixEntity_.ID)));
    return entityManager.createQuery(countQuery)
        .getSingleResult();
  }

  long extractQueryNamePrefix() {

    final QuerySqmImpl<?> querySqmImpl = this.entityManager.createQuery(criteriaQuery())
        .unwrap(QuerySqmImpl.class);

    // copy from ConcreteSqmSelectQueryPlan
    final var sessionFactory = querySqmImpl.getSessionFactory();
    final var queryOptions = querySqmImpl.getQueryOptions();
    final var domainParameterXref = querySqmImpl.getDomainParameterXref();
    final SqmTranslator<SelectStatement> sqmTranslator = new StandardSqmTranslatorFactory()
        .createSelectTranslator(
            (SqmSelectStatement<?>) querySqmImpl.getSqmStatement(),
            queryOptions,
            domainParameterXref,
            querySqmImpl.getQueryParameterBindings(),
            querySqmImpl.getSession().getLoadQueryInfluencers(),
            sessionFactory,
            true);
    final SqlAstTranslator<JdbcOperationQuerySelect> sqlAstTranslator =
        new StandardSqlAstTranslatorFactory()
            .buildSelectTranslator(sessionFactory, sqmTranslator.translate().getSqlAst());
    final JdbcOperationQuerySelect jdbcSelect = sqlAstTranslator.translate(null, queryOptions);

    // arrange params
    final List<JdbcParameterBinder> parameterBinders = jdbcSelect.getParameterBinders();
    final Map<JdbcParameterBinder, Integer> indexByparameterBinders = IntStream.range(0,
        parameterBinders.size())
        .boxed()
        .collect(Collectors.toMap(
            index -> parameterBinders.get(index),
            Function.identity()));

    // set params
    var nativeQuery = this.entityManager.createNativeQuery(jdbcSelect.getSqlString());
    sqmTranslator.getJdbcParamsBySqmParam()
        .entrySet()
        .stream()
        .forEach(entry -> entry.getValue()
            .forEach(values -> values
                .stream()
                .filter(value -> indexByparameterBinders.containsKey(value.getParameterBinder()))
                .findFirst()
                .ifPresent(value -> {

                  var index = indexByparameterBinders.get(value.getParameterBinder());
                  var object = (SqmJpaCriteriaParameterWrapper<?>) entry.getKey();
                  var objectValue = object.getJpaCriteriaParameter()
                      .getValue();
                  nativeQuery.setParameter(index + 1, objectValue);
                })));

    return nativeQuery.getResultList().size();
  }

  private CriteriaQuery<NamePrefixResponse> criteriaQuery() {

    var criteriaBuilder = this.entityManager.getCriteriaBuilder();
    var criteriaQuery = criteriaBuilder.createQuery(NamePrefixResponse.class);
    var root = criteriaQuery.from(NamePrefixEntity.class);
    return criteriaQuery
        .select(
            criteriaBuilder.construct(
                NamePrefixResponse.class,
                root.get(NamePrefixEntity_.FIRST_NAME),
                root.get(NamePrefixEntity_.LAST_NAME),
                root.get(NamePrefixEntity_.AGE)))
        .where(criteriaBuilder.lessThan(root.get(NamePrefixEntity_.ID), 10))
        .groupBy(
            root.get(NamePrefixEntity_.FIRST_NAME),
            root.get(NamePrefixEntity_.LAST_NAME),
            root.get(NamePrefixEntity_.AGE));
  }

}