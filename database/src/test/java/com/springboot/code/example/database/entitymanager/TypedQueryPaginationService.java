package com.springboot.code.example.database.entitymanager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory;
import org.hibernate.hql.spi.QueryTranslatorFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.code.example.database.domain.name.NamePrefixEntity;
import com.springboot.code.example.database.domain.name.NamePrefixEntity_;
import com.springboot.code.example.database.domain.name.NamePrefixResponse;
import com.springboot.code.example.utils.Strings;
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

  public long extractQueryNamePrefix() {

    final var query = entityManager.createQuery(criteriaQuery())
        .unwrap(Query.class);

    String hqlQueryString = query.getQueryString();
    QueryTranslatorFactory queryTranslatorFactory = new ASTQueryTranslatorFactory();
    var queryTranslator = queryTranslatorFactory.createQueryTranslator(
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