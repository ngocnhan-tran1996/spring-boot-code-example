package com.springboot.code.example.database.multiple.datasource.example;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import com.springboot.code.example.database.multiple.datasource.config.VehicleDatasourceConfig;
import com.springboot.code.example.database.multiple.datasource.config.WildDatasourceConfig;
import com.springboot.code.example.database.multiple.datasource.vehicle.CarEntity;
import com.springboot.code.example.database.multiple.datasource.vehicle.CardRepository;
import com.springboot.code.example.database.multiple.datasource.wild.AnimalEntity;
import com.springboot.code.example.database.multiple.datasource.wild.AnimalRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import({VehicleDatasourceConfig.class, WildDatasourceConfig.class})
class MultipleDatasourceExampleTests {

  @Autowired
  CardRepository cardRepository;

  @Autowired
  AnimalRepository animalRepository;

  MultipleDatasourceExample multipleDatasourceExample;

  @BeforeEach
  void init() {

    cardRepository.deleteAll();
    animalRepository.deleteAll();

    cardRepository.saveAll(
        List.of(
            new CarEntity(1, "Test 1"),
            new CarEntity(2, "Test 2"),
            new CarEntity(3, "Test 3"),
            new CarEntity(4, "Test 6")));
    animalRepository.saveAll(
        List.of(
            new AnimalEntity(1, "Test 1"),
            new AnimalEntity(2, "Test 2"),
            new AnimalEntity(3, "Test 3"),
            new AnimalEntity(4, "Test 4")));

    multipleDatasourceExample = new MultipleDatasourceExample(cardRepository, animalRepository);
  }

  @Test
  void testGetAllBaseEntity() {

    assertThat(multipleDatasourceExample.getAllBaseEntity())
        .hasSize(10);
  }

}