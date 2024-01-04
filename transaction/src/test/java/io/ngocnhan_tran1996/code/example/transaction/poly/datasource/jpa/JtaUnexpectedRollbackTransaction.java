package io.ngocnhan_tran1996.code.example.transaction.poly.datasource.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.transaction.annotation.Transactional;
import io.ngocnhan_tran1996.code.example.transaction.poly.jta.datasource.oracle.CatRepo;
import io.ngocnhan_tran1996.code.example.transaction.poly.jta.datasource.postgres.DogJtaRepo;

@TestConfiguration
class JtaUnexpectedRollbackTransaction {

  @Autowired
  DogJtaRepo dogRepo;

  @Autowired
  CatRepo catRepo;

  @Transactional(value = "jtaTransactionManager")
  void saveAllWithChainedTransactionWithException() {

    // reset all
    this.dogRepo.deleteAll();
    this.catRepo.deleteAll();

    // create
    this.dogRepo.insert(1, "Dog 1");
    catRepo.insert(1, "Cat 1");
    catRepo.insert(2, "Cat 2");

    // update
    catRepo.findById(1)
        .ifPresent(cat -> {

          cat.setSpecies("Cat changed");
          catRepo.save(cat);
        });

    // delete
    catRepo.deleteById(2);

    // intend to throw exception
    this.catRepo.insert();
  }

}