package io.ngocnhan_tran1996.code.example.transaction.poly.datasource.entitymanager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import io.ngocnhan_tran1996.code.example.transaction.domain.DogEntity;
import io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config.ChainedTransactionManagerConfig;
import io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config.OracleDataSourceConfig;
import io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config.PostgresDataSourceConfig;
import io.ngocnhan_tran1996.code.example.transaction.poly.datasource.oracle.CatEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TransactionRequiredException;

@ActiveProfiles("poly-datasource")
@SpringBootTest(classes = {PostgresDataSourceConfig.class, OracleDataSourceConfig.class,
    ChainedTransactionManagerConfig.class, UnexpectedRollbackTransaction.class})
class PolyEntityManagerTransactionTest {

  @PersistenceContext
  EntityManager entityManager;

  @PersistenceContext(unitName = OracleDataSourceConfig.PERSISTENCE_UNIT)
  EntityManager oracleEntityManager;

  @Autowired
  UnexpectedRollbackTransaction unexpectedRollbackTransaction;

  @Test
  @Transactional(PostgresDataSourceConfig.TRANSACTION_MANAGER)
  void testSavePostgres() {

    // reset all
    this.entityManager.createQuery("DELETE FROM DogEntity")
        .executeUpdate();
    assertThat(this.entityManager.createQuery("FROM DogEntity", DogEntity.class).getResultList())
        .isEmpty();

    // create
    this.entityManager.createQuery("INSERT INTO DogEntity (id, species) VALUES (?1, ?2)")
        .setParameter(1, 1)
        .setParameter(2, "Dog 1")
        .executeUpdate();
    this.entityManager.createQuery("INSERT INTO DogEntity (id, species) VALUES (?1, ?2)")
        .setParameter(1, 2)
        .setParameter(2, "Dog 2")
        .executeUpdate();

    // read
    assertThat(this.entityManager.createQuery("FROM DogEntity", DogEntity.class).getResultList())
        .hasSize(2);

    // update
    this.entityManager.createQuery("UPDATE DogEntity SET species = ?1 WHERE id = ?2")
        .setParameter(1, "Dog Changed")
        .setParameter(2, 1)
        .executeUpdate();

    // delete
    this.entityManager.createQuery("DELETE FROM DogEntity WHERE id = ?1")
        .setParameter(1, 1)
        .executeUpdate();

    assertThat(this.entityManager.createQuery("FROM DogEntity", DogEntity.class).getResultList())
        .hasSize(1);
  }

  @Test
  @Transactional(OracleDataSourceConfig.TRANSACTION_MANAGER)
  void testSaveOracle() {

    // reset all
    this.oracleEntityManager.createQuery("DELETE FROM CatEntity")
        .executeUpdate();
    assertThat(this.oracleEntityManager.createQuery("FROM CatEntity", CatEntity.class)
        .getResultList())
            .isEmpty();

    // create
    this.oracleEntityManager.createQuery(
        "INSERT INTO CatEntity (id, species) VALUES (?1, ?2)")
        .setParameter(1, 1)
        .setParameter(2, "Cat 1")
        .executeUpdate();
    this.oracleEntityManager.createQuery("INSERT INTO CatEntity (id, species) VALUES (?1, ?2)")
        .setParameter(1, 2)
        .setParameter(2, "Cat 2")
        .executeUpdate();

    // read
    assertThat(this.oracleEntityManager.createQuery("FROM CatEntity", CatEntity.class)
        .getResultList())
            .hasSize(2);

    // update
    this.oracleEntityManager.createQuery("UPDATE CatEntity SET species = ?1 WHERE id = ?2")
        .setParameter(1, "Cat Changed")
        .setParameter(2, 1)
        .executeUpdate();

    // delete
    this.oracleEntityManager.createQuery("DELETE FROM CatEntity WHERE id = ?1")
        .setParameter(1, 1)
        .executeUpdate();

    assertThat(this.oracleEntityManager.createQuery("FROM CatEntity", DogEntity.class)
        .getResultList())
            .hasSize(1);
  }

  @Test
  @Transactional(OracleDataSourceConfig.TRANSACTION_MANAGER)
  void testSavePostgresWithOracleTransaction() {

    assertThatExceptionOfType(TransactionRequiredException.class)
        .isThrownBy(this::testSavePostgres);
  }

  @Test
  @Transactional(PostgresDataSourceConfig.TRANSACTION_MANAGER)
  void testSaveOracleWithPostgresTransaction() {

    assertThatExceptionOfType(TransactionRequiredException.class)
        .isThrownBy(this::testSaveOracle);
  }

  @Test
  @Transactional(OracleDataSourceConfig.TRANSACTION_MANAGER)
  void testSaveAllWithOracleTransaction() {

    this.testSaveOracle();
    assertThatExceptionOfType(TransactionRequiredException.class)
        .isThrownBy(this::testSavePostgres);
  }

  @Test
  @Transactional(PostgresDataSourceConfig.TRANSACTION_MANAGER)
  void testSaveAllWithPostgresTransaction() {

    this.testSavePostgres();
    assertThatExceptionOfType(TransactionRequiredException.class)
        .isThrownBy(this::testSaveOracle);
  }

  @Test
  @Transactional("chainedTransactionManager")
  void testSaveAllWithChainedTransaction() {

    this.testSavePostgres();
    this.testSaveOracle();
  }

  @Test
  @Transactional(value = "chainedTransactionManager", propagation = Propagation.NOT_SUPPORTED)
  void testSaveAllWithChainedTransactionWithException() {

    assertThatExceptionOfType(UnexpectedRollbackException.class)
        .isThrownBy(this.unexpectedRollbackTransaction::saveAllWithChainedTransactionWithException);
  }

}