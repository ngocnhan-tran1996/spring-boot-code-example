package com.springboot.code.example.transaction.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.code.example.container.DatabaseContainer;
import com.springboot.code.example.transaction.domain.DogEntity;
import com.springboot.code.example.transaction.domain.DogRepo;

@ActiveProfiles("postgres")
@SpringBootTest(classes = DatabaseContainer.class)
@TestMethodOrder(OrderAnnotation.class)
class JpaTest {

  @Autowired
  DogRepo dogRepo;

  @Test
  @Order(1)
  @Transactional
  void testInsertWithTransaction() {

    assertThat(dogRepo.findAll())
        .isEmpty();
    dogRepo.save(new DogEntity(1, "Chihuahua"));
    assertThat(dogRepo.findAll())
        .hasSize(1);
  }

  @Test
  @Order(2)
  void testInsertWithoutTransaction() {

    assertThat(dogRepo.findAll())
        .isEmpty();
    dogRepo.save(new DogEntity(1, "Chihuahua"));
    assertThat(dogRepo.findAll())
        .hasSize(1);
  }

  @Test
  @Order(3)
  @Transactional
  void testUpdateWithTransaction() {

    assertThat(dogRepo.findAll())
        .hasSize(1);

    dogRepo.findById(1)
        .ifPresent(dog -> {

          dog.setSpecies("Anana 1");
          dogRepo.save(dog);
        });

    assertThat(dogRepo.findAll())
        .hasSize(1)
        .extracting(DogEntity::getId, DogEntity::getSpecies)
        .containsExactly(
            tuple(1, "Anana 1"));
  }

  @Test
  @Order(4)
  void testUpdateWithoutTransaction() {

    assertThat(dogRepo.findAll())
        .hasSize(1);

    dogRepo.findById(1)
        .ifPresent(dog -> {

          dog.setSpecies("Anana 1");
          dogRepo.save(dog);
        });

    assertThat(dogRepo.findAll())
        .hasSize(1)
        .extracting(DogEntity::getId, DogEntity::getSpecies)
        .containsExactly(
            tuple(1, "Anana 1"));
  }

  @Test
  @Order(5)
  @Transactional
  void testDeleteWithTransaction() {

    assertThat(dogRepo.findAll())
        .hasSize(1)
        .extracting(DogEntity::getId, DogEntity::getSpecies)
        .containsExactly(
            tuple(1, "Anana 1"));

    dogRepo.deleteAll();
    assertThat(dogRepo.findAll())
        .isEmpty();
  }

  @Test
  @Order(6)
  void testDeleteWithoutTransaction() {

    assertThat(dogRepo.findAll())
        .hasSize(1)
        .extracting(DogEntity::getId, DogEntity::getSpecies)
        .containsExactly(
            tuple(1, "Anana 1"));

    dogRepo.deleteAll();
    assertThat(dogRepo.findAll())
        .isEmpty();
  }

  @Test
  @Order(7)
  void testCount() {

    assertThat(dogRepo.findAll())
        .isEmpty();
  }

}