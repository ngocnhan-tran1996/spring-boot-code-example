package com.springboot.code.example.database.multiple.datasource;

import java.util.Collections;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory;
import org.hibernate.hql.spi.QueryTranslator;
import org.hibernate.hql.spi.QueryTranslatorFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.code.example.database.multiple.datasource.entity.BaseEntity;
import com.springboot.code.example.database.multiple.datasource.vehicle.CarEntity;
import com.springboot.code.example.database.multiple.datasource.vehicle.CardRepository;
import com.springboot.code.example.database.multiple.datasource.wild.AnimalEntity;
import com.springboot.code.example.database.multiple.datasource.wild.AnimalRepository;
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

    CriteriaBuilder criteriaBuilder = vehicleEntityManager.getCriteriaBuilder();
    CriteriaQuery<CarEntity> criteriaQuery = criteriaBuilder.createQuery(CarEntity.class);
    Root<CarEntity> book = criteriaQuery.from(CarEntity.class);

    var query = vehicleEntityManager.createQuery(criteriaQuery)
        .unwrap(org.hibernate.query.Query.class);

    String hqlQueryString = query.getQueryString();
    SessionImplementor hibernateSession = vehicleEntityManager.unwrap(SessionImplementor.class);

    QueryTranslatorFactory queryTranslatorFactory = new ASTQueryTranslatorFactory();
    QueryTranslator queryTranslator = queryTranslatorFactory.createQueryTranslator(
        "",
        hqlQueryString,
        Collections.emptyMap(),
        hibernateSession.getFactory(),
        null);
    queryTranslator.compile(Collections.emptyMap(), false);
    log.info("query is {}", queryTranslator.getSQLString());
  }

}