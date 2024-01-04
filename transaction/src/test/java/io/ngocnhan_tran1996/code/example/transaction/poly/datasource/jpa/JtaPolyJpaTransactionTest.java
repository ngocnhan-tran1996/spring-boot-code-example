package io.ngocnhan_tran1996.code.example.transaction.poly.datasource.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.util.Lists.list;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config.JtaOracleDataSourceConfig;
import io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config.JtaPostgresDataSourceConfig;
import io.ngocnhan_tran1996.code.example.transaction.poly.datasource.config.TransactionManagerConfig;
import io.ngocnhan_tran1996.code.example.transaction.poly.jta.datasource.oracle.CatEntity;
import io.ngocnhan_tran1996.code.example.transaction.poly.jta.datasource.oracle.CatRepo;
import io.ngocnhan_tran1996.code.example.transaction.poly.jta.datasource.postgres.DogJtaEntity;
import io.ngocnhan_tran1996.code.example.transaction.poly.jta.datasource.postgres.DogJtaRepo;

@ActiveProfiles("poly-datasource")
@SpringBootTest(classes = {
    JtaPostgresDataSourceConfig.class,
    JtaOracleDataSourceConfig.class,
    JtaUnexpectedRollbackTransaction.class,
    TransactionManagerConfig.class})
class JtaPolyJpaTransactionTest {

  @Autowired
  DogJtaRepo dogRepo;

  @Autowired
  CatRepo catRepo;

  @Autowired
  JtaUnexpectedRollbackTransaction unexpectedRollbackTransaction;

  @Test
  @Transactional("jtaTransactionManager")
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
        .extracting(DogJtaEntity::getSpecies)
        .isEqualTo(list("Dog changed"));
  }

  @Test
  @Transactional("jtaTransactionManager")
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
  @Transactional("jtaTransactionManager")
  void testSaveAllWithJtaTransaction() {

    this.testSavePostgres();
    this.testSaveOracle();
  }

  @Test
  @Transactional(value = "jtaTransactionManager")
  void testSaveAllWithJtaTransactionWithException() {

    assertThatExceptionOfType(JpaSystemException.class)
        .isThrownBy(this.unexpectedRollbackTransaction::saveAllWithChainedTransactionWithException);
  }

}