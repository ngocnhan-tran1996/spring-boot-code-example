package com.springboot.code.example.database.multiple.datasource.wild;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.springboot.code.example.database.multiple.datasource.annotation.MultipleDatasourceConfiguration;

@MultipleDatasourceConfiguration
@DataJpaTest
class AnimalRepositoryTest {

  @Autowired
  AnimalRepository animalRepository;

  @Test
  void testWildDatabase() {

    var animal = new AnimalEntity();
    animal.setName("Jerry");
    var newAnimal = animalRepository.save(animal);
    Optional<AnimalEntity> result = animalRepository.findById(newAnimal.getId());
    assertThat(result).isPresent();
  }

}