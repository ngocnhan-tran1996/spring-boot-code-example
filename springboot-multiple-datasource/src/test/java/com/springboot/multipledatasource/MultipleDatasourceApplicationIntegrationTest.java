package com.springboot.multipledatasource;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import com.springboot.multipledatasource.service.DataService;
import com.springboot.multipledatasource.vehicle.CarEntity;
import com.springboot.multipledatasource.vehicle.CardRepository;
import com.springboot.multipledatasource.wild.AnimalEntity;
import com.springboot.multipledatasource.wild.AnimalRepository;

@DataJpaTest
@Import(DataService.class)
class MultipleDatasourceApplicationIntegrationTest {

  @Autowired
  CardRepository cardRepository;

  @Autowired
  AnimalRepository animalRepository;

  @Test
  void testVehicleDatabase() {

    var car = new CarEntity();
    car.setName("BUS");
    var newCar = cardRepository.save(car);
    Optional<CarEntity> result = cardRepository.findById(newCar.getId());
    assertThat(result).isPresent();
  }

  @Test
  void testWildDatabase() {

    var animal = new AnimalEntity();
    animal.setName("Jerry");
    var newAnimal = animalRepository.save(animal);
    Optional<AnimalEntity> result = animalRepository.findById(newAnimal.getId());
    assertThat(result).isPresent();
  }

}