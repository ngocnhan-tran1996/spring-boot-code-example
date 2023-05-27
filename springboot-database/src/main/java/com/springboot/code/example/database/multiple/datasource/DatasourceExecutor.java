package com.springboot.code.example.database.multiple.datasource;

import java.util.List;
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
import com.springboot.code.example.database.multiple.datasource.entity.BaseEntity;
import com.springboot.code.example.database.multiple.datasource.vehicle.CarEntity;
import com.springboot.code.example.database.multiple.datasource.vehicle.CardRepository;
import com.springboot.code.example.database.multiple.datasource.wild.AnimalEntity;
import com.springboot.code.example.database.multiple.datasource.wild.AnimalRepository;
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
public class DatasourceExecutor {

  private final CardRepository cardRepository;
  private final AnimalRepository animalRepository;

  @PersistenceContext(unitName = "vehicleEntityManager")
  private final EntityManager vehicleEntityManager;

  private static final String AC = "1";

  public void manipulate() {

    this.print("Before udating...");

    // Updating
    var newCar = new CarEntity();
    newCar.setName("BENZ");
    cardRepository.save(newCar);

    var newAnimal = new AnimalEntity();
    newAnimal.setName("DOG");
    animalRepository.save(newAnimal);
    this.print("After udating...");

    // Deleting
    animalRepository.deleteById(1);
    cardRepository.deleteById(1);
    this.print("After deleting...");
  }

  protected void print(String content) {

    log.info(content);
    log.info("Vehicle Database");
    log.info("id | name");
    this.print(cardRepository.findAll());
    log.info("Wild Database");
    log.info("id | name");
    this.print(animalRepository.findAll());
  }

  protected <T extends BaseEntity> void print(Iterable<T> iterable) {

    iterable.forEach(entity -> log.info("{}  | {}", entity.getId(), entity.getName()));
  }

  @Transactional(transactionManager = "vehicleTransactionManager")
  public void logSQL() {

    int t1 = 1;
    int t2 = 1;

    Specification<CarEntity> specification = (root, query, criteriaBuilder) -> {

      return criteriaBuilder.and(
          criteriaBuilder.in(root.get("id")).value(t1),
          root.get("id").in(AC, AC),
          criteriaBuilder.equal(criteriaBuilder.literal(1), t1),
          criteriaBuilder.equal(root.get("id"), t2),
          criteriaBuilder.equal(root.get("id"), t1));
    };

    CriteriaBuilder criteriaBuilder = vehicleEntityManager.getCriteriaBuilder();
    CriteriaQuery<CarDto> criteriaQuery = criteriaBuilder.createQuery(CarDto.class);
    Root<CarEntity> root = criteriaQuery.from(CarEntity.class);
    criteriaQuery.where(specification.toPredicate(root, criteriaQuery, criteriaBuilder));
    criteriaQuery.select(criteriaBuilder.construct(
        CarDto.class,
        criteriaBuilder.selectCase()
            .when(root.get("id").in(t1, t2), String.valueOf(t1))
            .when(root.get("id").in(t1, t2), String.valueOf(t2))
            .when(criteriaBuilder.equal(root.get("id"), AC), String.valueOf(t1))
            .when(criteriaBuilder.equal(root.get("id"), AC), String.valueOf(t2)),
        root.get("name")));

    var querySqm = vehicleEntityManager.createQuery(criteriaQuery)
        .unwrap(QuerySqmImpl.class);

    SqmTranslator<SelectStatement> sqmTranslator = new StandardSqmTranslatorFactory()
        .createSelectTranslator(
            (SqmSelectStatement<?>) querySqm.getSqmStatement(),
            querySqm.getQueryOptions(),
            querySqm.getDomainParameterXref(),
            querySqm.getParameterBindings(),
            querySqm.getLoadQueryInfluencers(),
            querySqm.getSessionFactory(),
            true);

    SqlAstTranslator<JdbcSelect> sqlAstTranslator = new StandardSqlAstTranslatorFactory()
        .buildSelectTranslator(
            querySqm.getSessionFactory(),
            sqmTranslator.translate().getSqlAst());

    List<?> list = querySqm.getDomainParameterXref()
        .getQueryParameters()
        .values()
        .stream()
        .flatMap(List::stream)
        .map(x -> (SqmJpaCriteriaParameterWrapper<?>) x)
        .map(SqmJpaCriteriaParameterWrapper::getJpaCriteriaParameter)
        .map(JpaCriteriaParameter::getValue)
        .toList();

    String sql = sqlAstTranslator.translate(null, querySqm.getQueryOptions())
        .getSql();
    var output = vehicleEntityManager.createNativeQuery(sql);
    for (int i = 0; i < list.size(); i++) {
      output.setParameter(i + 1, list.get(i));
    }

    log.info("query is {}", output.getResultList());
    log.info("query is {}", vehicleEntityManager.createQuery(criteriaQuery).unwrap(
        org.hibernate.query.Query.class).getQueryString());
  }

}