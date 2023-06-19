package com.springboot.code.example.transaction.multiple.datasource.example;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.springboot.code.example.transaction.multiple.datasource.config.ChainedTransactionManagerConfig;
import com.springboot.code.example.transaction.multiple.datasource.config.VehicleDatasourceConfig;
import com.springboot.code.example.transaction.multiple.datasource.config.WildDatasourceConfig;
import com.springboot.code.example.transaction.multiple.datasource.config.properties.DataProperties;
import com.springboot.code.example.transaction.multiple.datasource.entity.BaseEntity;
import com.springboot.code.example.transaction.multiple.datasource.vehicle.CarEntity;
import com.springboot.code.example.transaction.multiple.datasource.vehicle.CarRepository;
import com.springboot.code.example.transaction.multiple.datasource.wild.AnimalEntity;
import com.springboot.code.example.transaction.multiple.datasource.wild.AnimalRepository;

@SpringBootTest
@Import({
    DataProperties.class,
    VehicleDatasourceConfig.class,
    WildDatasourceConfig.class,
    ChainedTransactionManagerConfig.class})
class MultipleDatasourceExampleTests {

  @Autowired
  CarRepository carRepository;

  @Autowired
  AnimalRepository animalRepository;

  @Autowired
  MultipleDatasourceExample multipleDatasourceExample;

  private static final List<CarEntity> CARS = List.of(
      new CarEntity(1, "Car 1"),
      new CarEntity(2, "Car 2"),
      new CarEntity(3, "Car 3"),
      new CarEntity(4, "Car 4"));

  private static final List<AnimalEntity> ANIMALS = List.of(
      new AnimalEntity(1, "Animal 1"),
      new AnimalEntity(2, "Animal 2"),
      new AnimalEntity(3, "Animal 3"),
      new AnimalEntity(4, "Animal 4"));

  @Test
  @Transactional(value = "vehicleTransactionManager")
  void testGetCars() {

    this.deleteAndSaveCars();

    List<String> expectOutput = CARS.stream()
        .map(BaseEntity::getName)
        .toList();

    assertThat(multipleDatasourceExample.getCars())
        .hasSize(5)
        .extracting(BaseEntity::getName)
        .containsAll(expectOutput);
  }

  @Test
  @Transactional(value = "vehicleTransactionManager")
  void testGetCarsWithException() {

    this.deleteAndSaveCars();

    assertThatThrownBy(multipleDatasourceExample::getCarsWithException)
        .isInstanceOf(InvalidDataAccessResourceUsageException.class);
  }

  @Test
  @Transactional(value = "wildTransactionManager")
  void testGetAnimals() {

    this.deleteAndSaveAnimals();

    List<String> expectOutput = ANIMALS.stream()
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

    List<BaseEntity> entities = new ArrayList<>(CARS);
    entities.addAll(ANIMALS);
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
        .isInstanceOf(UnexpectedRollbackException.class);
  }

  @Test
  @Transactional(value = "vehicleTransactionManager")
  void testGetAllVehicleTransactionManager() {

    // given
    this.deleteAndSaveCars();
    this.deleteAndSaveAnimals();

    List<BaseEntity> entities = new ArrayList<>(CARS);
    entities.addAll(ANIMALS);
    List<String> expectOutput = entities.stream()
        .map(BaseEntity::getName)
        .toList();

    assertThat(multipleDatasourceExample.getAllVehicleTransactionManager())
        .hasSize(10)
        .extracting(BaseEntity::getName)
        .containsAll(expectOutput);
  }

  @Test
  @Transactional(value = "wildTransactionManager")
  void testGetAllWildTransactionManager() {

    // given
    this.deleteAndSaveCars();
    this.deleteAndSaveAnimals();

    List<BaseEntity> entities = new ArrayList<>(CARS);
    entities.addAll(ANIMALS);
    List<String> expectOutput = entities.stream()
        .map(BaseEntity::getName)
        .toList();

    assertThat(multipleDatasourceExample.getAllWildTransactionManager())
        .hasSize(10)
        .extracting(BaseEntity::getName)
        .containsAll(expectOutput);
  }

  @Test
  @Transactional(value = "chainedTransactionManager")
  void testGetAllWithInvalidDataAccessResourceUsageException() {

    // given
    this.deleteAndSaveCars();
    this.deleteAndSaveAnimals();

    assertThatThrownBy(multipleDatasourceExample::getAllWithInvalidDataAccessResourceUsageException)
        .isInstanceOf(InvalidDataAccessResourceUsageException.class);
  }

  void deleteAndSaveCars() {

    carRepository.deleteAll();
    carRepository.saveAll(CARS);
  }

  void deleteAndSaveAnimals() {

    animalRepository.deleteAll();
    animalRepository.saveAll(ANIMALS);
  }

}