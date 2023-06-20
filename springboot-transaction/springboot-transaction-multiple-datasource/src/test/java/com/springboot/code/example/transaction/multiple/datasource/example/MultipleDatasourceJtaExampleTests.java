package com.springboot.code.example.transaction.multiple.datasource.example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.code.example.transaction.multiple.datasource.config.JtaTransactionManagerConfig;
import com.springboot.code.example.transaction.multiple.datasource.config.VehicleJtaDatasourceConfig;
import com.springboot.code.example.transaction.multiple.datasource.config.WildJtaDatasourceConfig;
import com.springboot.code.example.transaction.multiple.datasource.config.properties.DataProperties;
import com.springboot.code.example.transaction.multiple.datasource.entity.BaseEntity;
import com.springboot.code.example.transaction.multiple.datasource.jta.vehicle.CarJtaEntity;
import com.springboot.code.example.transaction.multiple.datasource.jta.vehicle.CarJtaRepository;
import com.springboot.code.example.transaction.multiple.datasource.jta.wild.AnimalJtaEntity;
import com.springboot.code.example.transaction.multiple.datasource.jta.wild.AnimalJtaRepository;

@SpringBootTest
@Transactional(transactionManager = "transactionManager")
@Import({
    DataProperties.class,
    VehicleJtaDatasourceConfig.class,
    WildJtaDatasourceConfig.class,
    JtaTransactionManagerConfig.class})
class MultipleDatasourceJtaExampleTests {

  @Autowired
  CarJtaRepository carJtaRepository;

  @Autowired
  AnimalJtaRepository animalJtaRepository;

  MultipleDatasourceJtaExample multipleDatasourceJtaExample;

  private static final List<CarJtaEntity> cars = List.of(
      new CarJtaEntity(1, "Car 1"),
      new CarJtaEntity(2, "Car 2"),
      new CarJtaEntity(3, "Car 3"),
      new CarJtaEntity(4, "Car 4"));

  private static final List<AnimalJtaEntity> animals = List.of(
      new AnimalJtaEntity(1, "Animal 1"),
      new AnimalJtaEntity(2, "Animal 2"),
      new AnimalJtaEntity(3, "Animal 3"),
      new AnimalJtaEntity(4, "Animal 4"));

  @BeforeEach
  void init() {

    multipleDatasourceJtaExample = new MultipleDatasourceJtaExample(
        carJtaRepository,
        animalJtaRepository);
  }

  @Test
  void testGetCars() {

    this.deleteAndSaveCars();

    List<String> expectOutput = cars.stream()
        .map(BaseEntity::getName)
        .toList();

    assertThat(multipleDatasourceJtaExample.getCars())
        .hasSize(5)
        .extracting(BaseEntity::getName)
        .containsAll(expectOutput);
  }

  @Test
  void testGetCarsWithException() {

    this.deleteAndSaveCars();

    assertThatThrownBy(multipleDatasourceJtaExample::getCarsWithException)
        .isInstanceOf(InvalidDataAccessResourceUsageException.class);
  }

  @Test
  void testGetAnimals() {

    this.deleteAndSaveAnimals();

    List<String> expectOutput = animals.stream()
        .map(BaseEntity::getName)
        .toList();

    assertThat(multipleDatasourceJtaExample.getAnimals())
        .hasSize(5)
        .extracting(BaseEntity::getName)
        .containsAll(expectOutput);
  }

  @Test
  void testGetAll() {

    // given
    this.deleteAndSaveCars();
    this.deleteAndSaveAnimals();

    List<BaseEntity> entities = new ArrayList<>(cars);
    entities.addAll(animals);
    List<String> expectOutput = entities.stream()
        .map(BaseEntity::getName)
        .toList();

    assertThat(multipleDatasourceJtaExample.getAll())
        .hasSize(10)
        .extracting(BaseEntity::getName)
        .containsAll(expectOutput);
  }

  @Test
  void testGetAllWithException() {

    // given
    this.deleteAndSaveCars();
    this.deleteAndSaveAnimals();

    // then
    assertThatThrownBy(multipleDatasourceJtaExample::getAllWithException)
        .isInstanceOf(JpaSystemException.class);
  }

  @Test
  void testGetAllWithInvalidDataAccessResourceUsageException() {

    // given
    this.deleteAndSaveCars();
    this.deleteAndSaveAnimals();

    assertThatThrownBy(
        multipleDatasourceJtaExample::getAllWithInvalidDataAccessResourceUsageException)
            .isInstanceOf(InvalidDataAccessResourceUsageException.class);
  }

  void deleteAndSaveCars() {

    carJtaRepository.deleteAll();
    carJtaRepository.saveAll(cars);
  }

  void deleteAndSaveAnimals() {

    animalJtaRepository.deleteAll();
    animalJtaRepository.saveAll(animals);
  }

}