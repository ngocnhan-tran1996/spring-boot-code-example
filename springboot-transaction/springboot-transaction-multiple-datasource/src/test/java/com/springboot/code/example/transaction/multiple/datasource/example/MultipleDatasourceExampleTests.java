package com.springboot.code.example.transaction.multiple.datasource.example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.code.example.common.helper.Strings;
import com.springboot.code.example.transaction.multiple.datasource.config.MultipleDatasourceConfig;
import com.springboot.code.example.transaction.multiple.datasource.config.VehicleDatasourceConfig;
import com.springboot.code.example.transaction.multiple.datasource.config.WildDatasourceConfig;
import com.springboot.code.example.transaction.multiple.datasource.entity.BaseEntity;
import com.springboot.code.example.transaction.multiple.datasource.vehicle.CarEntity;
import com.springboot.code.example.transaction.multiple.datasource.vehicle.CarRepository;
import com.springboot.code.example.transaction.multiple.datasource.wild.AnimalEntity;
import com.springboot.code.example.transaction.multiple.datasource.wild.AnimalRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import({
    VehicleDatasourceConfig.class,
    WildDatasourceConfig.class,
    MultipleDatasourceConfig.class})
class MultipleDatasourceExampleTests {

  @Autowired
  CarRepository carRepository;

  @Autowired
  AnimalRepository animalRepository;

  MultipleDatasourceExample multipleDatasourceExample;

  private static final List<CarEntity> cars = List.of(
      new CarEntity(1, "Car 1"),
      new CarEntity(2, "Car 2"),
      new CarEntity(3, "Car 3"),
      new CarEntity(4, "Car 4"));

  private static final List<AnimalEntity> animals = List.of(
      new AnimalEntity(1, "Animal 1"),
      new AnimalEntity(2, "Animal 2"),
      new AnimalEntity(3, "Animal 3"),
      new AnimalEntity(4, "Animal 4"));

  @BeforeEach
  void init() {

    multipleDatasourceExample = new MultipleDatasourceExample(
        carRepository,
        animalRepository);
  }

  @Test
  @Transactional(value = "vehicleTransactionManager")
  void testGetCars() {

    this.deleteAndSaveCars();

    List<String> expectOutput = cars.stream()
        .map(BaseEntity::getName)
        .toList();

    assertThat(multipleDatasourceExample.getCars())
        .hasSize(5)
        .extracting(BaseEntity::getName)
        .containsAll(expectOutput);
  }

  @Test
  @Transactional(value = "wildTransactionManager")
  void testGetAnimals() {

    this.deleteAndSaveAnimals();

    List<String> expectOutput = animals.stream()
        .map(BaseEntity::getName)
        .toList();

    assertThat(multipleDatasourceExample.getAnimals())
        .hasSize(5)
        .extracting(BaseEntity::getName)
        .containsAll(expectOutput);
  }

  @Test
  @Transactional(value = "chainedTransactionManager")
  void testGetAll() {

    // given
    this.deleteAndSaveCars();
    this.deleteAndSaveAnimals();

    List<BaseEntity> entities = new ArrayList<>(cars);
    entities.addAll(animals);
    List<String> expectOutput = entities.stream()
        .map(BaseEntity::getName)
        .toList();

    assertThat(multipleDatasourceExample.getAll())
        .hasSize(10)
        .extracting(BaseEntity::getName)
        .containsAll(expectOutput);
  }

  @Test
  @Transactional(value = "chainedTransactionManager", propagation = Propagation.NOT_SUPPORTED)
  void testGetAllWithException() {

    // given
    this.deleteAndSaveCars();
    this.deleteAndSaveAnimals();

    assertThatThrownBy(multipleDatasourceExample::getAllWithException)
        .isInstanceOf(JpaSystemException.class);
  }

  @AfterEach
  void afterTransaction() {

    log.info("After tranasction...");
    log.info("Cars are {}", Strings.toString(carRepository.findAll()));
    log.info("Animals are {}", Strings.toString(animalRepository.findAll()));
  }

  void deleteAndSaveCars() {

    carRepository.deleteAll();
    carRepository.saveAll(cars);
  }

  void deleteAndSaveAnimals() {

    animalRepository.deleteAll();
    animalRepository.saveAll(animals);
  }

}