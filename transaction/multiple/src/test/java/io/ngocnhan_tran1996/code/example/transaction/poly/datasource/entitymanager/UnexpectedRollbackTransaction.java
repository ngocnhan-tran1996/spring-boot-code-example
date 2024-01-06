package io.ngocnhan_tran1996.code.example.transaction.poly.datasource.entitymanager;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.transaction.annotation.Transactional;
import io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config.OracleDataSourceConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@TestConfiguration
class UnexpectedRollbackTransaction {

  @PersistenceContext
  EntityManager entityManager;

  @PersistenceContext(unitName = OracleDataSourceConfig.PERSISTENCE_UNIT)
  EntityManager oracleEntityManager;

  @Transactional(value = "chainedTransactionManager")
  void saveAllWithChainedTransactionWithException() {

    // reset all
    this.entityManager.createQuery("DELETE FROM DogEntity")
        .executeUpdate();
    this.oracleEntityManager.createQuery("DELETE FROM CatEntity")
        .executeUpdate();

    // create
    this.entityManager.createQuery("INSERT INTO DogEntity (id, species) VALUES (?1, ?2)")
        .setParameter(1, 1)
        .setParameter(2, "Dog 1")
        .executeUpdate();
    this.entityManager.createQuery("INSERT INTO DogEntity (id, species) VALUES (?1, ?2)")
        .setParameter(1, 2)
        .setParameter(2, "Dog 2")
        .executeUpdate();
    this.oracleEntityManager.createQuery(
        "INSERT INTO CatEntity (id, species) VALUES (?1, ?2)")
        .setParameter(1, 1)
        .setParameter(2, "Cat 1")
        .executeUpdate();
    this.oracleEntityManager.createQuery("INSERT INTO CatEntity (id, species) VALUES (?1, ?2)")
        .setParameter(1, 2)
        .setParameter(2, "Cat 2")
        .executeUpdate();

    // update
    this.oracleEntityManager.createQuery("UPDATE CatEntity SET species = ?1 WHERE id = ?2")
        .setParameter(1, "Cat Changed")
        .setParameter(2, 1)
        .executeUpdate();

    // delete
    this.oracleEntityManager.createQuery("DELETE FROM CatEntity WHERE id = ?1")
        .setParameter(1, 1)
        .executeUpdate();

    // intend to throw exception
    this.oracleEntityManager.createNativeQuery("INSERT INTO CAT VALUES (2, 'Test');COMMIT")
        .executeUpdate();
  }

}