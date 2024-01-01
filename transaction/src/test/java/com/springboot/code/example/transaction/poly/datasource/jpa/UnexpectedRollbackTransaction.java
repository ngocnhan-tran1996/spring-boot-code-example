package com.springboot.code.example.transaction.poly.datasource.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.code.example.transaction.domain.DogRepo;
import com.springboot.code.example.transaction.poly.datasource.oracle.CatRepo;

@Service
class UnexpectedRollbackTransaction {

  @Autowired
  DogRepo dogRepo;

  @Autowired
  CatRepo catRepo;

  void savePostgres() {

    // reset all
    this.dogRepo.deleteAll();

    // create
    dogRepo.insert(1, "Dog 1");
    dogRepo.insert(2, "Dog 2");

    // update
    dogRepo.findById(1)
        .ifPresent(dog -> {

          dog.setSpecies("Dog changed");
          dogRepo.save(dog);
        });

    // delete
    dogRepo.deleteById(2);
  }

  void saveOracle() {

    // reset all
    this.catRepo.deleteAll();

    // create
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
  }

  @Transactional(value = "chainedTransactionManager")
  void saveAllWithChainedTransactionWithException() {

    this.savePostgres();
    this.saveOracle();
    this.catRepo.insert();
  }

}