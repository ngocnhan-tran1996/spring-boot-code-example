package com.springboot.code.example.database.multiple.datasource.vehicle;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.springboot.code.example.database.multiple.datasource.annotation.MultipleDatasourceConfiguration;

@MultipleDatasourceConfiguration
@DataJpaTest
class CardRepositoryTest {

  @Autowired
  CardRepository cardRepository;

  @Test
  void testVehicleDatabase() {

    var car = new CarEntity();
    car.setName("BUS");
    var newCar = cardRepository.save(car);
    Optional<CarEntity> result = cardRepository.findById(newCar.getId());
    assertThat(result).isPresent();
  }

}