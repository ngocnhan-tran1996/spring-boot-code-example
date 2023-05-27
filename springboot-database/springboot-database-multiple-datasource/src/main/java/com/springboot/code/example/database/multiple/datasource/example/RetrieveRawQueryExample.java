package com.springboot.code.example.database.multiple.datasource.example;

import java.util.List;
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
import com.springboot.code.example.database.multiple.datasource.vehicle.CarEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class RetrieveRawQueryExample {

  @PersistenceContext(unitName = "vehicleEntityManager")
  private final EntityManager vehicleEntityManager;

  @Transactional(transactionManager = "vehicleTransactionManager")
  public void logSQL() {

    Specification<CarEntity> specification = (root, query, criteriaBuilder) -> {

      return criteriaBuilder.equal(root.get("id"), 1);
    };

    CriteriaBuilder criteriaBuilder = vehicleEntityManager.getCriteriaBuilder();
    CriteriaQuery<CarEntity> criteriaQuery = criteriaBuilder.createQuery(CarEntity.class);
    Root<CarEntity> root = criteriaQuery.from(CarEntity.class);
    criteriaQuery.where(specification.toPredicate(root, criteriaQuery, criteriaBuilder));
    var query = vehicleEntityManager.createQuery(criteriaQuery);

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
    var nativeQuery = vehicleEntityManager.createNativeQuery(sql);
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

    log.info("query is {}", query.getResultList());
    log.info("query is {}", nativeQuery.getResultList());
  }

}