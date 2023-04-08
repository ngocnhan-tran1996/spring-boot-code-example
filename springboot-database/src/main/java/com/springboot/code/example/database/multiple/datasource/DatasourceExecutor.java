package com.springboot.code.example.database.multiple.datasource;

import org.springframework.stereotype.Service;
import com.springboot.code.example.database.multiple.datasource.entity.BaseEntity;
import com.springboot.code.example.database.multiple.datasource.vehicle.CarEntity;
import com.springboot.code.example.database.multiple.datasource.vehicle.CardRepository;
import com.springboot.code.example.database.multiple.datasource.wild.AnimalEntity;
import com.springboot.code.example.database.multiple.datasource.wild.AnimalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class DatasourceExecutor {

  private final CardRepository cardRepository;
  private final AnimalRepository animalRepository;

  public void manipulate() {

    this.print("Before udating...");

    // Updating
    var newCar = new CarEntity();
    newCar.setName("BENZ");
    cardRepository.save(newCar);

    var newAnimal = new AnimalEntity();
    newAnimal.setName("DOG");
    animalRepository.save(newAnimal);
    this.print("After udating...");

    // Deleting
    animalRepository.deleteById(1);
    cardRepository.deleteById(1);
    this.print("After deleting...");
  }

  protected void print(String content) {

    log.info(content);
    log.info("Vehicle Database");
    log.info("id | name");
    this.print(cardRepository.findAll());
    log.info("Wild Database");
    log.info("id | name");
    this.print(animalRepository.findAll());
  }

  protected <T extends BaseEntity> void print(Iterable<T> iterable) {

    iterable.forEach(entity -> log.info("{}  | {}", entity.getId(), entity.getName()));
  }

}