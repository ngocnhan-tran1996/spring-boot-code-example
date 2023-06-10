package com.springboot.code.example.database.multiple.datasource.example;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import com.springboot.code.example.database.multiple.datasource.config.AtomikosConfig;
import com.springboot.code.example.database.multiple.datasource.config.VehicleDatasourceConfig;
import com.springboot.code.example.database.multiple.datasource.config.WildDatasourceConfig;
import com.springboot.code.example.database.multiple.datasource.entity.BaseEntity;
import com.springboot.code.example.database.multiple.datasource.vehicle.CarEntity;
import com.springboot.code.example.database.multiple.datasource.vehicle.CarRepository;
import com.springboot.code.example.database.multiple.datasource.wild.AnimalEntity;
import com.springboot.code.example.database.multiple.datasource.wild.AnimalRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import({VehicleDatasourceConfig.class, WildDatasourceConfig.class, AtomikosConfig.class})
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

    carRepository.deleteAll();
    carRepository.saveAll(cars);

    animalRepository.deleteAll();
    animalRepository.saveAll(animals);

    multipleDatasourceExample = new MultipleDatasourceExample(carRepository, animalRepository);
  }

  @Test
  void testGetAllBaseEntity() throws Exception {

    List<BaseEntity> entities = new ArrayList<>(cars);
    entities.addAll(animals);
    List<String> expectOutput = entities.stream()
        .map(BaseEntity::getName)
        .toList();

    assertThat(multipleDatasourceExample.getAllBaseEntity())
        .hasSize(10)
        .extracting(BaseEntity::getName)
        .containsAll(expectOutput);
  }

}