package io.ngocnhan_tran1996.code.example.transaction.poly.datasource.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.util.Lists.list;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import io.ngocnhan_tran1996.code.example.transaction.domain.DogEntity;
import io.ngocnhan_tran1996.code.example.transaction.domain.DogRepo;
import io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config.ChainedTransactionManagerConfig;
import io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config.OracleDataSourceConfig;
import io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config.PostgresDataSourceConfig;
import io.ngocnhan_tran1996.code.example.transaction.poly.datasource.oracle.CatEntity;
import io.ngocnhan_tran1996.code.example.transaction.poly.datasource.oracle.CatRepo;

@ActiveProfiles("poly-datasource")
@SpringBootTest(classes = {PostgresDataSourceConfig.class, OracleDataSourceConfig.class,
    ChainedTransactionManagerConfig.class, UnexpectedRollbackTransaction.class})
class PolyJpaTransactionTest {

  @Autowired
  DogRepo dogRepo;

  @Autowired
  CatRepo catRepo;

  @Autowired
  UnexpectedRollbackTransaction unexpectedRollbackTransaction;

  @Test
  @Transactional(PostgresDataSourceConfig.TRANSACTION_MANAGER)
  void testSavePostgres() {

    // reset all
    this.dogRepo.deleteAll();

    // create
    dogRepo.insert(1, "Dog 1");
    dogRepo.insert(2, "Dog 2");

    // read
    assertThat(dogRepo.findAll()).hasSize(2);

    // update
    dogRepo.findById(1)
        .ifPresent(dog -> {

          dog.setSpecies("Dog changed");
          dogRepo.save(dog);
        });
    assertThat(dogRepo.findAll()).hasSize(2);

    // delete
    dogRepo.deleteById(2);
    assertThat(dogRepo.findAll())
        .hasSize(1)
        .extracting(DogEntity::getSpecies)
        .isEqualTo(list("Dog changed"));
  }

  @Test
  @Transactional(OracleDataSourceConfig.TRANSACTION_MANAGER)
  void testSaveOracle() {

    // reset all
    this.catRepo.deleteAll();

    // create
    catRepo.insert(1, "Cat 1");
    catRepo.insert(2, "Cat 2");

    // read
    assertThat(catRepo.findAll()).hasSize(2);

    // update
    catRepo.findById(1)
        .ifPresent(cat -> {

          cat.setSpecies("Cat changed");
          catRepo.save(cat);
        });
    assertThat(catRepo.findAll()).hasSize(2);

    // delete
    catRepo.deleteById(2);
    assertThat(catRepo.findAll())
        .hasSize(1)
        .extracting(CatEntity::getSpecies)
        .isEqualTo(list("Cat changed"));
  }

  @Test
  @Transactional(OracleDataSourceConfig.TRANSACTION_MANAGER)
  void testSavePostgresWithOracleTransaction() {

    this.testSavePostgres();
  }

  @Test
  @Transactional(PostgresDataSourceConfig.TRANSACTION_MANAGER)
  void testSaveOracleWithPostgresTransaction() {

    this.testSaveOracle();
  }

  @Test
  @Transactional(OracleDataSourceConfig.TRANSACTION_MANAGER)
  void testSaveAllWithOracleTransaction() {

    this.testSaveOracle();
    this.testSavePostgres();
  }

  @Test
  @Transactional(PostgresDataSourceConfig.TRANSACTION_MANAGER)
  void testSaveAllWithPostgresTransaction() {

    this.testSavePostgres();
    this.testSaveOracle();
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